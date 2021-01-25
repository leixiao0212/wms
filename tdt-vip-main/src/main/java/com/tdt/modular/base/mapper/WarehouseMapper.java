package com.tdt.modular.base.mapper;

import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.model.params.WarehouseParam;
import com.tdt.modular.base.model.result.WarehouseResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenc
 * @since 2019-08-19
 */
public interface WarehouseMapper extends BaseMapper<Warehouse> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    List<WarehouseResult> customList(@Param("paramCondition") WarehouseParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page, @Param("paramCondition") WarehouseParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    Page<WarehouseResult> customPageList(@Param("page") Page page, @Param("paramCondition") WarehouseParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WarehouseParam paramCondition);

    /**
     * 根据仓库名查询仓库信息
     * @param warehousename
     * @return
     */
    Warehouse getByName(String warehousename);
    /**
     * 根据仓库名查询仓库信息
     * @param deptId
     * @return
     */
    List<Warehouse> warehouseTreeListByDeptId(@Param("deptId") Long deptId);

}
