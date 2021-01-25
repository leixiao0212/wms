package com.tdt.modular.outstore.mapper;

import com.tdt.modular.outstore.entity.Outorder;
import com.tdt.modular.outstore.entity.OutorderTag;
import com.tdt.modular.outstore.model.params.OutorderParam;
import com.tdt.modular.outstore.model.result.OutorderResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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
public interface OutorderMapper extends BaseMapper<Outorder> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-09-10
     */
    List<OutorderResult> customList(@Param("paramCondition") OutorderParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-09-10
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page, @Param("paramCondition") OutorderParam paramCondition, @Param("stime") Date stime, @Param("etime")Date etime);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-09-10
     */
    Page<OutorderResult> customPageList(@Param("page") Page page, @Param("paramCondition") OutorderParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-09-10
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OutorderParam paramCondition);

    /**
     * 级联删除出库订单和出库明细
     *
     * @author chenc
     * @Date 2019-09-10
     */
    void deleteAll(Long id);

    /**
     * 根据省市区，出库订单标签查询出库订单列表
     * @param param
     * @return
     */
    List<Long> pcctOutorderList(@Param("param") Map<String,String> param,@Param("start") int start,@Param("qty") int qty);

    /**
     * 查询状态为已审核的且未生成标签的出库订单列表
     * @param warehouseid
     * @return
     */
    List<Outorder> notCreateOutorderTaglist(@Param("warehouseid") Long warehouseid);
}
