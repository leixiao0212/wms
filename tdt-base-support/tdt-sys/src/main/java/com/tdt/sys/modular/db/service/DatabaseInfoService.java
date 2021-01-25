package com.tdt.sys.modular.db.service;

import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.base.db.entity.DatabaseInfo;
import com.tdt.sys.modular.db.model.params.DatabaseInfoParam;
import com.tdt.sys.modular.db.model.result.DatabaseInfoResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 数据库信息表 服务类
 * </p>
 *
 * @author thedreamtree
 * @since 2019-06-15
 */
public interface DatabaseInfoService extends IService<DatabaseInfo> {

    /**
     * 新增
     *
     * @author thedreamtree
     * @Date 2019-06-15
     */
    void add(DatabaseInfoParam param);

    /**
     * 删除
     *
     * @author thedreamtree
     * @Date 2019-06-15
     */
    void delete(DatabaseInfoParam param);

    /**
     * 更新
     *
     * @author thedreamtree
     * @Date 2019-06-15
     */
    void update(DatabaseInfoParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author thedreamtree
     * @Date 2019-06-15
     */
    DatabaseInfoResult findBySpec(DatabaseInfoParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author thedreamtree
     * @Date 2019-06-15
     */
    List<DatabaseInfoResult> findListBySpec(DatabaseInfoParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author thedreamtree
     * @Date 2019-06-15
     */
     LayuiPageInfo findPageBySpec(DatabaseInfoParam param);

}
