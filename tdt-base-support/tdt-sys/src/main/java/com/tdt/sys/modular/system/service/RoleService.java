package com.tdt.sys.modular.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tdt.sys.core.constant.Const;
import com.tdt.sys.core.constant.cache.Cache;
import com.tdt.sys.core.constant.factory.ConstantFactory;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.base.pojo.node.ZTreeNode;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.sys.core.log.LogObjectHolder;
import com.tdt.sys.core.util.CacheUtil;
import com.tdt.sys.modular.system.entity.Relation;
import com.tdt.sys.modular.system.entity.Role;
import com.tdt.sys.modular.system.mapper.RelationMapper;
import com.tdt.sys.modular.system.mapper.RoleMapper;
import com.tdt.sys.modular.system.model.RoleDto;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.sys.core.constant.Const;
import com.tdt.sys.core.constant.cache.Cache;
import com.tdt.sys.core.constant.factory.ConstantFactory;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.sys.core.log.LogObjectHolder;
import com.tdt.sys.core.util.CacheUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author thedreamtree
 * @since 2018-12-07
 */
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RelationMapper relationMapper;

    @Resource
    private UserService userService;

    /**
     * 添加角色
     *
     * @author thedreamtree
     * @Date 2018/12/23 6:40 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void addRole(Role role) {

        if (ToolUtil.isOneEmpty(role, role.getName(), role.getPid(), role.getDescription())) {
            throw new RequestEmptyException();
        }

        role.setRoleId(null);

        this.save(role);
    }

    /**
     * 编辑角色
     *
     * @author thedreamtree
     * @Date 2018/12/23 6:40 PM
     */
    @Transactional(rollbackFor = Exception.class)
    public void editRole(RoleDto roleDto) {

        if (ToolUtil.isOneEmpty(roleDto, roleDto.getName(), roleDto.getPid(), roleDto.getDescription())) {
            throw new RequestEmptyException();
        }

        Role old = this.getById(roleDto.getRoleId());
        BeanUtil.copyProperties(roleDto, old);
        this.updateById(old);

        //删除缓存
        CacheUtil.removeAll(Cache.CONSTANT);
    }

    /**
     * 设置某个角色的权限
     *
     * @param roleId 角色id
     * @param ids    权限的id
     * @date 2017年2月13日 下午8:26:53
     */
    @Transactional(rollbackFor = Exception.class)
    public void setAuthority(Long roleId, String ids) {
        //根据角色id查询出此角色的所有权限
        List<Relation> relationList = relationMapper.selectList(new QueryWrapper<Relation>().eq("role_id",roleId));
        //将原来的权限用Map<menuid,list下标方式存储>
        Map<Long,Integer> relationmap=new HashMap<Long,Integer>();
        for(int i=0;relationList!=null&&i<relationList.size();i++){
            Relation relation=relationList.get(i);
            relationmap.put(relation.getMenuId(),i);
        }
        //将现有的menuid存到list列表中
        List<Integer> indexlist=new ArrayList<Integer>();
        List<Long> newrelationList=Arrays.asList(Convert.toLongArray(ids.split(",")));
        for (int i=0;newrelationList!=null&&i<newrelationList.size();i++) {
            Long id=newrelationList.get(i);
            //如果当前的权限在原来的权限中能找到就将在Map集合中存入的下标存储到下标列表中
            if(relationmap.get(id)!=null){
                indexlist.add(relationmap.get(id));
            }else{
                //否则就将当前权限增加到权限列表中
                Relation relation = new Relation();
                relation.setRoleId(roleId);
                relation.setMenuId(id);
                this.relationMapper.insert(relation);
            }
        }
        //将下标列表按照降序排列，将现有的权限在原来的权限存在的下标移除
        indexlist.sort(Comparator.reverseOrder());
        for(int i=0;indexlist!=null&&i<indexlist.size();i++){
            int index=indexlist.get(i);
            relationList.remove(index);
        }
        // 删除原来权限与现有权限比较并且删除后剩余的权限删除
        for(int i=0;relationList!=null&&i<relationList.size();i++){
            Relation relation=relationList.get(i);
            this.relationMapper.deleteById(relation.getRelationId());
        }
        // 刷新当前用户的权限
        userService.refreshCurrentUser();
    }

    /**
     * 删除角色
     *
     * @author thedreamtree
     * @Date 2017/5/5 22:24
     */
    @Transactional(rollbackFor = Exception.class)
    public void delRoleById(Long roleId) {

        if (ToolUtil.isEmpty(roleId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }

        //不能删除超级管理员角色
        if (roleId.equals(Const.ADMIN_ROLE_ID)) {
            throw new ServiceException(BizExceptionEnum.CANT_DELETE_ADMIN);
        }

        //缓存被删除的角色名称
        LogObjectHolder.me().set(ConstantFactory.me().getSingleRoleName(roleId));

        //删除角色
        this.roleMapper.deleteById(roleId);

        //删除该角色所有的权限
        this.roleMapper.deleteRolesById(roleId);

        //删除缓存
        CacheUtil.removeAll(Cache.CONSTANT);
    }

    /**
     * 根据条件查询角色列表
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    public Page<Map<String, Object>> selectRoles(String condition) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectRoles(page, condition);
    }

    /**
     * 删除某个角色的所有权限
     *
     * @param roleId 角色id
     * @return
     * @date 2017年2月13日 下午7:57:51
     */
    public int deleteRolesById(Long roleId) {
        return this.baseMapper.deleteRolesById(roleId);
    }

    /**
     * 获取角色列表树
     *
     * @return
     * @date 2017年2月18日 上午10:32:04
     */
    public List<ZTreeNode> roleTreeList() {
        return this.baseMapper.roleTreeList();
    }

    /**
     * 获取角色列表树
     *
     * @return
     * @date 2017年2月18日 上午10:32:04
     */
    public List<ZTreeNode> roleTreeListByRoleId(Long[] roleId) {
        return this.baseMapper.roleTreeListByRoleId(roleId);
    }

}
