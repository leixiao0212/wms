package com.tdt.modular.base.mapper;

import com.tdt.modular.base.entity.Supplier;
import com.tdt.modular.base.model.params.SupplierParam;
import com.tdt.modular.base.model.result.SupplierResult;
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
 * @since 2019-08-19
 */
public interface SupplierMapper extends BaseMapper<Supplier> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    List<SupplierResult> customList(@Param("paramCondition") SupplierParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page, @Param("paramCondition") SupplierParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    Page<SupplierResult> customPageList(@Param("page") Page page, @Param("paramCondition") SupplierParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") SupplierParam paramCondition);

}
