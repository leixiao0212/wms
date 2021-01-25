package com.tdt.modular.instorage.mapper;

import com.tdt.modular.instorage.entity.WarehousingDetail;
import com.tdt.modular.instorage.model.params.WarehousingDetailParam;
import com.tdt.modular.instorage.model.result.WarehousingDetailResult;
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
public interface WarehousingDetailMapper extends BaseMapper<WarehousingDetail> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    List<WarehousingDetailResult> customList(@Param("paramCondition") WarehousingDetailParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    List<Map<String, Object>> customMapList(@Param("page") Page page,@Param("paramCondition") WarehousingDetailParam paramCondition);

    /**
     * 根据入库id获取接货订单详情map列表
     * @param page
     * @param pid
     * @return
     */
    List<Map<String, Object>> selectByPid(@Param("page") Page page,@Param("pid") Long pid);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    Page<WarehousingDetailResult> customPageList(@Param("page") Page page, @Param("paramCondition") WarehousingDetailParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") WarehousingDetailParam paramCondition);

}
