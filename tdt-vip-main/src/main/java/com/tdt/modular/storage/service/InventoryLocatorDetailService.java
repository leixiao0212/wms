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
import com.tdt.modular.base.entity.Commodity;
import com.tdt.modular.base.service.CommodityService;
import com.tdt.modular.storage.entity.Inventory;
import com.tdt.modular.storage.entity.InventoryLocator;
import com.tdt.modular.storage.entity.InventoryLocatorDetail;
import com.tdt.modular.storage.mapper.InventoryLocatorDetailMapper;
import com.tdt.modular.storage.model.params.InventoryLocatorDetailParam;
import com.tdt.modular.storage.model.result.InventoryLocatorDetailResult;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 盘点货位详情管理服务类
 */
@Service
public class InventoryLocatorDetailService extends ServiceImpl<InventoryLocatorDetailMapper, InventoryLocatorDetail> {

    @Autowired
    private InventoryLocatorService inventoryLocatorService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackFor = Exception.class)
    public void add(InventoryLocatorDetailParam param){
        InventoryLocatorDetail entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addDetail(InventoryLocatorDetail inventoryLocatorDetail, String inventoryno) {
        JSONObject result = new JSONObject();
        InventoryLocator inventoryLocator = inventoryLocatorService.getOne(new QueryWrapper<InventoryLocator>().eq("inventoryno", inventoryno).eq("locatorcode", inventoryLocatorDetail.getLocatorcode()));
        //判断盘点的货位是否有记录
        if (inventoryLocator != null) {
            Commodity commodity = commodityService.getOne(new QueryWrapper<Commodity>().eq("bar", inventoryLocatorDetail.getCommoditybar()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
            //判断商品是否存在且商品编码属于当前仓库
            if (commodity != null) {
                InventoryLocatorDetail inventoryLocatorDetail1 = this.getOne(new QueryWrapper<InventoryLocatorDetail>().eq("locatorcode",inventoryLocatorDetail.getLocatorcode()).eq("commoditybar",inventoryLocatorDetail.getCommoditybar()));
                //判断盘点明细中是否存在货位编码和商品编码相同的明细
                if (inventoryLocatorDetail1!=null){
                    inventoryLocatorDetail1.setQty(inventoryLocatorDetail1.getQty()+inventoryLocatorDetail.getQty());
                    this.updateById(inventoryLocatorDetail1);
                } else {
                    inventoryLocatorDetail.setPid(inventoryLocator.getId());
                    inventoryLocatorDetail.setLocatorid(inventoryLocator.getId());
                    inventoryLocatorDetail.setLocatorname(inventoryLocator.getLocatorname());
                    inventoryLocatorDetail.setCommodityid(commodity.getId());
                    inventoryLocatorDetail.setCommodityname(commodity.getName());
                    inventoryLocatorDetail.setCreateid(ShiroKit.getUser().getId());
                    inventoryLocatorDetail.setCreator(ShiroKit.getUser().getName());
                    inventoryLocatorDetail.setCreatetime(new Date());
                    this.baseMapper.insert(inventoryLocatorDetail);
                    Inventory inventory = inventoryService.getOne(new QueryWrapper<Inventory>().eq("inventoryno",inventoryno).eq("state",Constants.InventoryState.INIT));
                    if (inventory!=null){
                        inventory.setState(Constants.InventoryState.START);
                        inventoryService.updateById(inventory);
                    }
                }
            } else {
                result.put("code", 500);
                result.put("message", "商品编码不存在或商品编码不属于当前仓库");
                return result;
            }
        } else {
            result.put("code", 500);
            result.put("message", "盘点货位不存在");
            return result;
        }
        result.put("code", 200);
        result.put("message", "新增盘点明细成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(InventoryLocatorDetailParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(InventoryLocatorDetailParam param){
        InventoryLocatorDetail oldEntity = getOldEntity(param);
        InventoryLocatorDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, InventoryLocatorDetailParam paramCondition) {
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public InventoryLocatorDetailResult findBySpec(InventoryLocatorDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<InventoryLocatorDetailResult> findListBySpec(InventoryLocatorDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(InventoryLocatorDetailParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(InventoryLocatorDetailParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private InventoryLocatorDetail getOldEntity(InventoryLocatorDetailParam param) {
        return this.getById(getKey(param));
    }

    private InventoryLocatorDetail getEntity(InventoryLocatorDetailParam param) {
        InventoryLocatorDetail entity = new InventoryLocatorDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
