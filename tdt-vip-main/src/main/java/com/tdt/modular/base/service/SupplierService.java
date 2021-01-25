package com.tdt.modular.base.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.base.entity.Supplier;
import com.tdt.modular.base.mapper.SupplierMapper;
import com.tdt.modular.base.model.params.SupplierParam;
import com.tdt.modular.base.model.result.SupplierResult;
import com.tdt.sys.core.constant.cache.Cache;
import com.tdt.sys.core.constant.cache.CacheKey;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 供应商管理服务类
 * @author
 */
@Service
public class SupplierService extends ServiceImpl<SupplierMapper, Supplier> {

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = Cache.CONSTANT, key = CacheKey.WAREHOUSE_NAME, allEntries=true)
    public void add(SupplierParam param){
        Supplier entity = getEntity(param);
        entity.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        entity.setCreateid(ShiroKit.getUser().getId());
        entity.setCreator(ShiroKit.getUser().getName());
        entity.setCreatetime(new Date());
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = Cache.CONSTANT, key = CacheKey.WAREHOUSE_NAME, allEntries=true)
    public void delete(SupplierParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = Cache.CONSTANT, key = CacheKey.WAREHOUSE_NAME, allEntries=true)
    public void update(SupplierParam param){
        Supplier oldEntity = getOldEntity(param);
        Supplier newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        newEntity.setUpdateid(ShiroKit.getUser().getId());
        newEntity.setUpdator(ShiroKit.getUser().getName());
        newEntity.setUpdatetime(new Date());
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page,SupplierParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public SupplierResult findBySpec(SupplierParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<SupplierResult> findListBySpec(SupplierParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(SupplierParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(SupplierParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Supplier getOldEntity(SupplierParam param) {
        return this.getById(getKey(param));
    }

    private Supplier getEntity(SupplierParam param) {
        Supplier entity = new Supplier();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
}
