package com.tdt.modular.outstore.mapper;

import com.tdt.modular.outstore.entity.PicktaskOutorder;
import com.tdt.modular.outstore.model.params.PicktaskOutorderParam;
import com.tdt.modular.outstore.model.result.PicktaskOutorderResult;
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
 * @since 2019-09-12
 */
public interface PicktaskOutorderMapper extends BaseMapper<PicktaskOutorder> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    List<PicktaskOutorderResult> customList(@Param("paramCondition") PicktaskOutorderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page,@Param("paramCondition") PicktaskOutorderParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    Page<PicktaskOutorderResult> customPageList(@Param("page") Page page, @Param("paramCondition") PicktaskOutorderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PicktaskOutorderParam paramCondition);

}
