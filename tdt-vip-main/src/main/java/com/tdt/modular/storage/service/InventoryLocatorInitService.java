package com.tdt.modular.storage.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.storage.entity.InventoryLocatorInit;
import com.tdt.modular.storage.mapper.InventoryLocatorInitMapper;
import com.tdt.modular.storage.model.params.InventoryLocatorInitParam;
import com.tdt.modular.storage.model.result.InventoryLocatorInitResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 盘点初始管理服务类
 */
@Service
public class InventoryLocatorInitService extends ServiceImpl<InventoryLocatorInitMapper, InventoryLocatorInit> {

    @Transactional(rollbackFor = Exception.class)
    public void add(InventoryLocatorInitParam param){
        InventoryLocatorInit entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(InventoryLocatorInitParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(InventoryLocatorInitParam param){
        InventoryLocatorInit oldEntity = getOldEntity(param);
        InventoryLocatorInit newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public InventoryLocatorInitResult findBySpec(InventoryLocatorInitParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<InventoryLocatorInitResult> findListBySpec(InventoryLocatorInitParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(InventoryLocatorInitParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(InventoryLocatorInitParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private InventoryLocatorInit getOldEntity(InventoryLocatorInitParam param) {
        return this.getById(getKey(param));
    }

    private InventoryLocatorInit getEntity(InventoryLocatorInitParam param) {
        InventoryLocatorInit entity = new InventoryLocatorInit();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
