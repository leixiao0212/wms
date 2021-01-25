package com.tdt.modular.outstore.mapper;

import com.tdt.modular.outstore.entity.PickDetail;
import com.tdt.modular.outstore.model.params.PickDetailParam;
import com.tdt.modular.outstore.model.result.PickDetailResult;
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
public interface PickDetailMapper extends BaseMapper<PickDetail> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-09-17
     */
    List<PickDetailResult> customList(@Param("paramCondition") PickDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-09-17
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page, @Param("paramCondition") PickDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-09-17
     */
    Page<PickDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") PickDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-09-17
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PickDetailParam paramCondition);

    /**
     * 根据拣货id查询具体货位上的商品对应的数量
     * @param pid
     * @return
     */
    List<Map<String, Object>> countCommodityQty(Long pid);
}
