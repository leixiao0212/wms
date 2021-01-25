package com.tdt.modular.outstore.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.outstore.entity.PicktaskDetail;
import com.tdt.modular.outstore.mapper.PicktaskDetailMapper;
import com.tdt.modular.outstore.model.params.PicktaskDetailParam;
import com.tdt.modular.outstore.model.result.PicktaskDetailResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 拣货任务明细服务
 */
@Service
public class PicktaskDetailService extends ServiceImpl<PicktaskDetailMapper, PicktaskDetail> {

    @Transactional(rollbackFor = Exception.class)
    public void add(PicktaskDetailParam param){
        PicktaskDetail entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(PicktaskDetailParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PicktaskDetailParam param){
        PicktaskDetail oldEntity = getOldEntity(param);
        PicktaskDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, PicktaskDetailParam paramCondition) {
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public PicktaskDetailResult findBySpec(PicktaskDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<PicktaskDetailResult> findListBySpec(PicktaskDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(PicktaskDetailParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(PicktaskDetailParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private PicktaskDetail getOldEntity(PicktaskDetailParam param) {
        return this.getById(getKey(param));
    }

    private PicktaskDetail getEntity(PicktaskDetailParam param) {
        PicktaskDetail entity = new PicktaskDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
