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
import com.tdt.modular.outstore.entity.Review;
import com.tdt.modular.outstore.entity.ReviewDetail;
import com.tdt.modular.outstore.mapper.ReviewDetailMapper;
import com.tdt.modular.outstore.model.params.ReviewDetailParam;
import com.tdt.modular.outstore.model.result.ReviewDetailResult;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 出库复核明细服务类
 */
@Service
public class ReviewDetailService extends ServiceImpl<ReviewDetailMapper, ReviewDetail> {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private OutorderService outorderService;

    @Autowired
    private OutorderDetailService outorderDetailService;
    @Transactional(rollbackFor = Exception.class)
    public void add(ReviewDetailParam param){
        ReviewDetail entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(ReviewDetailParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ReviewDetailParam param){
        ReviewDetail oldEntity = getOldEntity(param);
        ReviewDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, ReviewDetailParam paramCondition) {
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addDetail(ReviewDetail reviewDetail) {
        JSONObject result = new JSONObject();
        Review review = reviewService.getOne(new QueryWrapper<Review>().eq("id",reviewDetail.getPid()));
        //判断出库复核状态是否为初始或者复核中
        if (review.getState().equals(Constants.ReviewState.INIT)||review.getState().equals(Constants.ReviewState.REVIEWING)){
            ReviewDetail _reviewDetail = this.getOne(new QueryWrapper<ReviewDetail>().eq("commoditybar",reviewDetail.getCommoditybar()));
            //判断出库复核明细中是否有重复记录
            if (_reviewDetail!=null){
                _reviewDetail.setQty(_reviewDetail.getQty()+reviewDetail.getQty());
                _reviewDetail.setUpdateid(ShiroKit.getUser().getId());
                _reviewDetail.setUpdator(ShiroKit.getUser().getName());
                _reviewDetail.setUpdatetime(new Date());
                this.updateById(_reviewDetail);
            } else {
                Commodity commodity = commodityService.getOne(new QueryWrapper<Commodity>().eq("bar",reviewDetail.getCommoditybar()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                //判断商品编码是否存在且商品编码属于当前仓库
                if (commodity!=null){
                    OutorderDetail outorderDetail = outorderDetailService.getOne(new QueryWrapper<OutorderDetail>().eq("pid",review.getOutorderid()).eq("commoditybar",commodity.getBar()));
                    //判断输入的商品是否为出库订单中的商品
                    if (outorderDetail!=null){
                        reviewDetail.setCommodityid(commodity.getId());
                        reviewDetail.setCommodityname(commodity.getName());
                        reviewDetail.setCreateid(ShiroKit.getUser().getId());
                        reviewDetail.setCreator(ShiroKit.getUser().getName());
                        reviewDetail.setCreatetime(new Date());
                        this.save(reviewDetail);
                        review.setState(Constants.ReviewState.REVIEWING);
                        reviewService.updateById(review);
                        result.put("code", 200);
                        result.put("message", "出库复核明细新增成功");
                    } else {
                        result.put("code", 500);
                        result.put("message", "此出库订单中没有此商品，请核对后重新输入");
                    }
                } else {
                    result.put("code", 500);
                    result.put("message", "商品编码不存在或此商品编码不属于当前仓库，请重新输入");
                }
            }
        } else {
            result.put("code", 500);
            result.put("message", "此出库订单已复核完成，无法重新复核");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public ReviewDetailResult findBySpec(ReviewDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<ReviewDetailResult> findListBySpec(ReviewDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(ReviewDetailParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(ReviewDetailParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private ReviewDetail getOldEntity(ReviewDetailParam param) {
        return this.getById(getKey(param));
    }

    private ReviewDetail getEntity(ReviewDetailParam param) {
        ReviewDetail entity = new ReviewDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
