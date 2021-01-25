package com.tdt.modular.storage.mapper;

import com.tdt.modular.storage.entity.Puton;
import com.tdt.modular.storage.model.params.PutonParam;
import com.tdt.modular.storage.model.result.PutonResult;
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
public interface PutonMapper extends BaseMapper<Puton> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<PutonResult> customList(@Param("paramCondition") PutonParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page,@Param("paramCondition") PutonParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    Page<PutonResult> customPageList(@Param("page") Page page, @Param("paramCondition") PutonParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PutonParam paramCondition);

}
