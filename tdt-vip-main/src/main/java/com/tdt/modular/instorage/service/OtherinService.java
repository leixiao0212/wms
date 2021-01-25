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
import com.tdt.core.util.DateUtil;
import com.tdt.modular.base.entity.Commodity;
import com.tdt.modular.base.entity.Locator;
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.service.CommodityService;
import com.tdt.modular.base.service.LocatorService;
import com.tdt.modular.base.service.WarehouseService;
import com.tdt.modular.instorage.entity.Otherin;
import com.tdt.modular.instorage.mapper.OtherinMapper;
import com.tdt.modular.instorage.model.params.OtherinParam;
import com.tdt.modular.instorage.model.result.OtherinResult;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.service.StockService;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 其它入库管理服务类
 */
@Service
public class OtherinService extends ServiceImpl<OtherinMapper, Otherin> {

    @Autowired
    private LocatorService locatorService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private StockService stockService;

    @Autowired
    private WarehouseService warehouseService;

    @Transactional(rollbackFor = Exception.class)
    public void add(OtherinParam param){
        Otherin entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addHead(Otherin otherin) {
        JSONObject resultObj = new JSONObject();
        Locator locator = locatorService.getOne(new QueryWrapper<Locator>().eq("code",otherin.getLocatorcode()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
        //判断入库库位是否存在且属于当前仓库
        if(locator!=null){
            Commodity commodity = commodityService.getOne(new QueryWrapper<Commodity>().eq("bar",otherin.getCommoditybar()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
            //判断商品编码是否存在且商品编码属于当前仓库
            if(commodity!=null){
                otherin.setOtherinno("IO"+ DateUtil.getAllMsTime());
                otherin.setLocatorid(locator.getId());
                otherin.setLocatorname(locator.getName());
                otherin.setCommodityid(commodity.getId());
                otherin.setCommodityname(commodity.getName());
                otherin.setWarehouseid(ShiroKit.getUser().getWarehouseId());
                otherin.setCreateid(ShiroKit.getUser().getId());
                otherin.setCreator(ShiroKit.getUser().getName());
                otherin.setCreatetime(new Date());
                otherin.setState(Constants.OtherinState.UNREVIEW);
                this.baseMapper.insert(otherin);
                resultObj.put("code", 200);
                resultObj.put("message", "入库成功");
                resultObj.put("otherin", otherin);
            }else{
                resultObj.put("code", 500);
                resultObj.put("message", "商品编码不存在或此商品编码不属于当前仓库");
            }
        }else{
            resultObj.put("code", 500);
            resultObj.put("message", "库位不存在");
        }

        return resultObj;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(OtherinParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(OtherinParam param){
        Otherin oldEntity = getOldEntity(param);
        Otherin newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        newEntity.setUpdateid(ShiroKit.getUser().getId());
        newEntity.setUpdator(ShiroKit.getUser().getName());
        newEntity.setUpdatetime(new Date());
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, OtherinParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public OtherinResult findBySpec(OtherinParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<OtherinResult> findListBySpec(OtherinParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(OtherinParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject review(Integer id) {
        JSONObject result = new JSONObject();
        Otherin otherin = this.baseMapper.selectById(id);
        //判断其他入库中是否有这条记录
        if(otherin != null){
            //判断入库状态是否审核
            if(otherin.getState().equals(Constants.OtherinState.UNREVIEW)){
                otherin.setState(Constants.OtherinState.REVIEWED);
                otherin.setAuditid(ShiroKit.getUser().getId());
                otherin.setAuditor(ShiroKit.getUser().getName());
                otherin.setAudittime(new Date());
                this.baseMapper.updateById(otherin);
                Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",otherin.getLocatorid()).eq("commodityid",otherin.getCommodityid()));
                if (stock!=null){
                    //覆盖掉原来的数据来源单号
                    stock.setBillno(otherin.getOtherinno());
                    stock.setCqty(stock.getCqty()+otherin.getQty());
                    stock.setUqty(stock.getUqty()+otherin.getQty());
                    stockService.updateById(stock);
                    //库存记录
                    stockService.addStockLog(otherin.getOtherinno(),stock.getWarehouseid(),stock.getLocatorid(),stock.getLocatorcode(),
                            stock.getLocatorname(),stock.getCommodityid(),stock.getCommoditybar(),stock.getCommodityname(),
                            otherin.getQty(),Constants.StockLogType.INSTOCK);
                } else {
                    stock = new Stock();
                    stock.setBillno(otherin.getOtherinno());
                    stock.setCommodityid(otherin.getCommodityid());
                    stock.setCommoditybar(otherin.getCommoditybar());
                    stock.setCommodityname(otherin.getCommodityname());
                    stock.setLocatorid(otherin.getLocatorid());
                    stock.setLocatorcode(otherin.getLocatorcode());
                    stock.setLocatorname(otherin.getLocatorname());
                    stock.setCqty(otherin.getQty());
                    stock.setUqty(otherin.getQty());
                    stock.setLqty(0);
                    stock.setLocatorstate("0");
                    Locator locator = locatorService.getOne(new QueryWrapper<Locator>().eq("id",otherin.getLocatorid()));
                    Warehouse warehouse = warehouseService.getOne(new QueryWrapper<Warehouse>().eq("id",locator.getWarehouseid()));
                    stock.setWarehouseid(warehouse.getId());
                    stock.setCreateid(ShiroKit.getUser().getId());
                    stock.setCreator(ShiroKit.getUser().getName());
                    stock.setCreatetime(new Date());
                    stockService.save(stock);
                    //库存记录
                    stockService.addStockLog(otherin.getOtherinno(),warehouse.getId(),otherin.getLocatorid(),
                            otherin.getLocatorcode(), otherin.getLocatorname(),otherin.getCommodityid(),
                            otherin.getCommoditybar(),otherin.getCommodityname(), otherin.getQty(),Constants.StockLogType.INSTOCK);
                }
                result.put("code", 200);
                result.put("message", "审核成功");
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
        Otherin otherin = this.baseMapper.selectById(id);
        //判断其他入库信息是否存在
        if(otherin != null){
            //判断入库状态是否为已审核
            if(otherin.getState().equals(Constants.OtherinState.REVIEWED)){
                otherin.setState(Constants.OtherinState.UNREVIEW);
                this.baseMapper.updateById(otherin);
                Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",otherin.getLocatorid()).eq("commodityid",otherin.getCommodityid()));
                if (stock.getCqty()-otherin.getQty()==0){
                    stockService.removeById(stock.getId());
                    //修改库存日志状态
                    stockService.changeStockLogState(otherin.getOtherinno());
                } else {
                    stock.setCqty(stock.getCqty()-otherin.getQty());
                    stock.setUqty(stock.getUqty()-otherin.getQty());
                    stock.setUpdateid(ShiroKit.getUser().getId());
                    stock.setUpdator(ShiroKit.getUser().getName());
                    stock.setUpdatetime(new Date());
                    stockService.updateById(stock);
                    //修改库存日志状态
                    stockService.changeStockLogState(otherin.getOtherinno());
                }
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

    private Serializable getKey(OtherinParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Otherin getOldEntity(OtherinParam param) {
        return this.getById(getKey(param));
    }

    private Otherin getEntity(OtherinParam param) {
        Otherin entity = new Otherin();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
}
