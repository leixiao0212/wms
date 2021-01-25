package com.tdt.modular.outstore.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.outstore.entity.Review;
import com.tdt.modular.outstore.entity.ReviewDetail;
import com.tdt.modular.outstore.model.params.ReviewDetailParam;
import com.tdt.modular.outstore.model.params.ReviewParam;
import com.tdt.modular.outstore.service.ReviewDetailService;
import com.tdt.modular.outstore.service.ReviewService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.outstore.wrapper.ReviewWrapper;
import com.tdt.sys.core.constant.factory.ConstantFactory;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.sys.modular.system.entity.Dict;
import com.tdt.sys.modular.system.warpper.DeptWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author chenc
 * @Date 2019-09-18 14:28:35
 */
@Controller
@RequestMapping("/review")
public class ReviewController extends BaseController {

    private String PREFIX = "/modular/outstore/review";

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewDetailService reviewDetailService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-09-18
     */
    @RequestMapping("")
    public String index(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("review_state");
        model.addAttribute("dicts", dicts);
        return PREFIX + "/review.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-09-18
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/review_add.html";
    }

    /**
     * 添加出库复核页面
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("/add_detail")
    public String addDetail(Model model, Integer id) {
        model.addAttribute("id", id);
        return PREFIX + "/reviewDetail_add.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-09-18
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/review_edit.html";
    }

    /**
     * 查看出库复核详情页面
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("/checkDetail")
    public String checkDetail() {
        return PREFIX + "/reviewDetail.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-09-18
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(ReviewParam reviewParam) {
        this.reviewService.add(reviewParam);
        return ResponseData.success();
    }

    /**
     * 新增拣货信息
     */
    @RequestMapping(value = "/addHead")
    @ResponseBody
    public JSONObject addHead(@Valid Review review, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject resultObj = reviewService.addHead(review);
        return resultObj;
    }

    /**
     * 添加出库复核明细信息
     */
    @RequestMapping(value = "/addReviewDetail")
    @ResponseBody
    public JSONObject addDetail(ReviewDetail reviewDetail) {
        if (ToolUtil.isOneEmpty(reviewDetail.getCommoditybar(),reviewDetail.getQty())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = reviewDetailService.addDetail(reviewDetail);
        return result;
    }

    /**
     * 点击添加出库复核明细货明细的结束按钮
     */
    @RequestMapping(value = "/finish")
    @ResponseBody
    public Object finish(@Valid ReviewDetail reviewDetail, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        return reviewService.finish(reviewDetail);
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-09-18
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(ReviewParam reviewParam) {
        this.reviewService.update(reviewParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author chenc
     * @Date 2019-09-18
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(ReviewParam reviewParam) {
        this.reviewService.delete(reviewParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author chenc
     * @Date 2019-09-18
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(ReviewParam reviewParam) {
        Review detail = this.reviewService.getById(reviewParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author chenc
     * @Date 2019-09-18
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(ReviewParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = reviewService.list(page, paramCondition);
        page.setRecords(new ReviewWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询添加出库复核详情列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/addDetailList")
    public Object addDetailList(ReviewDetailParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = reviewDetailService.list(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

}


