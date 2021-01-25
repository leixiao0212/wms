package com.tdt.modular.base.mapper;

import com.tdt.modular.base.entity.Commodity;
import com.tdt.modular.base.model.params.CommodityParam;
import com.tdt.modular.base.model.result.CommodityResult;
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
 * @since 2019-08-16
 */
public interface CommodityMapper extends BaseMapper<Commodity> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-16
     */
    List<CommodityResult> customList(@Param("paramCondition") CommodityParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-16
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page, @Param("paramCondition") CommodityParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-16
     */
    Page<CommodityResult> customPageList(@Param("page") Page page, @Param("paramCondition") CommodityParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-16
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") CommodityParam paramCondition);

    /**
     * 根据条码查询商品信息
     * @param bar
     * @return
     */
    Commodity queryByBar(String bar);
}
