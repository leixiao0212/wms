package com.tdt.modular.outstore.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.outstore.entity.OutorderTag;
import com.tdt.modular.outstore.mapper.OutorderTagMapper;
import com.tdt.modular.outstore.model.params.OutorderTagParam;
import com.tdt.modular.outstore.model.result.OutorderTagResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 出库订单标签服务类
 */
@Service
public class OutorderTagService extends ServiceImpl<OutorderTagMapper, OutorderTag> {

    @Transactional(rollbackFor = Exception.class)
    public void add(OutorderTagParam param){
        OutorderTag entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(OutorderTagParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(OutorderTagParam param){
        OutorderTag oldEntity = getOldEntity(param);
        OutorderTag newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public OutorderTagResult findBySpec(OutorderTagParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<OutorderTagResult> findListBySpec(OutorderTagParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(OutorderTagParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(OutorderTagParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private OutorderTag getOldEntity(OutorderTagParam param) {
        return this.getById(getKey(param));
    }

    private OutorderTag getEntity(OutorderTagParam param) {
        OutorderTag entity = new OutorderTag();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
}
