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
import com.tdt.modular.base.entity.Locator;
import com.tdt.modular.base.service.LocatorService;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.service.StockService;
import com.tdt.modular.storage.entity.Inventory;
import com.tdt.modular.storage.entity.InventoryLocator;
import com.tdt.modular.storage.entity.InventoryLocatorDetail;
import com.tdt.modular.storage.entity.InventoryLocatorInit;
import com.tdt.modular.storage.mapper.InventoryMapper;
import com.tdt.modular.storage.model.params.InventoryParam;
import com.tdt.modular.storage.model.result.InventoryResult;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 盘点管理服务类
 */
@Service
public class InventoryService extends ServiceImpl<InventoryMapper, Inventory> {

    @Autowired
    private LocatorService locatorService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryLocatorService inventoryLocatorService;

    @Autowired
    private StockService stockService;

    @Autowired
    private InventoryLocatorInitService inventoryLocatorInitService;

    @Autowired
    private InventoryLocatorDetailService inventoryLocatorDetailService;

    @Transactional(rollbackFor = Exception.class)
    public void add(InventoryParam param){
        Inventory entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addInventory(String locatorcodes) {
        JSONObject result = new JSONObject();
        String[] _locatorcodes = locatorcodes.split(",");
        Inventory inventory = new Inventory();
        if (_locatorcodes.length > 0) {
            String inventoryno = SequenceBuilder.generateNo(Constants.PREFIX_NO.INVENTORY_NO);
            inventory.setInventoryno(inventoryno);
            inventory.setState(Constants.InventoryState.INIT);
            inventory.setWarehouseid(ShiroKit.getUser().getWarehouseId());
            inventory.setCreateid(ShiroKit.getUser().getId());
            inventory.setCreator(ShiroKit.getUser().getName());
            inventory.setCreatetime(new Date());
            if (this.baseMapper.insert(inventory)>0) {
                for (int i = 0; i < _locatorcodes.length; i++) {
                    Locator locator = this.locatorService.getOne(new QueryWrapper<Locator>().eq("code", _locatorcodes[i]));
                    if (locator != null) {
                        InventoryLocator inventoryLocator = new InventoryLocator();
                        inventoryLocator.setPid(inventory.getId());
                        inventoryLocator.setInventoryno(inventoryno);
                        inventoryLocator.setLocatorid(locator.getId());
                        inventoryLocator.setLocatorname(locator.getName());
                        inventoryLocator.setLocatorcode(locator.getCode());
                        inventoryLocator.setCreatetime(new Date());
                        inventoryLocator.setCreateid(ShiroKit.getUser().getId());
                        inventoryLocator.setCreator(ShiroKit.getUser().getName());
                        inventoryLocatorService.save(inventoryLocator);
                        List<Stock> stocks = this.stockService.list(new QueryWrapper<Stock>().eq("locatorcode", locator.getCode()));
                        for (Stock stock : stocks) {
                            if (stock.getLocatorstate().equals(Constants.LocatorState.NORMAL)) {
                                InventoryLocatorInit inventoryLocatorInit = new InventoryLocatorInit();
                                inventoryLocatorInit.setPid(inventoryLocator.getId());
                                inventoryLocatorInit.setQty(stock.getCqty());
                                inventoryLocatorInit.setLocatorid(locator.getId());
                                inventoryLocatorInit.setLocatorname(locator.getName());
                                inventoryLocatorInit.setLocatorcode(locator.getCode());
                                inventoryLocatorInit.setCommodityid(stock.getCommodityid());
                                inventoryLocatorInit.setCommoditybar(stock.getCommoditybar());
                                inventoryLocatorInit.setCommodityname(stock.getCommodityname());
                                inventoryLocatorInit.setCreatetime(new Date());
                                inventoryLocatorInit.setCreateid(ShiroKit.getUser().getId());
                                inventoryLocatorInit.setCreator(ShiroKit.getUser().getName());
                                inventoryLocatorInitService.save(inventoryLocatorInit);
                            } else if (stock.getLocatorstate().equals(Constants.LocatorState.LOCK)){
                                result.put("code", 500);
                                result.put("message", "此[" + stock.getLocatorcode() + "]状态为锁定状态,不能盘点");
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                return result;
                            } else if (stock.getLocatorstate().equals(Constants.LocatorState.INVALID)){
                                result.put("code", 500);
                                result.put("message", "此[" + stock.getLocatorcode() + "]状态为失效状态,不能盘点");
                                return result;
                            }
                        }
                        //盘点时需要将货位锁定
                        locator.setState(Constants.LocatorState.LOCK);
                        locatorService.updateById(locator);
                    } else {
                        result.put("code", 500);
                        result.put("message", "此[" + _locatorcodes[i] + "]货位不存在");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return result;
                    }
                }
            } else {
                result.put("code", 500);
                result.put("message", "系统异常");
                return result;
            }
        } else {
            result.put("code", 500);
            result.put("message", "盘点货位为空");
            return result;
        }
        result.put("code", 200);
        result.put("message", "操作成功");
        result.put("inventory", inventory);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addAll() {
        JSONObject result = new JSONObject();
        List<Locator> locators = this.locatorService.list(new QueryWrapper<Locator>().eq("state", Constants.LocatorState.NORMAL));
        Inventory inventory = new Inventory();
        String inventoryno = SequenceBuilder.generateNo(Constants.PREFIX_NO.INVENTORY_NO);
        inventory.setInventoryno(inventoryno);
        inventory.setState(Constants.InventoryState.INIT);
        inventory.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        inventory.setCreateid(ShiroKit.getUser().getId());
        inventory.setCreator(ShiroKit.getUser().getName());
        inventory.setCreatetime(new Date());
        List<InventoryLocator> inventoryLocators =new ArrayList<InventoryLocator>();
        if (this.save(inventory)) {
            for (Locator locator : locators) {
                InventoryLocator inventoryLocator = new InventoryLocator();
                inventoryLocator.setPid(inventory.getId());
                inventoryLocator.setInventoryno(inventoryno);
                inventoryLocator.setLocatorid(locator.getId());
                inventoryLocator.setLocatorname(locator.getName());
                inventoryLocator.setLocatorcode(locator.getCode());
                inventoryLocator.setCreateid(ShiroKit.getUser().getId());
                inventoryLocator.setCreator(ShiroKit.getUser().getName());
                inventoryLocator.setCreatetime(new Date());
                inventoryLocators.add(inventoryLocator);
                List<Stock> stocks = stockService.list(new QueryWrapper<Stock>().eq("locatorcode", locator.getCode()));
                List<InventoryLocatorInit> inventoryLocatorInits =new ArrayList<InventoryLocatorInit>();
                for (Stock stock : stocks) {
                    if (stock.getLocatorstate().equals(Constants.LocatorState.NORMAL)) {
                        InventoryLocatorInit inventoryLocatorInit = new InventoryLocatorInit();
                        inventoryLocatorInit.setPid(inventoryLocator.getId());
                        inventoryLocatorInit.setQty(stock.getCqty());
                        inventoryLocatorInit.setLocatorid(locator.getId());
                        inventoryLocatorInit.setLocatorname(locator.getName());
                        inventoryLocatorInit.setLocatorcode(locator.getCode());
                        inventoryLocatorInit.setCommodityid(stock.getCommodityid());
                        inventoryLocatorInit.setCommoditybar(stock.getCommoditybar());
                        inventoryLocatorInit.setCommodityname(stock.getCommodityname());
                        inventoryLocatorInit.setCreatetime(new Date());
                        inventoryLocatorInit.setCreateid(ShiroKit.getUser().getId());
                        inventoryLocatorInit.setCreator(ShiroKit.getUser().getName());
                        inventoryLocatorInits.add(inventoryLocatorInit);
                    } else if (stock.getLocatorstate().equals(Constants.LocatorState.LOCK)) {
                        result.put("code", 500);
                        result.put("message", "货位编码为[" + stock.getLocatorcode() + "]的状态为锁定状态,不能盘点");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return result;
                    } else if (stock.getLocatorstate().equals(Constants.LocatorState.INVALID)) {
                        result.put("code", 500);
                        result.put("message", "货位编码为[" + stock.getLocatorcode() + "]的状态为失效状态,不能盘点");
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return result;
                    }
                }
                inventoryLocatorInitService.saveBatch(inventoryLocatorInits);
            }
            inventoryLocatorService.saveBatch(inventoryLocators);
        } else {
            result.put("code", 500);
            result.put("message", "系统异常");
            return result;
        }
        result.put("code", 200);
        result.put("message", "盘点成功");
        result.put("inventory", inventory);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(InventoryParam param){
        //将盘点的货位状态设置为正常
        List<InventoryLocator> inventoryLocatorList = inventoryLocatorService.list(new QueryWrapper<InventoryLocator>().eq("pid",param.getId()));
        List<Long> ids=new ArrayList<Long>();
        List<Long> pids=new ArrayList<Long>();
        List<Locator> locators=new ArrayList<Locator>();
        for (InventoryLocator inventoryLocator:inventoryLocatorList){
            Locator locator = locatorService.getById(inventoryLocator.getLocatorid());
            locator.setState(Constants.LocatorState.NORMAL);
            locators.add(locator);
            //删除四个表中相关数据
            ids.add(inventoryLocator.getId());
            pids.add(inventoryLocator.getPid());
        }
        if(locators.size()>0){
            locatorService.updateBatchById(locators);
        }
        if(pids.size()>0){
            inventoryService.removeByIds(pids);
            inventoryLocatorInitService.remove(new QueryWrapper<InventoryLocatorInit>().in("pid",ids));
            inventoryLocatorDetailService.remove(new QueryWrapper<InventoryLocatorDetail>().in("pid",ids));
            inventoryLocatorService.removeByIds(ids);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(InventoryParam param){
        Inventory oldEntity = getOldEntity(param);
        Inventory newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject endInventory(int inventoryId) {
        JSONObject result = new JSONObject();
        Inventory inventory = this.getById(inventoryId);
        //判断盘点的记录是否存在
        if (inventory != null) {
            inventory.setState(Constants.InventoryState.END);
            if (this.updateById(inventory)) {
                //将库存中的库位状态更新为正常状态
                List<InventoryLocator> inventoryLocators = inventoryLocatorService.list(new QueryWrapper<InventoryLocator>().eq("pid", inventoryId));
                inventoryLocators.forEach(inventoryLocator -> {
                    Locator locator = locatorService.getById(inventoryLocator.getLocatorid());
                    locator.setState(Constants.LocatorState.NORMAL);
                    locatorService.updateById(locator);
                });
            } else {
                result.put("code", 500);
                result.put("message", "系统错误");
            }
        } else {
            result.put("code", 500);
            result.put("message", "系统错误");
            return result;
        }
        result.put("code", 200);
        result.put("message", "盘点已结束");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> inventoryProfitList(Page page, Long pid) {
        return this.baseMapper.inventoryProfitList(page, pid);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> inventoryLossList(Page page, Long pid) {
        return this.baseMapper.inventoryLossList(page, pid);
    }

    @Transactional(rollbackFor = Exception.class)
    public InventoryResult findBySpec(InventoryParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<InventoryResult> findListBySpec(InventoryParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(InventoryParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(InventoryParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Inventory getOldEntity(InventoryParam param) {
        return this.getById(getKey(param));
    }

    private Inventory getEntity(InventoryParam param) {
        Inventory entity = new Inventory();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
