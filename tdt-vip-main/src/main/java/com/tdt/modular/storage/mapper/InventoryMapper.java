package com.tdt.modular.storage.mapper;

import com.tdt.modular.storage.entity.Inventory;
import com.tdt.modular.storage.model.params.InventoryParam;
import com.tdt.modular.storage.model.result.InventoryResult;
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
public interface InventoryMapper extends BaseMapper<Inventory> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<InventoryResult> customList(@Param("paramCondition") InventoryParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") InventoryParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    Page<InventoryResult> customPageList(@Param("page") Page page, @Param("paramCondition") InventoryParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") InventoryParam paramCondition);

    /**
     * 盘赢列表
     */
    List<Map<String, Object>> inventoryProfitList(@Param("page") Page page,@Param("pid") Long pid);

    /**
     * 盘亏列表
     */
    List<Map<String, Object>> inventoryLossList(@Param("page") Page page,@Param("pid") Long pid);
}
