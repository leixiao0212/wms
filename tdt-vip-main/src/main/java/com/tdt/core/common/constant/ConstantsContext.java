package com.tdt.core.common.constant;

import cn.hutool.core.lang.Singleton;
import cn.stylefeng.roses.core.util.SpringContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tdt.modular.base.entity.Supplier;
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.mapper.SupplierMapper;
import com.tdt.modular.base.mapper.WarehouseMapper;
import com.tdt.sys.core.constant.cache.Cache;
import com.tdt.sys.core.constant.cache.CacheKey;
import com.tdt.sys.core.constant.factory.IConstantFactory;
import com.tdt.sys.modular.system.mapper.RoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ConstantsContext {
    private static volatile ConstantsContext me = null;
    ConstantsContext() {
    }
    public static ConstantsContext me() {
        if (me == null) {
            synchronized(Singleton.class) {
                if (me == null) {
                    me = new ConstantsContext();
                }
            }
        }
        return me;
    }
    private WarehouseMapper warehouseMapper = SpringContextHolder.getBean(WarehouseMapper.class);
    private SupplierMapper supplierMapper = SpringContextHolder.getBean(SupplierMapper.class);
    @Cacheable(value = Cache.CONSTANT, key = CacheKey.SUPPLIER_NAME)
    public Map getSuppliers() {
        Map map=new HashMap();
        List<Supplier> supplisers=this.supplierMapper.selectList(new QueryWrapper<Supplier>());
        if(supplisers!=null){
            for(int i=0;i<supplisers.size();i++){
                Supplier suppliser=supplisers.get(i);
                map.put(suppliser.getId(),suppliser.getName());
            }
        }
        return map;
    }

    @Cacheable(value = Cache.CONSTANT, key = CacheKey.WAREHOUSE_NAME)
    public Map getWarehouses() {
        Map map=new HashMap();
        List<Warehouse> warehouses=this.warehouseMapper.selectList(new QueryWrapper<Warehouse>());
        if(warehouses!=null){
            for(int i=0;i<warehouses.size();i++){
                Warehouse warehouse=warehouses.get(i);
                map.put(warehouse.getId(),warehouse.getName());
            }
        }
        return map;
    }
}
