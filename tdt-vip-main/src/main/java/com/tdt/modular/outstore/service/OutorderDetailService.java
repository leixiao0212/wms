package com.tdt.modular.outstore.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.core.common.constant.Constants;
import com.tdt.modular.base.entity.Commodity;
import com.tdt.modular.base.service.CommodityService;
import com.tdt.modular.outstore.entity.Outorder;
import com.tdt.modular.outstore.entity.OutorderDetail;
import com.tdt.modular.outstore.mapper.OutorderDetailMapper;
import com.tdt.modular.outstore.model.params.OutorderDetailParam;
import com.tdt.modular.outstore.model.result.OutorderDetailResult;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 出库订单明细服务类
 */
@Service
public class OutorderDetailService extends ServiceImpl<OutorderDetailMapper, OutorderDetail> {

    @Autowired
    private OutorderService outorderService;

    @Autowired
    private CommodityService commodityService;

    @Transactional(rollbackFor = Exception.class)
    public void add(OutorderDetailParam param){
        OutorderDetail entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addDetail(OutorderDetail outorderDetail){
        JSONObject result = new JSONObject();
        Outorder outorder = outorderService.getOne(new QueryWrapper<Outorder>().eq("id",outorderDetail.getPid()));
        //判断出库订单状态是否不为已审核
        if (!outorder.getState().equals(Constants.OutorderState.REVIEW)){
            OutorderDetail detail = this.getOne(new QueryWrapper<OutorderDetail>().eq("commoditybar",outorderDetail.getCommoditybar()).eq("pid",outorderDetail.getPid()));
            //判断出库明细表中是否有相同的记录
            if (detail!=null){
                detail.setQty(detail.getQty()+outorderDetail.getQty());
                detail.setUpdateid(ShiroKit.getUser().getId());
                detail.setUpdator(ShiroKit.getUser().getName());
                detail.setUpdatetime(new Date());
                this.updateById(detail);
                result.put("code", 200);
                result.put("message", "出库明细修改成功");
            } else {
                Commodity commodity = commodityService.getOne(new QueryWrapper<Commodity>().eq("bar",outorderDetail.getCommoditybar()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                //判断商品编码是否先存在且商品编码属于当前仓库
                if (commodity!=null){
                    outorderDetail.setCommodityid(commodity.getId());
                    outorderDetail.setCommodityname(commodity.getName());
                    outorderDetail.setCreateid(ShiroKit.getUser().getId());
                    outorderDetail.setCreator(ShiroKit.getUser().getName());
                    outorderDetail.setCreatetime(new Date());
                    this.save(outorderDetail);
                    outorder.setState(Constants.OutorderState.UNREVIEW);
                    outorder.setUpdateid(ShiroKit.getUser().getId());
                    outorder.setUpdator(ShiroKit.getUser().getName());
                    outorder.setUpdatetime(new Date());
                    outorderService.updateById(outorder);
                    result.put("code", 200);
                    result.put("message", "出库明细添加成功");
                } else {
                    result.put("code", 500);
                    result.put("message", "商品编码不存在或商品编码不属于当前仓库，请重新输入");
                }
            }
        } else {
            result.put("code", 500);
            result.put("message", "出库订单已被审核，无法修改明细");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(OutorderDetailParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(OutorderDetailParam param){
        OutorderDetail oldEntity = getOldEntity(param);
        OutorderDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> detailList(Page page, OutorderDetailParam paramCondition) {
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public OutorderDetailResult findBySpec(OutorderDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<OutorderDetailResult> findListBySpec(OutorderDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(OutorderDetailParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(OutorderDetailParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private OutorderDetail getOldEntity(OutorderDetailParam param) {
        return this.getById(getKey(param));
    }

    private OutorderDetail getEntity(OutorderDetailParam param) {
        OutorderDetail entity = new OutorderDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
