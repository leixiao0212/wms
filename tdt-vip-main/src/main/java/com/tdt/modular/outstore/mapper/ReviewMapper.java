package com.tdt.modular.outstore.mapper;

import com.tdt.modular.outstore.entity.Review;
import com.tdt.modular.outstore.model.params.ReviewParam;
import com.tdt.modular.outstore.model.result.ReviewResult;
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
 * @since 2019-09-18
 */
public interface ReviewMapper extends BaseMapper<Review> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-09-18
     */
    List<ReviewResult> customList(@Param("paramCondition") ReviewParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-09-18
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page,@Param("paramCondition") ReviewParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-09-18
     */
    Page<ReviewResult> customPageList(@Param("page") Page page, @Param("paramCondition") ReviewParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-09-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") ReviewParam paramCondition);

}
