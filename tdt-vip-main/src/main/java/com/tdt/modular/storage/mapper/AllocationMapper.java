package com.tdt.modular.storage.mapper;

import com.tdt.modular.storage.entity.Allocation;
import com.tdt.modular.storage.model.params.AllocationParam;
import com.tdt.modular.storage.model.result.AllocationResult;
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
public interface AllocationMapper extends BaseMapper<Allocation> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<AllocationResult> customList(@Param("paramCondition") AllocationParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page,@Param("paramCondition") AllocationParam paramCondition,@Param("warehouseid") Long warehouseid);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    Page<AllocationResult> customPageList(@Param("page") Page page, @Param("paramCondition") AllocationParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") AllocationParam paramCondition);

    /**
     * 根据id连表删除调拨订单和接调拨明细
     * @param id
     */
    void deleteAll(Long id);

}
