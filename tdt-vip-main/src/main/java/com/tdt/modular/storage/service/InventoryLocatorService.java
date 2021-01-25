package com.tdt.modular.storage.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.storage.entity.InventoryLocator;
import com.tdt.modular.storage.mapper.InventoryLocatorMapper;
import com.tdt.modular.storage.model.params.InventoryLocatorParam;
import com.tdt.modular.storage.model.result.InventoryLocatorResult;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 盘点货位管理服务类
 */
@Service
public class InventoryLocatorService extends ServiceImpl<InventoryLocatorMapper, InventoryLocator> {

    @Transactional(rollbackFor = Exception.class)
    public void add(InventoryLocatorParam param){
        InventoryLocator entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(InventoryLocatorParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(InventoryLocatorParam param){
        InventoryLocator oldEntity = getOldEntity(param);
        InventoryLocator newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, InventoryLocatorParam paramCondition) {
        Long warehouseid = ShiroKit.getUser().getWarehouseId();
        return this.baseMapper.customMapList(page, paramCondition,warehouseid);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> inventoryDetailLists(Page page, InventoryLocatorParam paramCondition) {
        return this.baseMapper.inventoryDetailLists(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> detailList(Page page, InventoryLocatorParam paramCondition) {
        return this.baseMapper.detailList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public InventoryLocatorResult findBySpec(InventoryLocatorParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<InventoryLocatorResult> findListBySpec(InventoryLocatorParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(InventoryLocatorParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(InventoryLocatorParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private InventoryLocator getOldEntity(InventoryLocatorParam param) {
        return this.getById(getKey(param));
    }

    private InventoryLocator getEntity(InventoryLocatorParam param) {
        InventoryLocator entity = new InventoryLocator();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
