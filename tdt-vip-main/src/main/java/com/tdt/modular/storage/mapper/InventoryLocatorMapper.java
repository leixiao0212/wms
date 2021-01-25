package com.tdt.modular.storage.mapper;

import com.tdt.modular.storage.entity.InventoryLocator;
import com.tdt.modular.storage.model.params.InventoryLocatorParam;
import com.tdt.modular.storage.model.result.InventoryLocatorResult;
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
 * @since 2019-08-28
 */
public interface InventoryLocatorMapper extends BaseMapper<InventoryLocator> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<InventoryLocatorResult> customList(@Param("paramCondition") InventoryLocatorParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page,@Param("paramCondition") InventoryLocatorParam paramCondition,@Param("warehouseid") Long warehouseid);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<Map<String, Object>> detailList(@Param("page") Page page,@Param("paramCondition") InventoryLocatorParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    Page<InventoryLocatorResult> customPageList(@Param("page") Page page, @Param("paramCondition") InventoryLocatorParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InventoryLocatorParam paramCondition);

    /**
     * 盘点明细列表
     */
    List<Map<String, Object>> inventoryDetailLists(@Param("page") Page page,@Param("paramCondition") InventoryLocatorParam paramCondition);
}
