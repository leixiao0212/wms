package com.tdt.modular.outstore.mapper;

import com.tdt.modular.outstore.entity.Picktask;
import com.tdt.modular.outstore.model.params.PicktaskParam;
import com.tdt.modular.outstore.model.result.PicktaskResult;
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
public interface PicktaskMapper extends BaseMapper<Picktask> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    List<PicktaskResult> customList(@Param("paramCondition") PicktaskParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page, @Param("paramCondition") PicktaskParam paramCondition,@Param("outorderno") String outorderno,@Param("isReceive") String isReceive);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    Page<PicktaskResult> customPageList(@Param("page") Page page, @Param("paramCondition") PicktaskParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PicktaskParam paramCondition);

    /**
     *
     * @param picktaskid
     */
    void updateSetNull(Long picktaskid);

}
