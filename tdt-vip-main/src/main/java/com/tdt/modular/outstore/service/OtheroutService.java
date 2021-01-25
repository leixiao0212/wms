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
import com.tdt.core.util.DateUtil;
import com.tdt.modular.base.entity.Commodity;
import com.tdt.modular.base.entity.Locator;
import com.tdt.modular.base.service.CommodityService;
import com.tdt.modular.base.service.LocatorService;
import com.tdt.modular.outstore.entity.Otherout;
import com.tdt.modular.outstore.mapper.OtheroutMapper;
import com.tdt.modular.outstore.model.params.OtheroutParam;
import com.tdt.modular.outstore.model.result.OtheroutResult;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.entity.StockLog;
import com.tdt.modular.repertory.mapper.StockLogMapper;
import com.tdt.modular.repertory.service.StockService;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 其他出库服务类
 */
@Service
public class OtheroutService extends ServiceImpl<OtheroutMapper, Otherout> {

    @Autowired
    private LocatorService locatorService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private StockService stockService;

    @Resource
    private StockLogMapper stockLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public void add(OtheroutParam param){
        Otherout entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addHead(Otherout otherout) {
        JSONObject resultObj = new JSONObject();
        Locator locator = locatorService.getOne(new QueryWrapper<Locator>().eq("code",otherout.getLocatorcode()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
        //判断入库库位是否存在且库位属于当前仓库
        if(locator!=null){
            Commodity commodity = commodityService.getOne(new QueryWrapper<Commodity>().eq("bar",otherout.getCommoditybar()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
            //判断商品编码是否存在且商品编码属于当前仓库
            if(commodity!=null){
                otherout.setOtheroutno("OO"+ DateUtil.getAllMsTime());
                otherout.setLocatorid(locator.getId());
                otherout.setLocatorname(locator.getName());
                otherout.setCommodityid(commodity.getId());
                otherout.setCommodityname(commodity.getName());
                otherout.setWarehouseid(ShiroKit.getUser().getWarehouseId());
                otherout.setCreateid(ShiroKit.getUser().getId());
                otherout.setCreator(ShiroKit.getUser().getName());
                otherout.setCreatetime(new Date());
                otherout.setState(Constants.OtheroutState.UNREVIEW);
                this.baseMapper.insert(otherout);
                Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorcode",otherout.getLocatorcode()).eq("commoditybar",otherout.getCommoditybar()));
                //判断此货位上的此商品数量是否足够出库
                if (stock.getUqty()>=otherout.getQty()){
                    //锁定对应货位对应商品的对应数量
                    stock.setUqty(stock.getUqty()-otherout.getQty());
                    stock.setLqty(stock.getLqty()+otherout.getQty());
                    stock.setUpdateid(ShiroKit.getUser().getId());
                    stock.setUpdator(ShiroKit.getUser().getName());
                    stock.setUpdatetime(new Date());
                    stockService.updateById(stock);
                    //记录库存日志
                    stockService.addStockLog(otherout.getOtheroutno(),locator.getWarehouseid(),locator.getId(),locator.getCode(),
                            locator.getName(),commodity.getId(),commodity.getBar(),commodity.getName(),otherout.getQty(),
                            Constants.StockLogType.LOCKSTOCK);
                    resultObj.put("code", 200);
                    resultObj.put("message", "出库成功");
                    resultObj.put("otherout", otherout);
                } else {
                    resultObj.put("code", 500);
                    resultObj.put("message", "此货位上的此商品库存数量不足，无法出库");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }else{
                resultObj.put("code", 500);
                resultObj.put("message", "商品编码不存在或此商品编码不属于当前仓库");
            }
        }else{
            resultObj.put("code", 500);
            resultObj.put("message", "库位不存在或库位不属于当前仓库");
        }

        return resultObj;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(OtheroutParam param){
        Otherout otherout = this.getById(param.getId());
        //将锁定时的库存日志置为失效
        StockLog stockLog = stockLogMapper.selectOne(new QueryWrapper<StockLog>().eq("billno",otherout.getOtheroutno()).eq("type",Constants.StockLogType.LOCKSTOCK).eq("state",Constants.StockLogState.NORMAL));
        stockLog.setState(Constants.StockLogState.INVALID);
        stockLogMapper.updateById(stockLog);
        //将锁定的库存还原
        Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",otherout.getLocatorid()).eq("commodityid",otherout.getCommodityid()));
        stock.setLqty(stock.getLqty()-otherout.getQty());
        stock.setUqty(stock.getUqty()+otherout.getQty());
        stock.setUpdateid(ShiroKit.getUser().getId());
        stock.setUpdator(ShiroKit.getUser().getName());
        stock.setUpdatetime(new Date());
        stockService.updateById(stock);
        //删除相关记录
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(OtheroutParam param){
        Otherout oldEntity = getOldEntity(param);
        Otherout newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, OtheroutParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject review(Long id) {
        JSONObject result = new JSONObject();
        Otherout otherout = this.getById(id);
        //判断其他出库状态是否为未审核
        if (otherout.getState().equals(Constants.OtheroutState.UNREVIEW)){
            otherout.setState(Constants.OtheroutState.REVIEWED);
            this.updateById(otherout);
            //将锁定的库存日志记录置为失效
            StockLog stockLog = stockLogMapper.selectOne(new QueryWrapper<StockLog>().eq("billno",otherout.getOtheroutno()).eq("type",Constants.StockLogType.LOCKSTOCK).eq("state",Constants.StockLogState.NORMAL));
            stockLog.setState(Constants.StockLogState.INVALID);
            stockLogMapper.updateById(stockLog);
            //将锁定的库存扣除
            Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorcode",otherout.getLocatorcode()).eq("commoditybar",otherout.getCommoditybar()));
            if (stock.getCqty()-otherout.getQty()==0){
                stockService.removeById(stock);
            } else {
                stock.setCqty(stock.getCqty()-otherout.getQty());
                stock.setLqty(stock.getLqty()-otherout.getQty());
                stock.setUpdateid(ShiroKit.getUser().getId());
                stock.setUpdator(ShiroKit.getUser().getName());
                stock.setUpdatetime(new Date());
                stockService.updateById(stock);
            }
            Locator locator = locatorService.getOne(new QueryWrapper<Locator>().eq("code",otherout.getLocatorcode()));
            //记录扣除的日志
            stockService.addStockLog(otherout.getOtheroutno(),locator.getWarehouseid(),locator.getId(),locator.getCode(),
                    locator.getName(),otherout.getCommodityid(),otherout.getCommoditybar(),otherout.getCommodityname(),
                    otherout.getQty(),Constants.StockLogType.OUTSTOCK);
            otherout.setAuditid(ShiroKit.getUser().getId());
            otherout.setAuditor(ShiroKit.getUser().getName());
            otherout.setAudittime(new Date());
            this.updateById(otherout);
            result.put("code", 200);
            result.put("message", "审核成功");
        } else {
            result.put("code", 500);
            result.put("message", "此订单已审核，请勿重复审核");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject cancelReview(Long id) {
        JSONObject result = new JSONObject();
        Otherout otherout = this.getById(id);
        //判断其他出库状态是否为已审核
        if (otherout.getState().equals(Constants.OtheroutState.REVIEWED)){
            otherout.setState(Constants.OtheroutState.UNREVIEW);
            this.updateById(otherout);
            //将审核时候的库存日志置位失效
            StockLog stockLog = stockLogMapper.selectOne(new QueryWrapper<StockLog>().eq("billno",otherout.getOtheroutno()).eq("type",Constants.StockLogType.OUTSTOCK).eq("state",Constants.StockLogState.NORMAL));
            stockLog.setState(Constants.StockLogState.INVALID);
            stockLogMapper.updateById(stockLog);
            //将审核扣除掉的库存加上去
            Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorcode",otherout.getLocatorcode()).eq("commoditybar",otherout.getCommoditybar()));
            stock.setCqty(stock.getCqty()+otherout.getQty());
            stock.setLqty(stock.getLqty()+otherout.getQty());
            stockService.updateById(stock);
            //将审核时锁定库存的记录还原为正常
            StockLog _stockLog = stockLogMapper.selectOne(new QueryWrapper<StockLog>().eq("billno",otherout.getOtheroutno()).eq("type",Constants.StockLogType.LOCKSTOCK).eq("state",Constants.StockLogState.INVALID));
            _stockLog.setState(Constants.StockLogState.NORMAL);
            stockLogMapper.updateById(_stockLog);
            result.put("code", 200);
            result.put("message", "撤销审核成功");
        } else {
            result.put("code", 500);
            result.put("message", "此订单还未审核，不用撤销审核");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public OtheroutResult findBySpec(OtheroutParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<OtheroutResult> findListBySpec(OtheroutParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(OtheroutParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(OtheroutParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Otherout getOldEntity(OtheroutParam param) {
        return this.getById(getKey(param));
    }

    private Otherout getEntity(OtheroutParam param) {
        Otherout entity = new Otherout();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
