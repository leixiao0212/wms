package com.tdt.sys.modular.system.mapper;

import com.tdt.base.pojo.node.TreeviewNode;
import com.tdt.base.pojo.node.ZTreeNode;
import com.tdt.sys.modular.system.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author thedreamtree
 * @since 2018-12-07
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> tree();

    /**
     * 获取所有部门列表
     */
    Page<Map<String, Object>> list(@Param("page") Page page, @Param("condition") String condition, @Param("deptId") Long deptId);

    /**
     * 获取所有部门树列表
     */
    List<TreeviewNode> treeviewNodes();

    /**
     * where pids like ''
     */
    List<Dept> likePids(@Param("deptId") Long deptId);

    List<Long> queryWarehouses(@Param("deptId") Long deptId);

    int deletewById(@Param("deptId") Long deptId);

    int savewd(@Param("deptId") Long deptId,@Param("warehouseId") Long id);
}
