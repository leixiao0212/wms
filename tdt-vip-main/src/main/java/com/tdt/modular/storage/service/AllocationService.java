package com.tdt.modular.storage.service;

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
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.service.WarehouseService;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.entity.StockLog;
import com.tdt.modular.repertory.mapper.StockLogMapper;
import com.tdt.modular.repertory.service.StockService;
import com.tdt.modular.storage.entity.Allocation;
import com.tdt.modular.storage.entity.AllocationDetail;
import com.tdt.modular.storage.mapper.AllocationMapper;
import com.tdt.modular.storage.model.params.AllocationParam;
import com.tdt.modular.storage.model.result.AllocationResult;
import com.tdt.sys.core.shiro.ShiroKit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 调拨服务类
 */
@Service
public class AllocationService extends ServiceImpl<AllocationMapper, Allocation> {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private AllocationDetailService allocationDetailService;

    @Autowired
    private StockService stockService;

    @Resource
    private StockLogMapper stockLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public void add(AllocationParam param){
        Allocation entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addHead(Allocation allocation) {
        JSONObject resultObj = new JSONObject();
        Warehouse warehouse1 = this.warehouseService.getOne(new QueryWrapper<Warehouse>().eq("id", allocation.getBwarehouseid()));
        //判断原仓库是否存在
        if (warehouse1!=null){
            Warehouse warehouse2 = this.warehouseService.getOne(new QueryWrapper<Warehouse>().eq("id", allocation.getAwarehouseid()));
            //判断目标仓库是否存在
            if (warehouse2!=null){
                //判断原仓库与目标仓库是否不同
                if (!allocation.getBwarehouseid().equals(allocation.getAwarehouseid())){
                    Allocation _allocation = this.getOne(new QueryWrapper<Allocation>().eq("allocationno", allocation.getAllocationno()));
                    //判断是否存在此调拨单号的记录
                    if (_allocation!=null){
                        resultObj.put("code", 200);
                        resultObj.put("message", "操作成功");
                        resultObj.put("allocation", _allocation);
                    } else {
                        if(StringUtils.isEmpty(allocation.getAllocationno())){
                            allocation.setAllocationno(SequenceBuilder.generateNo(Constants.PREFIX_NO.ALLOCATION_NO));
                        }
                        allocation.setBwarehousename(warehouse1.getName());
                        allocation.setAwarehousename(warehouse2.getName());
                        allocation.setState(Constants.AllocationState.INIT);
                        allocation.setCreateid(ShiroKit.getUser().getId());
                        allocation.setCreator(ShiroKit.getUser().getName());
                        allocation.setCreatetime(new Date());
                        this.save(allocation);
                        resultObj.put("code", 200);
                        resultObj.put("message", "操作成功");
                        resultObj.put("allocation", allocation);
                    }
                } else {
                    resultObj.put("code", 500);
                    resultObj.put("message", "原仓库和目标仓库不能相同，请重新选择");
                }
            } else {
                resultObj.put("code", 500);
                resultObj.put("message", "目标仓库不存在,请重新选择");
            }
        } else {
            resultObj.put("code", 500);
            resultObj.put("message", "原仓库不存在,请重新选择");
        }
        return resultObj;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(AllocationParam param){
        allocationDetailService.remove(new QueryWrapper<AllocationDetail>().eq("pid",param.getId()));
        this.removeById(param.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(AllocationParam param){
        Allocation oldEntity = getOldEntity(param);
        Allocation newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, AllocationParam paramCondition) {
        Long warehouseid = ShiroKit.getUser().getWarehouseId();
        return this.baseMapper.customMapList(page, paramCondition,warehouseid);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject finish(Integer pid) {
        JSONObject resultObj = new JSONObject();
        Allocation _allocation = this.getById(pid);
        if(_allocation.getState().equals(Constants.AllocationState.UNREVIEW)){
            resultObj.put("code", 200);
            resultObj.put("message", "调拨结束成功");
        }else{
            resultObj.put("code", 500);
            resultObj.put("message", "此调拨单已经结束,不要重复操作");
        }
        return resultObj;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject review(int id) {
        JSONObject result = new JSONObject();
        Allocation allocation = this.baseMapper.selectById(id);
        //判断调拨单状态是否为未审核
        if (allocation.getState().equals(Constants.AllocationState.UNREVIEW)){
            List<AllocationDetail> details = allocationDetailService.list(new QueryWrapper<AllocationDetail>().eq("pid", id));
            for (AllocationDetail allocationDetail:details){
                //判断调拨是从货位调还是从仓库调
                if (allocationDetail.getLocatorid()!=null){
                    Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",allocationDetail.getLocatorid()).eq("commodityid",allocationDetail.getCommodityid()));
                    //判断库存的可用数量是否足够被调拨
                    if (stock.getUqty()>=allocationDetail.getQty()){
                        stock.setUqty(stock.getUqty()-allocationDetail.getQty());
                        stock.setLqty(stock.getLqty()+allocationDetail.getQty());
                        stock.setUpdateid(ShiroKit.getUser().getId());
                        stock.setUpdator(ShiroKit.getUser().getName());
                        stock.setUpdatetime(new Date());
                        stockService.updateById(stock);
                        //库存记录
                        stockService.addStockLog(allocation.getAllocationno(),stock.getWarehouseid(),stock.getLocatorid(),
                                stock.getLocatorcode(),stock.getLocatorname(),stock.getCommodityid(),stock.getCommoditybar(),
                                stock.getCommodityname(),allocationDetail.getQty(),Constants.StockLogType.LOCKSTOCK);
                    } else {
                        result.put("code", 500);
                        result.put("message", "此商品的库存为："+stock.getUqty()+",不足以被调拨，审核失败");
                        return result;
                    }
                } else {
                    //自定义方法：根据商品数量查出库存，遍历库存依次扣除
                    List<Stock> stockList = stockService.list(new QueryWrapper<Stock>().eq("commodityid",allocationDetail.getCommodityid()));
                    Integer sum = allocationDetail.getQty();
                    for (Stock stock:stockList){
                        if (stock.getUqty()>=sum){
                            stock.setUqty(stock.getUqty()-allocationDetail.getQty());
                            stock.setLqty(stock.getLqty()+allocationDetail.getQty());
                            stock.setUpdateid(ShiroKit.getUser().getId());
                            stock.setUpdator(ShiroKit.getUser().getName());
                            stock.setUpdatetime(new Date());
                            stockService.updateById(stock);
                            //库存记录
                            stockService.addStockLog(allocation.getAllocationno(),stock.getWarehouseid(),stock.getLocatorid(),
                                    stock.getLocatorcode(), stock.getLocatorname(),stock.getCommodityid(),stock.getCommoditybar(),
                                    stock.getCommodityname(),sum,Constants.StockLogType.LOCKSTOCK);
                            break;
                        } else {
                            stock.setLqty(stock.getLqty()+stock.getUqty());
                            stock.setUqty(0);
                            stock.setUpdateid(ShiroKit.getUser().getId());
                            stock.setUpdator(ShiroKit.getUser().getName());
                            stock.setUpdatetime(new Date());
                            stockService.updateById(stock);
                            //库存记录
                            stockService.addStockLog(allocation.getAllocationno(),stock.getWarehouseid(),stock.getLocatorid(),
                                    stock.getLocatorcode(), stock.getLocatorname(),stock.getCommodityid(),stock.getCommoditybar(),
                                    stock.getCommodityname(),sum,Constants.StockLogType.LOCKSTOCK);
                            sum = sum-stock.getUqty();
                        }
                    }
                }
            }
        } else if (allocation.getState().equals(Constants.AllocationState.INIT)){
            result.put("code", 500);
            result.put("message", "此调拨单还未录入明细，无法审核");
            return result;
        } else if (allocation.getState().equals(Constants.AllocationState.REVIEW)){
            result.put("code", 500);
            result.put("message", "此调拨单已审核，请勿重复审核");
            return result;
        }
        allocation.setState(Constants.AllocationState.REVIEW);
        allocation.setAuditid(ShiroKit.getUser().getId());
        allocation.setAuditor(ShiroKit.getUser().getName());
        allocation.setAudittime(new Date());
        this.updateById(allocation);
        result.put("code", 200);
        result.put("message", "审核成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject cancelReview(int id) {
        JSONObject result = new JSONObject();
        Allocation allocation = this.baseMapper.selectById(id);
        //判断调拨单状态
        if (allocation.getState().equals(Constants.AllocationState.REVIEW)){
            List<AllocationDetail> allocationDetailList = allocationDetailService.list(new QueryWrapper<AllocationDetail>().eq("pid", id));
            for (AllocationDetail allocationDetail:allocationDetailList){
                //判断是从仓库调拨还是从货位调拨
                if (allocationDetail.getLocatorid()!=null){
                    Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",allocationDetail.getLocatorid()).eq("commodityid",allocationDetail.getCommodityid()));
                    StockLog stockLog = stockLogMapper.selectOne(new QueryWrapper<StockLog>().eq("billno",allocation.getAllocationno()).eq("locatorid",allocationDetail.getLocatorid()).eq("commodityid",allocationDetail.getCommodityid()).eq("state",Constants.StockLogState.NORMAL));
                    stock.setUqty(stock.getUqty()+stockLog.getQty());
                    stock.setLqty(stock.getLqty()-stockLog.getQty());
                    stock.setUpdateid(ShiroKit.getUser().getId());
                    stock.setUpdator(ShiroKit.getUser().getName());
                    stock.setUpdatetime(new Date());
                    stockService.updateById(stock);
                } else {
                    List<StockLog> stockLogList = stockLogMapper.selectList(new QueryWrapper<StockLog>().eq("billno",allocation.getAllocationno()).eq("commodityid",allocationDetail.getCommodityid()).eq("state",Constants.StockLogState.NORMAL));
                    for (StockLog stockLog:stockLogList){
                        Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",stockLog.getLocatorid()).eq("commodityid",stockLog.getCommodityid()));
                        stock.setUqty(stock.getUqty()+stockLog.getQty());
                        stock.setLqty(stock.getLqty()-stockLog.getQty());
                        stock.setUpdateid(ShiroKit.getUser().getId());
                        stock.setUpdator(ShiroKit.getUser().getName());
                        stock.setUpdatetime(new Date());
                        stockService.updateById(stock);
                    }
                }
            }
            //撤销审核后改变库存日志状态
            stockService.changeStockLogState(allocation.getAllocationno());
        } else {
            result.put("code", 500);
            result.put("message", "此调拨单还未审核，无法撤销审核");
            return result;
        }
        allocation.setState(Constants.AllocationState.UNREVIEW);
        this.updateById(allocation);
        result.put("code", 200);
        result.put("message", "撤销审核成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public AllocationResult findBySpec(AllocationParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<AllocationResult> findListBySpec(AllocationParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(AllocationParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(AllocationParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Allocation getOldEntity(AllocationParam param) {
        return this.getById(getKey(param));
    }

    private Allocation getEntity(AllocationParam param) {
        Allocation entity = new Allocation();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
