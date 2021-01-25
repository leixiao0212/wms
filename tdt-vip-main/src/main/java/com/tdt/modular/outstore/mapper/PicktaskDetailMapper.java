package com.tdt.modular.outstore.mapper;

import com.tdt.modular.outstore.entity.PicktaskDetail;
import com.tdt.modular.outstore.model.params.PicktaskDetailParam;
import com.tdt.modular.outstore.model.result.PicktaskDetailResult;
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
public interface PicktaskDetailMapper extends BaseMapper<PicktaskDetail> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    List<PicktaskDetailResult> customList(@Param("paramCondition") PicktaskDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page,@Param("paramCondition") PicktaskDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    Page<PicktaskDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") PicktaskDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PicktaskDetailParam paramCondition);

}
