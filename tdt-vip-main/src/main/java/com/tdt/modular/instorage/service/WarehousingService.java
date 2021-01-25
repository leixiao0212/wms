package com.tdt.modular.instorage.service;

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
import com.tdt.modular.base.entity.Locator;
import com.tdt.modular.base.service.LocatorService;
import com.tdt.modular.instorage.entity.Receive;
import com.tdt.modular.instorage.entity.ReceiveDetail;
import com.tdt.modular.instorage.entity.Warehousing;
import com.tdt.modular.instorage.entity.WarehousingDetail;
import com.tdt.modular.instorage.mapper.WarehousingMapper;
import com.tdt.modular.instorage.model.params.WarehousingParam;
import com.tdt.modular.instorage.model.result.WarehousingResult;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.service.StockService;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *入库服务类
 */
@Service
public class WarehousingService extends ServiceImpl<WarehousingMapper, Warehousing> {

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private LocatorService locatorService;

    @Autowired
    private WarehousingDetailService warehousingDetailService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ReceiveDetailService receiveDetailService;

    @Transactional(rollbackFor = Exception.class)
    public void add(WarehousingParam param){
        Warehousing entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addHead(Warehousing warehousing){
        JSONObject resultObj = new JSONObject();
        Warehousing _warehousing = this.baseMapper.selectOne(new QueryWrapper<Warehousing>().eq("warehousingno", warehousing.getReceiveno()).or().eq("receiveno", warehousing.getReceiveno()));
        //判断是否已经存在此入库订单
        if(_warehousing != null){
            //判断入库状态是否审核
            if (_warehousing.getState().equals(Constants.WarehousingState.REVIEWED)){
                resultObj.put("code", 500);
                resultObj.put("message", "此订单已入库并且审核");
            } else {
                resultObj.put("code", 200);
                resultObj.put("message", "此订单已入库");
                resultObj.put("warehousing", _warehousing);
            }
        }else{
            Receive receive = receiveService.getOne(new QueryWrapper<Receive>().eq("receiveno", warehousing.getReceiveno()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
            //判断是否存在此接货单号且接货单号属于当前仓库
            if (receive != null){
                //判断此接货单号是否审核
                if(receive.getState().equals(Constants.ReceiveState.auditoed)){
                    Locator locator = locatorService.getOne(new QueryWrapper<Locator>().eq("code",warehousing.getLocatorcode()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                    //判断是否存在此货位且货位属于当前仓库
                    if(locator!=null){
                        //判断货位状态是否正常
                        if (locator.getState().equals(Constants.LocatorState.NORMAL)){
                            //货位是否为入库区或者大货区
                            if (locator.getType().equals(Constants.LocatorType.INSTORAGE)||locator.getType().equals(Constants.LocatorType.BIG)){
                                warehousing.setWarehousingno(SequenceBuilder.generateNo(Constants.PREFIX_NO.WAREHOUSING_NO));
                                warehousing.setLocatorid(locator.getId());
                                warehousing.setLocatorcode(locator.getCode());
                                warehousing.setLocatorname(locator.getName());
                                warehousing.setWarehouseid(ShiroKit.getUser().getWarehouseId());
                                warehousing.setReceiveno(receive.getReceiveno());
                                warehousing.setState(Constants.WarehousingState.UNFINISH);
                                warehousing.setCreateid(ShiroKit.getUser().getId());
                                warehousing.setCreator(ShiroKit.getUser().getName());
                                warehousing.setCreatetime(new Date());
                                this.baseMapper.insert(warehousing);
                                resultObj.put("code", 200);
                                resultObj.put("message", "订单入库成功");
                                resultObj.put("warehousing", warehousing);
                            } else {
                                resultObj.put("code", 500);
                                resultObj.put("message", "入库订单只能入大货区或者入库区");
                            }
                        } else if (locator.getState().equals(Constants.LocatorState.INVALID)){
                            resultObj.put("code", 500);
                            resultObj.put("message", "此货位失效，无法入库到此货位");
                        } else if (locator.getState().equals(Constants.LocatorState.LOCK)){
                            resultObj.put("code", 500);
                            resultObj.put("message", "此货位被锁定，无法入库到此货位");
                        }
                    }else{
                        resultObj.put("code", 500);
                        resultObj.put("message", "货位不存在或货位不属于当前仓库");
                    }
                }else{
                    resultObj.put("code", 500);
                    resultObj.put("message", "接货单号尚未审核,请审核后再入库");
                }
                //判断是否存在此调拨单号
            } else {
                resultObj.put("code", 500);
                resultObj.put("message", "此单号不存在或单号不属于当前仓库,请重新输入");
            }
        }
        return resultObj;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addEnd(Warehousing warehousing) {
        JSONObject resultObj = new JSONObject();
        Warehousing _warehousing = this.baseMapper.selectOne(new QueryWrapper<Warehousing>().eq("warehousingno", warehousing.getReceiveno()).or().eq("receiveno", warehousing.getReceiveno()));
        //判断是否已经存在此入库订单
        if(_warehousing != null){
            //判断入库状态是否审核
            if (_warehousing.getState().equals(Constants.WarehousingState.REVIEWED)){
                resultObj.put("code", 500);
                resultObj.put("message", "此订单已入库并且审核");
            } else {
                resultObj.put("code", 200);
                resultObj.put("message", "此订单已入库");
            }
        }else{
            Receive receive = receiveService.getOne(new QueryWrapper<Receive>().eq("receiveno", warehousing.getReceiveno()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
            //判断是否存在此接货单号且接货单号属于当前仓库
            if(receive != null){
                //判断此接货单号是否审核
                if(receive.getState().equals(Constants.ReceiveState.auditoed)){
                    Locator locator = locatorService.getOne(new QueryWrapper<Locator>().eq("code",warehousing.getLocatorcode()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                    //判断是否存在此货位且货位单号属于当前仓库
                    if(locator!=null){
                        //判断货位状态是否正常
                        if (locator.getState().equals(Constants.LocatorState.NORMAL)){
                            //货位是否为入库区或者大货区
                            if (locator.getType().equals(Constants.LocatorType.INSTORAGE)||locator.getType().equals(Constants.LocatorType.BIG)){
                                warehousing.setWarehousingno(SequenceBuilder.generateNo(Constants.PREFIX_NO.WAREHOUSING_NO));
                                warehousing.setLocatorid(locator.getId());
                                warehousing.setLocatorcode(locator.getCode());
                                warehousing.setLocatorname(locator.getName());
                                warehousing.setReceiveno(receive.getReceiveno());
                                warehousing.setState(Constants.WarehousingState.UNREVIEW);
                                warehousing.setWarehouseid(ShiroKit.getUser().getWarehouseId());
                                warehousing.setCreateid(ShiroKit.getUser().getId());
                                warehousing.setCreator(ShiroKit.getUser().getName());
                                warehousing.setCreatetime(new Date());
                                if (this.baseMapper.insert(warehousing)>0) {
                                    List<ReceiveDetail> receiveDetails = receiveDetailService.list(new QueryWrapper<ReceiveDetail>().eq("pid", receive.getId()));
                                    List<WarehousingDetail> warehousingDetails = new ArrayList<>();
                                    receiveDetails.forEach(receiveDetail -> {
                                        WarehousingDetail _warehousingDetail = new WarehousingDetail();
                                        _warehousingDetail.setPid(warehousing.getId());
                                        _warehousingDetail.setCommodityid(receiveDetail.getCommodityid());
                                        _warehousingDetail.setCommoditybar(receiveDetail.getCommoditybar());
                                        _warehousingDetail.setCommodityname(receiveDetail.getCommodityname());
                                        _warehousingDetail.setQty(receiveDetail.getQty());
                                        _warehousingDetail.setCreateid(ShiroKit.getUser().getId());
                                        _warehousingDetail.setCreator(ShiroKit.getUser().getName());
                                        _warehousingDetail.setCreatetime(new Date());
                                        warehousingDetails.add(_warehousingDetail);
                                    });
                                    warehousingDetailService.saveBatch(warehousingDetails);
                                    resultObj.put("code", 200);
                                    resultObj.put("message", "直接入库成功");
                                    resultObj.put("warehousing", warehousing);
                                }else{
                                    resultObj.put("code", 500);
                                    resultObj.put("message", "系统异常");
                                }
                            } else {
                                resultObj.put("code", 500);
                                resultObj.put("message", "入库订单只能入大货区或者入库区");
                            }
                        } else if (locator.getState().equals(Constants.LocatorState.INVALID)){
                            resultObj.put("code", 500);
                            resultObj.put("message", "此货位失效，无法入库到此货位");
                        } else if (locator.getState().equals(Constants.LocatorState.LOCK)){
                            resultObj.put("code", 500);
                            resultObj.put("message", "此货位被锁定，无法入库到此货位");
                        }
                    }else{
                        resultObj.put("code", 500);
                        resultObj.put("message", "货位不存在或货位不属于当前仓库");
                    }
                }else{
                    resultObj.put("code", 500);
                    resultObj.put("message", "接货单号尚未审核,请审核后再入库");
                }
            }else{
                resultObj.put("code", 500);
                resultObj.put("message", "接货单号不存在或接货单号不属于当前仓库,请重新输入");
            }
        }
        return resultObj;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject finish(WarehousingDetail warehousingDetail) {
        JSONObject resultObj = new JSONObject();
        Warehousing warehousing = this.baseMapper.selectById(warehousingDetail.getPid());
        //判断入库状态是否不为已审核
        if(!warehousing.getState().equals(Constants.WarehousingState.REVIEWED)){
            resultObj.put("code", 200);
            resultObj.put("message", "操作成功");
        }else{
            resultObj.put("code", 500);
            resultObj.put("message", "此订单已经完成入库,不要重复操作");
        }
        return resultObj;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(WarehousingParam param){
        warehousingDetailService.remove(new QueryWrapper<WarehousingDetail>().eq("pid",param.getId()));
        this.removeById(param.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(WarehousingParam warehousingParam){
        this.baseMapper.deleteAll(warehousingParam.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(WarehousingParam param){
        Warehousing oldEntity = getOldEntity(param);
        Warehousing newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        newEntity.setUpdateid(ShiroKit.getUser().getId());
        newEntity.setUpdator(ShiroKit.getUser().getName());
        newEntity.setUpdatetime(new Date());
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, WarehousingParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public WarehousingResult findBySpec(WarehousingParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<WarehousingResult> findListBySpec(WarehousingParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(WarehousingParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject review(Integer id) {
        JSONObject result = new JSONObject();
        Warehousing warehousing = this.baseMapper.selectById(id);
        //判断是否存在此入库信息
        if(warehousing != null){
            //判断入库信息的状态是否为未审核
            if(warehousing.getState().equals(Constants.WarehousingState.UNREVIEW)){
                Receive receive = receiveService.getOne(new QueryWrapper<Receive>().eq("receiveno",warehousing.getReceiveno()));
                List<ReceiveDetail> receiveDetailList = receiveDetailService.list(new QueryWrapper<ReceiveDetail>().eq("pid",receive.getId()));
                for (ReceiveDetail receiveDetail:receiveDetailList){
                    WarehousingDetail warehousingDetail = warehousingDetailService.getOne(new QueryWrapper<WarehousingDetail>().eq("pid",warehousing.getId()).eq("commodityid",receiveDetail.getCommodityid()).eq("qty",receiveDetail.getQty()));
                    //判断入库明细与接货明细是否一致
                    if (warehousingDetail==null){
                        result.put("code", 500);
                        result.put("message", "入库明细与接货明细不一致，请核对后再入库");
                        return result;
                    }
                }
                warehousing.setState(Constants.WarehousingState.REVIEWED);
                warehousing.setAuditid(ShiroKit.getUser().getId());
                warehousing.setAuditor(ShiroKit.getUser().getName());
                warehousing.setAudittime(new Date());
                this.baseMapper.updateById(warehousing);
                List<WarehousingDetail> warehousingDetails = warehousingDetailService.list(new QueryWrapper<WarehousingDetail>().eq("pid", id));
                warehousingDetails.forEach(warehousingDetail -> {
                    Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",warehousing.getLocatorid()).eq("commodityid",warehousingDetail.getCommodityid()));
                    //判断库存中是否存在相同货位和商品信息的记录
                    if (stock!=null){
                        //覆盖掉原来的数据来源单号
                        stock.setBillno(warehousing.getWarehousingno());
                        stock.setCqty(stock.getCqty()+warehousingDetail.getQty());
                        stock.setUqty(stock.getUqty()+warehousingDetail.getQty());
                        stock.setUpdateid(ShiroKit.getUser().getId());
                        stock.setUpdator(ShiroKit.getUser().getName());
                        stock.setUpdatetime(new Date());
                        stockService.updateById(stock);
                        //库存记录
                        stockService.addStockLog(warehousing.getWarehousingno(),stock.getWarehouseid(),stock.getLocatorid(),stock.getLocatorcode(),
                                stock.getLocatorname(),stock.getCommodityid(),stock.getCommoditybar(),stock.getCommodityname(),
                                warehousingDetail.getQty(),Constants.StockLogType.INSTOCK);
                    } else {
                        stock = new Stock();
                        stock.setBillno(warehousing.getWarehousingno());
                        stock.setCommodityid(warehousingDetail.getCommodityid());
                        stock.setCommoditybar(warehousingDetail.getCommoditybar());
                        stock.setCommodityname(warehousingDetail.getCommodityname());
                        stock.setLocatorid(warehousing.getLocatorid());
                        stock.setLocatorcode(warehousing.getLocatorcode());
                        stock.setLocatorname(warehousing.getLocatorname());
                        stock.setCqty(warehousingDetail.getQty());
                        stock.setUqty(warehousingDetail.getQty());
                        stock.setLqty(0);
                        stock.setLocatorstate(Constants.LocatorState.NORMAL);
                        Locator locator = locatorService.getOne(new QueryWrapper<Locator>().eq("id",warehousing.getLocatorid()));
                        stock.setWarehouseid(locator.getWarehouseid());
                        stock.setCreateid(ShiroKit.getUser().getId());
                        stock.setCreator(ShiroKit.getUser().getName());
                        stock.setCreatetime(new Date());
                        stockService.save(stock);
                        //库存记录
                        stockService.addStockLog(warehousing.getWarehousingno(),locator.getWarehouseid(),
                                warehousing.getLocatorid(),warehousing.getLocatorcode(), warehousing.getLocatorname(),
                                warehousingDetail.getCommodityid(),warehousingDetail.getCommoditybar(),
                                warehousingDetail.getCommodityname(),warehousingDetail.getQty(),Constants.StockLogType.INSTOCK);
                    }
                });
                result.put("code", 200);
                result.put("message", "审核成功");
            }else if(warehousing.getState() == Constants.WarehousingState.UNFINISH){
                result.put("code", 500);
                result.put("message", "入库信息未录入完成,不能审核");
            }else{
                result.put("code", 500);
                result.put("message", "入库信息已经审核,请勿重复审核");
            }
        }else {
            result.put("code", 500);
            result.put("message", "入库信息不存在");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject cancelReview(Integer id) {
        JSONObject result = new JSONObject();
        Warehousing warehousing = this.baseMapper.selectById(id);
        if(warehousing != null){
            //判断入库订单是否被审核
            if(warehousing.getState().equals(Constants.WarehousingState.REVIEWED)){
                warehousing.setState(Constants.WarehousingState.UNREVIEW);
                this.baseMapper.updateById(warehousing);
                List<WarehousingDetail> warehousingDetails = warehousingDetailService.list(new QueryWrapper<WarehousingDetail>().eq("pid", id));
                warehousingDetails.forEach(warehousingDetail -> {
                    Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",warehousing.getLocatorid()).eq("commodityid",warehousingDetail.getCommodityid()));
                    if (stock.getCqty()-warehousingDetail.getQty() == 0){
                        stockService.removeById(stock.getId());
                    } else {
                        stock.setCqty(stock.getCqty()-warehousingDetail.getQty());
                        stock.setUqty(stock.getUqty()-warehousingDetail.getQty());
                        stock.setUpdateid(ShiroKit.getUser().getId());
                        stock.setUpdator(ShiroKit.getUser().getName());
                        stock.setUpdatetime(new Date());
                        stockService.updateById(stock);
                    }
                    //修改库存日志状态
                    stockService.changeStockLogState(warehousing.getWarehousingno());
                });
                result.put("code", 200);
                result.put("message", "撤销审核成功");
            }else{
                result.put("code", 500);
                result.put("message", "入库信息尚未审核,不用撤销审核");
            }
        }else {
            result.put("code", 500);
            result.put("message", "入库信息不存在");
        }
        return result;
    }

    private Serializable getKey(WarehousingParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Warehousing getOldEntity(WarehousingParam param) {
        return this.getById(getKey(param));
    }

    private Warehousing getEntity(WarehousingParam param) {
        Warehousing entity = new Warehousing();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
