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
import com.tdt.modular.outstore.entity.*;
import com.tdt.modular.outstore.mapper.ReviewMapper;
import com.tdt.modular.outstore.model.params.ReviewParam;
import com.tdt.modular.outstore.model.result.ReviewResult;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.service.StockService;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 出库复核服务类
 */
@Service
public class ReviewService extends ServiceImpl<ReviewMapper, Review> {

    @Autowired
    private OutorderService outorderService;

    @Autowired
    private OutorderDetailService outorderDetailService;

    @Autowired
    private ReviewDetailService reviewDetailService;

    @Autowired
    private PicktaskLockDetailService picktaskLockDetailService;

    @Autowired
    private StockService stockService;

    @Autowired
    private PicktaskService picktaskService;

    @Transactional(rollbackFor = Exception.class)
    public void add(ReviewParam param){
        Review entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addHead(Review review) {
        JSONObject result = new JSONObject();
        Outorder outorder = outorderService.getOne(new QueryWrapper<Outorder>().eq("outorderno",review.getOutorderno()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
        //判断出库订单编号是否存在并且状态为拣货中且属于当前仓库
        if (outorder!=null&&outorder.getState().equals(Constants.OutorderState.PICKING)){
            Review _review = this.getOne(new QueryWrapper<Review>().eq("outorderno",review.getOutorderno()));
            //判断出库订单是否已被出库复核
            if (_review!=null){
                //判断此出库订单出库复核状态是否为初始或复核中
                if (_review.getState().equals(Constants.ReviewState.INIT)&&_review.getState().equals(Constants.ReviewState.REVIEWING)){
                    result.put("code", 200);
                    result.put("message", "此出库单号已被出库复核");
                    result.put("review", _review);
                } else {
                    result.put("code", 500);
                    result.put("message", "此出库订单单号已复核完成，请勿重复复核");
                }
            } else {
                review.setOutorderid(outorder.getId());
                review.setState(Constants.ReviewState.INIT);
                review.setWarehouseid(ShiroKit.getUser().getWarehouseId());
                review.setCreateid(ShiroKit.getUser().getId());
                review.setCreator(ShiroKit.getUser().getName());
                review.setCreatetime(new Date());
                this.save(review);
                result.put("code", 200);
                result.put("message", "出库复核开始成功");
                result.put("review", review);
            }
        } else {
            result.put("code", 500);
            result.put("message", "此出库订单单号不符合出库审核条件，请重新输入");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(ReviewParam param){
        Review review = this.getById(param.getId());
        reviewDetailService.remove(new QueryWrapper<ReviewDetail>().eq("pid",review.getId()));
        this.removeById(review.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ReviewParam param){
        Review oldEntity = getOldEntity(param);
        Review newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject finish(ReviewDetail reviewDetail) {
        JSONObject result = new JSONObject();
        Review review = this.getById(reviewDetail.getPid());
        if (review.getState().equals(Constants.ReviewState.REVIEWING)){
            //将出库审核明细与出库订单明细进行对比，如果商品对应的数量等于出库订单明细数量，修改出库复核状态为完成，扣除此出库订单在拣货任务中锁定的库存，否则为异常
            List<ReviewDetail> reviewDetailList = reviewDetailService.list(new QueryWrapper<ReviewDetail>().eq("pid",review.getId()));
            for (ReviewDetail _reviewDetail:reviewDetailList){
                OutorderDetail outorderDetail = outorderDetailService.getOne(new QueryWrapper<OutorderDetail>().eq("pid",review.getOutorderid()).eq("commodityid",_reviewDetail.getCommodityid()));
                if (_reviewDetail.getQty().equals(outorderDetail.getQty())){
                    review.setState(Constants.ReviewState.REVIEWED);
                    this.updateById(review);
                    List<PicktaskLockDetail> picktaskLockDetailList = picktaskLockDetailService.list(new QueryWrapper<PicktaskLockDetail>().eq("outorderid",outorderDetail.getPid()));
                    for (PicktaskLockDetail picktaskLockDetail:picktaskLockDetailList){
                        Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",picktaskLockDetail.getLocatorid()).eq("commodityid",picktaskLockDetail.getCommodityid()));
                        if (stock.getCqty()-picktaskLockDetail.getQty()==0){
                            stockService.removeById(stock);
                        } else {
                            stock.setCqty(stock.getCqty()-picktaskLockDetail.getQty());
                            stock.setLqty(stock.getLqty()-picktaskLockDetail.getQty());
                            stock.setUpdateid(ShiroKit.getUser().getId());
                            stock.setUpdator(ShiroKit.getUser().getName());
                            stock.setUpdatetime(new Date());
                            stockService.updateById(stock);
                        }
                        //增加库存日志
                        Picktask picktask = picktaskService.getById(picktaskLockDetail.getPid());
                        stockService.addStockLog(picktask.getPicktaskno(),stock.getWarehouseid(),stock.getLocatorid(), stock.getLocatorcode(),
                                stock.getLocatorname(),stock.getCommodityid(), stock.getCommoditybar(),stock.getCommodityname(),
                                picktaskLockDetail.getQty()*(-1), Constants.StockLogType.OUTSTOCK);
                    }
                } else {
                    review.setState(Constants.ReviewState.EXCEPTIONTODO);
                    this.updateById(review);
                    result.put("code", 200);
                    result.put("message", "出库订单复核结束成功");
                    return result;
                }
            }
            Outorder outorder=this.outorderService.getById(review.getOutorderid());
            outorder.setState(Constants.OutorderState.OUTSTORED);
            this.outorderService.updateById(outorder);
        } else if (review.getState().equals(Constants.ReviewState.INIT)) {
            result.put("code", 500);
            result.put("message", "此出库订单还未录入明细，无法结束");
        } else {
            result.put("code", 500);
            result.put("message", "此出库订单已结束出库复核，请勿重复操作");
        }
        result.put("code", 200);
        result.put("message", "出库订单复核结束成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, ReviewParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public ReviewResult findBySpec(ReviewParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<ReviewResult> findListBySpec(ReviewParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(ReviewParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(ReviewParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Review getOldEntity(ReviewParam param) {
        return this.getById(getKey(param));
    }

    private Review getEntity(ReviewParam param) {
        Review entity = new Review();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
