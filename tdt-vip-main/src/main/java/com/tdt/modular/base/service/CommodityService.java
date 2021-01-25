package com.tdt.modular.base.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.base.entity.Commodity;
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.mapper.CommodityMapper;
import com.tdt.modular.base.model.params.CommodityParam;
import com.tdt.modular.base.model.result.CommodityResult;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品管理服务类
 * @author
 */
@Service
public class CommodityService extends ServiceImpl<CommodityMapper, Commodity> {

    @Autowired
    private WarehouseService warehouseService;

    @Transactional(rollbackFor = Exception.class)
    public void add(CommodityParam param){
        Commodity entity = getEntity(param);
        if (param.getWarehouseid()!=null){
            Warehouse warehouse = warehouseService.getById(param.getWarehouseid());
            entity.setWarehousename(warehouse.getName());
        }
        entity.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        entity.setCreateid(ShiroKit.getUser().getId());
        entity.setCreator(ShiroKit.getUser().getName());
        entity.setCreatime(new Date());
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(CommodityParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(CommodityParam param){
        Commodity oldEntity = getOldEntity(param);
        Commodity newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        newEntity.setWarehouseid(oldEntity.getWarehouseid());
        Warehouse warehouse = warehouseService.getById(newEntity.getWarehouseid());
        newEntity.setWarehousename(warehouse.getName());
        newEntity.setUpdateid(ShiroKit.getUser().getId());
        newEntity.setUpdator(ShiroKit.getUser().getName());
        newEntity.setUpdatetime(new Date());
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page,CommodityParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public CommodityResult findBySpec(CommodityParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<CommodityResult> findListBySpec(CommodityParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(CommodityParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    @Transactional(rollbackFor = Exception.class)
    public Commodity queryByBar(String bar) {
        return this.baseMapper.queryByBar(bar);
    }

    private Serializable getKey(CommodityParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Commodity getOldEntity(CommodityParam param) {
        return this.getById(getKey(param));
    }

    private Commodity getEntity(CommodityParam param) {
        Commodity entity = new Commodity();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
}
