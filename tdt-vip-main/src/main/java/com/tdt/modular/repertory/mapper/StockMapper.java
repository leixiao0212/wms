package com.tdt.modular.repertory.mapper;

import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.model.params.StockParam;
import com.tdt.modular.repertory.model.result.StockResult;
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
public interface StockMapper extends BaseMapper<Stock> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<StockResult> customList(@Param("paramCondition") StockParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page,@Param("paramCondition") StockParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    Page<StockResult> customPageList(@Param("page") Page page, @Param("paramCondition") StockParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") StockParam paramCondition);

    /**
     * 根据商品id和普通货位查询库存列表
     * @param commodityId
     * @return
     */
    List<Stock> queryCommonLocatorStock(@Param("commodityId") Long commodityId);

}
