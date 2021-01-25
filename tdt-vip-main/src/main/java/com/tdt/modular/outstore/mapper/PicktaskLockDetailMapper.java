package com.tdt.modular.outstore.mapper;

import com.tdt.modular.outstore.entity.PicktaskLockDetail;
import com.tdt.modular.outstore.model.params.PicktaskLockDetailParam;
import com.tdt.modular.outstore.model.result.PicktaskLockDetailResult;
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
 * @since 2019-09-26
 */
public interface PicktaskLockDetailMapper extends BaseMapper<PicktaskLockDetail> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-09-26
     */
    List<PicktaskLockDetailResult> customList(@Param("paramCondition") PicktaskLockDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-09-26
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") PicktaskLockDetailParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-09-26
     */
    Page<PicktaskLockDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") PicktaskLockDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-09-26
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") PicktaskLockDetailParam paramCondition);

    /**
     * 查询相同的出库订单明细的数量
     * @param outorderdetailid
     * @return
     */
    int countQty(Long outorderdetailid);

    /**
     * 查询在一个拣货任务下的商品和货位相同情况下对应的数量
     * @param picktaskno
     * @return
     */
    List<Map<String, Object>> countCommodityQty(String picktaskno);

    int updateoutorderno();
}
