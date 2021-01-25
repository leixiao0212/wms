package com.tdt.modular.base.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.node.ZTreeNode;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.mapper.WarehouseMapper;
import com.tdt.modular.base.model.params.WarehouseParam;
import com.tdt.modular.base.model.result.WarehouseResult;
import com.tdt.sys.core.constant.cache.Cache;
import com.tdt.sys.core.constant.cache.CacheKey;
import com.tdt.sys.core.shiro.ShiroKit;
import com.tdt.sys.modular.system.mapper.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 仓库管理服务类
 * @author
 */
@Service
public class WarehouseService extends ServiceImpl<WarehouseMapper, Warehouse> {

    @Resource
    private DeptMapper deptMapper;

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = Cache.CONSTANT, key = CacheKey.WAREHOUSE_NAME, allEntries=true)
    public void add(WarehouseParam param){
        Warehouse entity = getEntity(param);
        entity.setCreateid(ShiroKit.getUser().getId());
        entity.setCreator(ShiroKit.getUser().getName());
        entity.setCreatetime(new Date());
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = Cache.CONSTANT, key = CacheKey.WAREHOUSE_NAME, allEntries=true)
    public void delete(WarehouseParam param){
        if(this.baseMapper.selectCount(new QueryWrapper<Warehouse>())>1){
            this.removeById(getKey(param));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = Cache.CONSTANT, key = CacheKey.WAREHOUSE_NAME, allEntries=true)
    public void update(WarehouseParam param){
        Warehouse oldEntity = getOldEntity(param);
        Warehouse newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        newEntity.setUpdateid(ShiroKit.getUser().getId());
        newEntity.setUpdator(ShiroKit.getUser().getName());
        newEntity.setUpdatetime(new Date());
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page,WarehouseParam paramCondition) {
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public Warehouse getByName(String warehousename) {
        return this.baseMapper.getByName(warehousename);
    }

    @Transactional(rollbackFor = Exception.class)
    public WarehouseResult findBySpec(WarehouseParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<WarehouseResult> findListBySpec(WarehouseParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(WarehouseParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(WarehouseParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Warehouse getOldEntity(WarehouseParam param) {
        return this.getById(getKey(param));
    }

    private Warehouse getEntity(WarehouseParam param) {
        Warehouse entity = new Warehouse();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    /**
     * 根据角色获取菜单
     *
     * @param deptId
     * @return
     * @date 2017年2月19日 下午10:35:40
     */
    public List<ZTreeNode> warehouseTreeListByDeptIds(Long deptId) {
        List<Warehouse> warehouses = this.baseMapper.selectList(new QueryWrapper<>());
        List<Long> havewarehouses=deptMapper.queryWarehouses(deptId);
        List<ZTreeNode> zTreeNodes= new ArrayList<ZTreeNode>();
        //给所有的菜单url加上ctxPath
        for (Warehouse warehouse : warehouses) {
            ZTreeNode node=new ZTreeNode();
            node.setId(warehouse.getId());
            node.setName(warehouse.getName());
            node.setOpen(false);
            if(havewarehouses.remove(warehouse.getId())){
                node.setChecked(true);
            }else{
                node.setChecked(false);
            }
            node.setPId(0L);
            zTreeNodes.add(node);
        }
        return zTreeNodes;
    }
}
