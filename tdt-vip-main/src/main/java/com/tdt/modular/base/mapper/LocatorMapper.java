package com.tdt.modular.base.mapper;

import com.tdt.modular.base.entity.Locator;
import com.tdt.modular.base.model.params.LocatorParam;
import com.tdt.modular.base.model.result.LocatorResult;
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
public interface LocatorMapper extends BaseMapper<Locator> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    List<LocatorResult> customList(@Param("paramCondition") LocatorParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page, @Param("paramCondition") LocatorParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    Page<LocatorResult> customPageList(@Param("page") Page page, @Param("paramCondition") LocatorParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") LocatorParam paramCondition);

}
