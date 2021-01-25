package com.tdt.modular.instorage.mapper;

import com.tdt.modular.instorage.entity.Otherin;
import com.tdt.modular.instorage.model.params.OtherinParam;
import com.tdt.modular.instorage.model.result.OtherinResult;
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
 * @since 2019-08-21
 */
public interface OtherinMapper extends BaseMapper<Otherin> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    List<OtherinResult> customList(@Param("paramCondition") OtherinParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    List<Map<String, Object>> customMapList(@Param("page")Page page,@Param("paramCondition") OtherinParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    Page<OtherinResult> customPageList(@Param("page") Page page, @Param("paramCondition") OtherinParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OtherinParam paramCondition);

}
