package com.tdt.modular.storage.mapper;

import com.tdt.modular.storage.entity.Move;
import com.tdt.modular.storage.model.params.MoveParam;
import com.tdt.modular.storage.model.result.MoveResult;
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
public interface MoveMapper extends BaseMapper<Move> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<MoveResult> customList(@Param("paramCondition") MoveParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page,@Param("paramCondition") MoveParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    Page<MoveResult> customPageList(@Param("page") Page page, @Param("paramCondition") MoveParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") MoveParam paramCondition);

}
