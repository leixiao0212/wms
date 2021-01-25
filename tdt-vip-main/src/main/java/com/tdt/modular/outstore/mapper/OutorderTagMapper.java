package com.tdt.modular.outstore.mapper;

import com.tdt.modular.outstore.entity.OutorderTag;
import com.tdt.modular.outstore.model.params.OutorderTagParam;
import com.tdt.modular.outstore.model.result.OutorderTagResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * @since 2019-10-11
 */
public interface OutorderTagMapper extends BaseMapper<OutorderTag> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-10-11
     */
    List<OutorderTagResult> customList(@Param("paramCondition") OutorderTagParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-10-11
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") OutorderTagParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-10-11
     */
    Page<OutorderTagResult> customPageList(@Param("page") Page page, @Param("paramCondition") OutorderTagParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-10-11
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") OutorderTagParam paramCondition);

    /**
     * 查询当前仓库符合条件的记录
     * @param warehouseid
     * @param type
     * @return
     */
    int count(Long warehouseid,String type);

    /**
     * 根据条件查询数量
     * @param warehouseid
     * @param type
     * @param sql
     * @param qty
     * @return
     */
    List<Map<String, Object>> groupList(@Param("warehouseid") Long warehouseid,@Param("type") String type,@Param("sql") String sql,@Param("qty") Integer qty);
}
