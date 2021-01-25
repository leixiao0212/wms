package com.tdt.modular.outstore.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.outstore.entity.PicktaskOutorder;
import com.tdt.modular.outstore.mapper.PicktaskOutorderMapper;
import com.tdt.modular.outstore.model.params.PicktaskOutorderParam;
import com.tdt.modular.outstore.model.result.PicktaskOutorderResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 拣货出货关联服务
 */
@Service
public class PicktaskOutorderService extends ServiceImpl<PicktaskOutorderMapper, PicktaskOutorder> {

    @Transactional(rollbackFor = Exception.class)
    public void add(PicktaskOutorderParam param){
        PicktaskOutorder entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(PicktaskOutorderParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PicktaskOutorderParam param){
        PicktaskOutorder oldEntity = getOldEntity(param);
        PicktaskOutorder newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, PicktaskOutorderParam paramCondition) {
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public PicktaskOutorderResult findBySpec(PicktaskOutorderParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<PicktaskOutorderResult> findListBySpec(PicktaskOutorderParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(PicktaskOutorderParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(PicktaskOutorderParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private PicktaskOutorder getOldEntity(PicktaskOutorderParam param) {
        return this.getById(getKey(param));
    }

    private PicktaskOutorder getEntity(PicktaskOutorderParam param) {
        PicktaskOutorder entity = new PicktaskOutorder();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
