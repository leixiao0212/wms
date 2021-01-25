package com.tdt.modular.outstore.mapper;

import com.tdt.modular.outstore.entity.OutorderDetail;
import com.tdt.modular.outstore.model.params.OutorderDetailParam;
import com.tdt.modular.outstore.model.result.OutorderDetailResult;
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
 * @since 2019-09-10
 */
public interface OutorderDetailMapper extends BaseMapper<OutorderDetail> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-09-10
     */
    List<OutorderDetailResult> customList(@Param("paramCondition") OutorderDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-09-10
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page,@Param("paramCondition") OutorderDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-09-10
     */
    Page<OutorderDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") OutorderDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-09-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OutorderDetailParam paramCondition);

    /**
     * 统计在出库订单号相同时相同的商品种类
     *
     * @author chenc
     * @Date 2019-09-10
     */
    int countCategory (Long id);

    /**
     * 统计根据生成拣货订单时选中的出库订单的id查询出的某一类商品的数量
     *
     * @author chenc
     * @Date 2019-09-10
     */
    List<Map<String, Object>> countCommodityQty(@Param("array") String[] _ids);
    List<Long> noenough(@Param("array") String[] _ids);
    /**
     * 根据商品id和出库订单明细id查询出库订单
     * @param commodityId
     * @return
     */
    List<OutorderDetail> selectListByCommodityIdAndOutorderId(@Param("commodityId") Long commodityId,@Param("array")String[] _ids);

}
