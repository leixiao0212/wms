package com.tdt.modular.outstore.mapper;

import com.tdt.modular.outstore.entity.Otherout;
import com.tdt.modular.outstore.model.params.OtheroutParam;
import com.tdt.modular.outstore.model.result.OtheroutResult;
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
 * @since 2019-09-19
 */
public interface OtheroutMapper extends BaseMapper<Otherout> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-09-19
     */
    List<OtheroutResult> customList(@Param("paramCondition") OtheroutParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-09-19
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page, @Param("paramCondition") OtheroutParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-09-19
     */
    Page<OtheroutResult> customPageList(@Param("page") Page page, @Param("paramCondition") OtheroutParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-09-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OtheroutParam paramCondition);

}
