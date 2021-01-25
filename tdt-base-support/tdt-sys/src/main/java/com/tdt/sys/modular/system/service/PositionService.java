package com.tdt.sys.modular.system.service;

import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.sys.modular.system.entity.Position;
import com.tdt.sys.modular.system.model.params.PositionParam;
import com.tdt.sys.modular.system.model.result.PositionResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 职位表 服务类
 * </p>
 *
 * @author thedreamtree
 * @since 2019-06-27
 */
public interface PositionService extends IService<Position> {

    /**
     * 新增
     *
     * @author thedreamtree
     * @Date 2019-06-27
     */
    void add(PositionParam param);

    /**
     * 删除
     *
     * @author thedreamtree
     * @Date 2019-06-27
     */
    void delete(PositionParam param);

    /**
     * 更新
     *
     * @author thedreamtree
     * @Date 2019-06-27
     */
    void update(PositionParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author thedreamtree
     * @Date 2019-06-27
     */
    PositionResult findBySpec(PositionParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author thedreamtree
     * @Date 2019-06-27
     */
    List<PositionResult> findListBySpec(PositionParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author thedreamtree
     * @Date 2019-06-27
     */
     LayuiPageInfo findPageBySpec(PositionParam param);

    /**
     * 获取多选框的职位列表
     *
     * @author thedreamtree
     * @Date 2019-06-27
     */
    LayuiPageInfo listPositions(Long userId);

}
