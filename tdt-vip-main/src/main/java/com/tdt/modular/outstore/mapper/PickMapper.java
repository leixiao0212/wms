package com.tdt.modular.outstore.mapper;

import com.tdt.modular.outstore.entity.Pick;
import com.tdt.modular.outstore.model.params.PickParam;
import com.tdt.modular.outstore.model.result.PickResult;
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
 * @since 2019-09-17
 */
public interface PickMapper extends BaseMapper<Pick> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-09-17
     */
    List<PickResult> customList(@Param("paramCondition") PickParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-09-17
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page,@Param("paramCondition") PickParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-09-17
     */
    Page<PickResult> customPageList(@Param("page") Page page, @Param("paramCondition") PickParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-09-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PickParam paramCondition);

}
