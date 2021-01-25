package com.tdt.modular.outstore.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.core.common.constant.Constants;
import com.tdt.core.util.SequenceBuilder;
import com.tdt.modular.base.entity.Commodity;
import com.tdt.modular.base.entity.Locator;
import com.tdt.modular.base.service.CommodityService;
import com.tdt.modular.base.service.LocatorService;
import com.tdt.modular.outstore.entity.*;
import com.tdt.modular.outstore.mapper.OutorderDetailMapper;
import com.tdt.modular.outstore.mapper.OutorderMapper;
import com.tdt.modular.outstore.mapper.PicktaskLockDetailMapper;
import com.tdt.modular.outstore.model.params.OutorderParam;
import com.tdt.modular.outstore.model.result.OutorderResult;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.entity.StockLog;
import com.tdt.modular.repertory.mapper.StockLogMapper;
import com.tdt.modular.repertory.mapper.StockMapper;
import com.tdt.modular.repertory.service.StockService;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * 出库订单服务类
 */
@Service
public class OutorderService extends ServiceImpl<OutorderMapper, Outorder> {

    @Resource
    private OutorderDetailMapper outorderDetailMapper;

    @Autowired
    private OutorderDetailService outorderDetailService;

    @Autowired
    private OutorderService outorderService;

    @Autowired
    private OutorderTagService outorderTagService;

    @Autowired
    private PicktaskService picktaskService;

    @Autowired
    private PicktaskOutorderService picktaskOutorderService;

    @Autowired
    private PicktaskLockDetailService picktaskLockDetailService;

    @Resource
    private PicktaskLockDetailMapper picktaskLockDetailMapper;

    @Autowired
    private StockService stockService;

    @Resource
    private StockMapper stockMapper;

    @Resource
    private StockLogMapper stockLogMapper;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private PicktaskDetailService picktaskDetailService;

    @Autowired
    private LocatorService locatorService;

    @Transactional(rollbackFor = Exception.class)
    public Outorder add(OutorderParam param){
        Outorder entity = getEntity(param);
        entity.setOutorderno(param.getOutorderno());
        entity.setWarehouseid(param.getWarehouseid());
        entity.setQty(param.getQty());
        entity.setState(Constants.OutorderState.INIT);
        entity.setRemarks(param.getRemarks());
        entity.setReceiver(param.getReceiver());
        entity.setProvince(param.getProvince());
        entity.setCity(param.getCity());
        entity.setCounty(param.getCounty());
        entity.setAddress(param.getAddress());
        entity.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        entity.setCreateid(ShiroKit.getUser().getId());
        entity.setCreator(ShiroKit.getUser().getName());
        entity.setCreatetime(new Date());
        this.save(entity);
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addPick(String ids){
        JSONObject result = new JSONObject();
        String[] _ids = ids.split(",");
        //统计出库订单id在传入的id数组中且状态为已审核的数量
        int count = this.count(new QueryWrapper<Outorder>().in("id",_ids).ne("state",Constants.OutorderState.REVIEW));
        if(count>0){
            for (int i=0;i<_ids.length;i++){
                Outorder outorder = this.getOne(new QueryWrapper<Outorder>().eq("id",_ids[i]));
                //判断出库订单状态是否不为已审核
                if (outorder.getState().equals(Constants.OutorderState.INIT)){
                    result.put("code", 500);
                    result.put("message", "出货订单号为"+outorder.getOutorderno()+"的订单尚未录入出库明细，生成拣货任务失败");
                    return result;
                } else if (outorder.getState().equals(Constants.OutorderState.UNREVIEW)){
                    result.put("code", 500);
                    result.put("message", "出货订单号为"+outorder.getOutorderno()+"的订单尚未审核，生成拣货任务失败");
                    return result;
                } else if (outorder.getState().equals(Constants.OutorderState.DISTRIBUTED)){
                    result.put("code", 500);
                    result.put("message", "出货订单号为"+outorder.getOutorderno()+"的订单已分配，生成拣货任务失败");
                    return result;
                } else if (outorder.getState().equals(Constants.OutorderState.PICKING)){
                    result.put("code", 500);
                    result.put("message", "出货订单号为"+outorder.getOutorderno()+"的订单已拣货，生成拣货任务失败");
                    return result;
                } else if (outorder.getState().equals(Constants.OutorderState.OUTSTORED)){
                    result.put("code", 500);
                    result.put("message", "出货订单号为"+outorder.getOutorderno()+"的订单已出库完成，生成拣货任务失败");
                    return result;
                }
            }
        } else {
            //根据查询出来此拣货任务中所有出库订单明细id来统计此拣货任务中某种商品及对应的数量
            List<Long> noenough = outorderDetailMapper.noenough(_ids);
            if(noenough!=null&&noenough.size()>0){
                result.put("code", 500);
                result.put("message","商品库存不足，生成拣货任务失败");
                return result;
            }
            List<Outorder> outorders = (List<Outorder>)outorderService.listByIds(Arrays.asList(_ids));
            for(int i=0;outorders!=null&&i<outorders.size();i++){
                outorders.get(i).setState(Constants.OutorderState.DISTRIBUTED);
            }
            outorderService.updateBatchById(outorders);
            outorderTagService.remove(new QueryWrapper<OutorderTag>().in("pid",ids));
            //添加picktask表信息
            Picktask picktask = new Picktask();
            String picktaskno = SequenceBuilder.generateNo(Constants.PREFIX_NO.PICKTASK_NO);
            picktask.setPicktaskno(picktaskno);
            picktask.setWarehouseid(ShiroKit.getUser().getWarehouseId());
            picktask.setCreateid(ShiroKit.getUser().getId());
            picktask.setCreator(ShiroKit.getUser().getName());
            picktask.setCreatetime(new Date());
            picktaskService.save(picktask);
            //遍历符合条件的出库订单id
            for(int i=0;outorders!=null&&i<outorders.size();i++){
                Outorder outorder=outorders.get(i);
                //添加picktaskoutorder表信息
                PicktaskOutorder picktaskOutorder = new PicktaskOutorder();
                picktaskOutorder.setPid(picktask.getId());
                picktaskOutorder.setOutorderid(outorder.getId());
                picktaskOutorder.setOutorderno(outorder.getOutorderno());
                picktaskOutorderService.save(picktaskOutorder);
            }
            List<Map<String, Object>> commodityQtyMapList = outorderDetailMapper.countCommodityQty(_ids);
            //此循环用来保存picktaskdetail数据
            for (int p=0;p<commodityQtyMapList.size();p++){
                //查询具体的商品及所对应的数量
                Long commodityId = Long.valueOf(commodityQtyMapList.get(p).get("commodityid").toString());
                Integer qtySum = Integer.valueOf(commodityQtyMapList.get(p).get("sqty").toString());
                //查出当前商品的详细信息
                //添加拣货任务明细数据（pid,commodityid,commoditybar,commodityname）
                PicktaskDetail picktaskDetail = new PicktaskDetail();
                picktaskDetail.setPid(picktask.getId());
                picktaskDetail.setCommodityid((Long)commodityQtyMapList.get(p).get("commodityid"));
                picktaskDetail.setCommoditybar(commodityQtyMapList.get(p).get("commodityid").toString());
                picktaskDetail.setCommodityname(commodityQtyMapList.get(p).get("commodityid").toString());
                //查询拥有此商品库存且货位为普通货位的list列表
                List<Stock> stockList = stockMapper.queryCommonLocatorStock(commodityId);
                //遍历拥有此商品的库存列表
                for (Stock stock:stockList){
                    //判断当前的库存可用数量是否大于商品所需数量
                    if (stock.getUqty()>=qtySum){
                        stock.setUqty(stock.getUqty()-qtySum);
                        stock.setLqty(stock.getLqty()+qtySum);
                        stock.setUpdateid(ShiroKit.getUser().getId());
                        stock.setUpdator(ShiroKit.getUser().getName());
                        stock.setUpdatetime(new Date());
                        stockService.updateById(stock);
                        StockLog stockLog = stockService.addStockLog(picktask.getPicktaskno(),stock.getWarehouseid(),stock.getLocatorid(),
                                stock.getLocatorcode(),stock.getLocatorname(),stock.getCommodityid(),stock.getCommoditybar(),
                                stock.getCommodityname(),qtySum,Constants.StockLogType.LOCKSTOCK);
                        //添加剩余的拣货任务明细（stockid,stocklogid,qty,locatorid,locatorcode,locatorname）
                        picktaskDetail.setId(null);
                        picktaskDetail.setStockid(stock.getId());
                        picktaskDetail.setStocklogid(stockLog.getId());
                        picktaskDetail.setQty(qtySum);
                        picktaskDetail.setLocatorid(stock.getLocatorid());
                        picktaskDetail.setLocatorcode(stock.getLocatorcode());
                        picktaskDetail.setLocatorname(stock.getLocatorname());
                        picktaskDetailService.save(picktaskDetail);
                        break;
                    } else {
                        picktaskDetail.setId(null);
                        picktaskDetail.setQty(stock.getUqty());
                        stock.setLqty(stock.getLqty()+stock.getUqty());
                        qtySum = qtySum-stock.getUqty();
                        stock.setUqty(0);
                        stock.setUpdateid(ShiroKit.getUser().getId());
                        stock.setUpdator(ShiroKit.getUser().getName());
                        stock.setUpdatetime(new Date());
                        stockService.updateById(stock);
                        StockLog stockLog = stockService.addStockLog(picktask.getPicktaskno(),stock.getWarehouseid(),stock.getLocatorid(),
                                stock.getLocatorcode(),stock.getLocatorname(),stock.getCommodityid(),stock.getCommoditybar(),
                                stock.getCommodityname(),stock.getUqty(),Constants.StockLogType.LOCKSTOCK);
                        picktaskDetail.setStockid(stock.getId());
                        picktaskDetail.setStocklogid(stockLog.getId());
                        picktaskDetail.setLocatorid(stock.getLocatorid());
                        picktaskDetail.setLocatorcode(stock.getLocatorcode());
                        picktaskDetail.setLocatorname(stock.getLocatorname());
                        picktaskDetailService.save(picktaskDetail);
                    }
                }
            }
            //根据已锁定的库存分配每一条出库订单中每一种商品在哪一个货位上扣除了多少数量
            //根据拣货单号,状态正常，类型为锁定查询出锁定库存的日志记录并根据商品编码排序显示
            List<StockLog> stockLogList = stockLogMapper.selectListByBillno(picktaskno,Constants.StockLogState.NORMAL,Constants.StockLogType.LOCKSTOCK);
            for (StockLog stockLog:stockLogList){
                int stockLogQty = stockLog.getQty();
                //根据当前商品id和出库订单的id集合查询出符合条件的出库订单明细
                List<OutorderDetail> outorderDetailList = outorderDetailMapper.selectListByCommodityIdAndOutorderId(stockLog.getCommodityid(),_ids);
                for (OutorderDetail outorderDetail:outorderDetailList){
                    //统计当前出库明细已插入到picktasklockdetail表中的数量
                    int qty = picktaskLockDetailMapper.countQty(outorderDetail.getId());
                    //判断当前出库订单明细是否已分配完成
                    if (outorderDetail.getQty()!=qty){
                        int outorderdetailQty = outorderDetail.getQty()-qty;
                        //添加picktask_lock_detail表数据
                        PicktaskLockDetail picktaskLockDetail = new PicktaskLockDetail();
                        picktaskLockDetail.setPid(picktask.getId());
                        picktaskLockDetail.setPicktaskno(picktaskno);
                        picktaskLockDetail.setOutorderid(outorderDetail.getPid());
                        Outorder outorder = outorderService.getById(outorderDetail.getPid());
                        picktaskLockDetail.setOutorderno(outorder.getOutorderno());
                        picktaskLockDetail.setOutorderdetailid(outorderDetail.getId());
                        picktaskLockDetail.setCommodityid(stockLog.getCommodityid());
                        picktaskLockDetail.setCommoditybar(stockLog.getCommoditybar());
                        picktaskLockDetail.setCommodityname(stockLog.getCommodityname());
                        picktaskLockDetail.setLocatorid(stockLog.getLocatorid());
                        picktaskLockDetail.setLocatorcode(stockLog.getLocatorcode());
                        picktaskLockDetail.setLocatorname(stockLog.getLocatorname());
                        //判断当前日志的商品数量是否大于当前出库明细数量
                        if (stockLogQty>=outorderdetailQty){
                            picktaskLockDetail.setQty(outorderdetailQty);
                            picktaskLockDetailService.save(picktaskLockDetail);
                            stockLogQty = stockLogQty - outorderdetailQty;
                            if (stockLogQty==0){
                                break;
                            }
                        } else {
                            picktaskLockDetail.setQty(stockLogQty);
                            picktaskLockDetailService.save(picktaskLockDetail);
                            break;
                        }
                    }
                }
            }
        }
        result.put("code", 200);
        result.put("message","生成拣货任务成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject removeOutorder(Long id) {
        JSONObject result = new JSONObject();
        Outorder outorder = this.getById(id);
        //判断出库订单是否未被领取（状态不为拣货中）
        if (outorder.getState().equals(Constants.OutorderState.OUTSTORED)||outorder.getState().equals(Constants.OutorderState.INVALID)){
            result.put("code", 500);
            result.put("message", "出库订单取消失败");
            return result;
        }
        if (outorder.getState().equals(Constants.OutorderState.DISTRIBUTED)){
            //查询与此出库订单有关的拣货任务记录
            PicktaskOutorder picktaskOutorder = picktaskOutorderService.getOne(new QueryWrapper<PicktaskOutorder>().eq("outorderid",outorder.getId()));
            //判断出库订单是否已生成拣货任务
            if (picktaskOutorder!=null){
                Picktask picktask = picktaskService.getById(picktaskOutorder.getPid());
                //将与其有关的拣货日志的记录置为失效
                List<StockLog> stockLogList = stockLogMapper.selectList(new QueryWrapper<StockLog>().eq("picktaskno",picktask.getPicktaskno()).eq("state",Constants.StockLogState.NORMAL).eq("type",Constants.StockLogType.LOCKSTOCK));
                for (StockLog stockLog:stockLogList){
                    stockLog.setState(Constants.StockLogState.INVALID);
                    stockLogMapper.updateById(stockLog);
                }
                //判断此拣货任务中是否只有一个出库订单
                List<PicktaskOutorder> picktaskOutorderList = picktaskOutorderService.list(new QueryWrapper<PicktaskOutorder>().eq("pid",picktask.getId()));
                if (picktaskOutorderList.size()==1){
                    //将此拣货任务锁定的库存还原
                    List<PicktaskDetail> picktaskDetailList = picktaskDetailService.list(new QueryWrapper<PicktaskDetail>().eq("pid",picktask.getId()));
                    for (PicktaskDetail picktaskDetail:picktaskDetailList){
                        Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",picktaskDetail.getLocatorid()).eq("commodityid",picktaskDetail.getCommodityid()));
                        stock.setLqty(stock.getLqty()-picktaskDetail.getQty());
                        stock.setUqty(stock.getUqty()+picktaskDetail.getQty());
                        stock.setUpdateid(ShiroKit.getUser().getId());
                        stock.setUpdator(ShiroKit.getUser().getName());
                        stock.setUpdatetime(new Date());
                        stockService.updateById(stock);
                    }
                    //删除此拣货任务表中的相关数据
                    picktaskOutorderService.remove(new QueryWrapper<PicktaskOutorder>().eq("pid",picktask.getId()));
                    picktaskDetailService.remove(new QueryWrapper<PicktaskDetail>().eq("pid",picktask.getId()));
                    picktaskLockDetailService.remove(new QueryWrapper<PicktaskLockDetail>().eq("pid",picktask.getId()));
                    picktaskService.removeById(picktask);
                } else {
                    List<PicktaskLockDetail> picktaskLockDetailList = picktaskLockDetailService.list(new QueryWrapper<PicktaskLockDetail>().eq("picktaskno",picktask.getPicktaskno()).eq("outorderid",outorder.getId()));
                    for (PicktaskLockDetail picktaskLockDetail:picktaskLockDetailList){
                        //将此出库订单在对应货位上锁定的对应商品还原
                        Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",picktaskLockDetail.getLocatorid()).eq("commodityid",picktaskLockDetail.getCommodityid()));
                        stock.setLqty(stock.getLqty()-picktaskLockDetail.getQty());
                        stock.setUqty(stock.getUqty()+picktaskLockDetail.getQty());
                        stock.setUpdateid(ShiroKit.getUser().getId());
                        stock.setUpdator(ShiroKit.getUser().getName());
                        stock.setUpdatetime(new Date());
                        stockService.updateById(stock);
                        //修改picktaskdetail表中对应的数量
                        PicktaskDetail picktaskDetail = picktaskDetailService.getOne(new QueryWrapper<PicktaskDetail>().eq("pid",picktask.getId()).eq("locatorid",stock.getLocatorid()).eq("commodityid",stock.getCommodityid()));
                        if (picktaskDetail.getQty()-picktaskLockDetail.getQty()==0){
                            picktaskDetailService.removeById(picktaskDetail);
                        } else {
                            picktaskDetail.setQty(picktaskDetail.getQty()-picktaskLockDetail.getQty());
                            picktaskDetailService.updateById(picktaskDetail);
                        }
                    }
                    //删除对应的数据
                    picktaskOutorderService.remove(new QueryWrapper<PicktaskOutorder>().eq("outorderid",outorder.getId()));
                    picktaskLockDetailService.remove(new QueryWrapper<PicktaskLockDetail>().eq("outorderid",outorder.getId()));
                    //加上修改后的库存日志
                    //统计此订单号下的商品和货位相同情况下的累计数量
                    List<Map<String,Object>> commodityQtyMapList = picktaskLockDetailMapper.countCommodityQty(picktask.getPicktaskno());
                    for (int i=0;i<commodityQtyMapList.size();i++){
                        Long commodityid = (Long) commodityQtyMapList.get(i).get("commodityid");
                        Long locatorid = (Long) commodityQtyMapList.get(i).get("locatorid");
                        Integer qtySum = Integer.valueOf(commodityQtyMapList.get(i).get("SUM(qty)").toString());
                        Commodity commodity = commodityService.getById(commodityid);
                        Locator locator = locatorService.getById(locatorid);
                        stockService.addStockLog(picktask.getPicktaskno(),locator.getWarehouseid(),locator.getId(),
                                locator.getCode(),locator.getName(),commodity.getId(),commodity.getBar(),
                                commodity.getName(),qtySum,Constants.StockLogType.LOCKSTOCK);
                    }
                }
            }
            outorder.setState(Constants.OutorderState.INVALID);
            this.updateById(outorder);
        } else {
            //将出库订单状态置为失效
            outorder.setState(Constants.OutorderState.INVALID);
            outorderService.updateById(outorder);
        }
        result.put("code", 200);
        result.put("message", "出库订单取消成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(OutorderParam param){
        this.baseMapper.deleteAll(param.getId());
        outorderTagService.remove(new QueryWrapper<OutorderTag>().eq("pid",param.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(OutorderParam param){
        Outorder oldEntity = getOldEntity(param);
        Outorder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        outorderTagService.remove(new QueryWrapper<OutorderTag>().eq("pid",param.getId()));
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject finish(OutorderDetail outorderDetail) {
        JSONObject resultObj = new JSONObject();
        Outorder outorder = this.getById(outorderDetail.getPid());
        //如果出库订单还未审核,计算商品种类
        if(!outorder.getState().equals(Constants.OutorderState.REVIEW)){
            //计算商品种类
            outorder.setCategory(outorderDetailMapper.countCategory(outorder.getId()));
            //计算商品总数量
            int sum = 0;
            List<OutorderDetail> outorderDetailList = outorderDetailService.list(new QueryWrapper<OutorderDetail>().eq("pid",outorder.getId()));
            for (OutorderDetail _outorderDetail:outorderDetailList){
                sum = sum + _outorderDetail.getQty();
            }
            outorder.setQty(sum);
            this.updateById(outorder);
            resultObj.put("code", 200);
            resultObj.put("message", "出库订单录入完成");
        }else{
            resultObj.put("code", 500);
            resultObj.put("message", "此采购订单已经结束录入,不要重复操作");
        }
        return resultObj;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, OutorderParam paramCondition,Date stime,Date etime) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition,stime,etime);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject review(Long id) {
        JSONObject result = new JSONObject();
        Outorder outorder = this.getById(id);
        //判断出库订单状态是否为未审核
        if (outorder.getState().equals(Constants.OutorderState.UNREVIEW)){
            //改变订单状态
            outorder.setState(Constants.OutorderState.REVIEW);
            this.updateById(outorder);
            result.put("code", 200);
            result.put("message", "审核成功");
        } else if (outorder.getState().equals(Constants.OutorderState.REVIEW)){
            result.put("code", 500);
            result.put("message", "此订单已审核,请勿重复审核");
        } else {
            result.put("code", 500);
            result.put("message", "此订单尚未录入明细,不用审核");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject cancelReview(Long id) {
        JSONObject result = new JSONObject();
        Outorder outorder = this.getById(id);
        //判断出库订单状态是否为已审核
        if (outorder.getState().equals(Constants.OutorderState.REVIEW)){
            //改变订单状态
            outorder.setState(Constants.OutorderState.UNREVIEW);
            outorder.setCategory(0);
            this.updateById(outorder);
            result.put("code", 200);
            result.put("message", "撤销审核成功");
        } else {
            result.put("code", 500);
            result.put("message", "此订单还未审核，不用撤销审核");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public OutorderResult findBySpec(OutorderParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<OutorderResult> findListBySpec(OutorderParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(OutorderParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(OutorderParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Outorder getOldEntity(OutorderParam param) {
        return this.getById(getKey(param));
    }

    private Outorder getEntity(OutorderParam param) {
        Outorder entity = new Outorder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
}
