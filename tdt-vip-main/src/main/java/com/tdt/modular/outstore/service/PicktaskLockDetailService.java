package com.tdt.modular.outstore.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.outstore.entity.PicktaskLockDetail;
import com.tdt.modular.outstore.mapper.PicktaskLockDetailMapper;
import com.tdt.modular.outstore.model.params.PicktaskLockDetailParam;
import com.tdt.modular.outstore.model.result.PicktaskLockDetailResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 拣货任务出库订单锁定明细
 */
@Service
public class PicktaskLockDetailService extends ServiceImpl<PicktaskLockDetailMapper, PicktaskLockDetail> {

    @Transactional(rollbackFor = Exception.class)
    public void add(PicktaskLockDetailParam param){
        PicktaskLockDetail entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(PicktaskLockDetailParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PicktaskLockDetailParam param){
        PicktaskLockDetail oldEntity = getOldEntity(param);
        PicktaskLockDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public PicktaskLockDetailResult findBySpec(PicktaskLockDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<PicktaskLockDetailResult> findListBySpec(PicktaskLockDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(PicktaskLockDetailParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(PicktaskLockDetailParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private PicktaskLockDetail getOldEntity(PicktaskLockDetailParam param) {
        return this.getById(getKey(param));
    }

    private PicktaskLockDetail getEntity(PicktaskLockDetailParam param) {
        PicktaskLockDetail entity = new PicktaskLockDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    public int updateoutorderno(){
        return this.baseMapper.updateoutorderno();
    }
}
