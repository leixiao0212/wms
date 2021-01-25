package com.tdt.modular.instorage.mapper;

import com.tdt.modular.instorage.entity.Warehousing;
import com.tdt.modular.instorage.model.params.WarehousingParam;
import com.tdt.modular.instorage.model.result.WarehousingResult;
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
public interface WarehousingMapper extends BaseMapper<Warehousing> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    List<WarehousingResult> customList(@Param("paramCondition") WarehousingParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    List<Map<String, Object>> customMapList(@Param("page")Page page,@Param("paramCondition") WarehousingParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    Page<WarehousingResult> customPageList(@Param("page") Page page, @Param("paramCondition") WarehousingParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WarehousingParam paramCondition);

    /**
     * 根据id连表删除入库订单和入库明细
     * @param id
     */
    void deleteAll(Long id);
}
