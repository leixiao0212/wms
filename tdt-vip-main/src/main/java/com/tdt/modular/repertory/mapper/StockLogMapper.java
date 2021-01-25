package com.tdt.modular.repertory.mapper;

import com.tdt.modular.repertory.entity.StockLog;
import com.tdt.modular.repertory.model.params.StockLogParam;
import com.tdt.modular.repertory.model.result.StockLogResult;
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
 * @since 2019-09-06
 */
public interface StockLogMapper extends BaseMapper<StockLog> {

    /**
     * 获取列表
     *
     * @author chenc
     * @Date 2019-09-06
     */
    List<StockLogResult> customList(@Param("paramCondition") StockLogParam paramCondition);

    /**
     * 获取map列表
     *
     * @author chenc
     * @Date 2019-09-06
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") StockLogParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author chenc
     * @Date 2019-09-06
     */
    Page<StockLogResult> customPageList(@Param("page") Page page, @Param("paramCondition") StockLogParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author chenc
     * @Date 2019-09-06
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") StockLogParam paramCondition);

    /**
     * 根据来源单号查询日志列表并根据商品编码排序
     * @param billno
     * @return
     */
    List<StockLog> selectListByBillno(@Param("billno") String billno,@Param("state") String state,@Param("type") String type);

}
