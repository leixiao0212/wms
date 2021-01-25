package com.tdt.modular.instorage.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.modular.instorage.entity.Receive;
import com.tdt.modular.instorage.entity.ReceiveDetail;
import com.tdt.modular.instorage.model.params.ReceiveDetailParam;
import com.tdt.modular.instorage.model.params.ReceiveParam;
import com.tdt.modular.instorage.service.ReceiveDetailService;
import com.tdt.modular.instorage.service.ReceiveService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.instorage.wrapper.ReceiveWrapper;
import com.tdt.sys.core.constant.factory.ConstantFactory;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.sys.modular.system.entity.Dict;
import com.tdt.sys.modular.system.warpper.DeptWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author chenc
 * @Date 2019-08-21 10:55:52
 */
@Controller
@RequestMapping("/receive")
public class ReceiveController extends BaseController {

    @Autowired
    private ReceiveDetailService receiveDetailService;

    private String PREFIX = "/modular/instorage/receive";

    @Autowired
    private ReceiveService receiveService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("")
    public String index(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("receive_state");
        model.addAttribute("dicts", dicts);
        return PREFIX + "/receive.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/receive_add.html";
    }

    /**
     * 订单收货明细页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/add_detail")
    public String addDetail(Model model,Integer id) {
        Receive receive=receiveService.getById(id);
        model.addAttribute("id", id);
        model.addAttribute("receive", receive);
        return PREFIX + "/receiveDetail_add.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/receive_edit.html";
    }

    /**
     * 接货订单详情页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/receive_detail")
    public String detail(Long id) {
        return PREFIX + "/receiveDetail.html";
    }


    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(ReceiveParam receiveParam) {
        this.receiveService.add(receiveParam);
        return ResponseData.success();
    }

    /**
     * 接货
     * @param receive
     * @param result
     * @return
     */
    @RequestMapping("/addHead")
    @ResponseBody
    public JSONObject addHead(@Valid Receive receive, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        return this.receiveService.addhead(receive);
    }

    /**
     * 添加采购订单明细信息
     */
    @RequestMapping(value = "/addDetail")
    @ResponseBody
    public JSONObject addDetail(ReceiveDetail receiveDetail) {
        if (ToolUtil.isOneEmpty(receiveDetail.getCommoditybar(),receiveDetail.getQty())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = this.receiveDetailService.addDetail(receiveDetail);
        return result;
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(ReceiveParam receiveParam) {
        this.receiveService.update(receiveParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(ReceiveParam receiveParam) {
        this.receiveService.deleteAll(receiveParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(ReceiveParam receiveParam) {
        Receive detail = this.receiveService.getById(receiveParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/list")
    public Object list(ReceiveParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = receiveService.list(page, paramCondition);
        page.setRecords(new ReceiveWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询收货订单详情列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/addDetailList")
    public Object addDetailList(ReceiveDetailParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = receiveDetailService.detailList(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查看详情列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/detailList")
    public Object detailList(Long id) {
        Receive receive = receiveService.getById(id);
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = receiveDetailService.selectByPid(page,receive.getId());
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 审核
     */
    @RequestMapping(value = "/review")
    @ResponseBody
    public Object review(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = receiveService.review(id);
        return result;
    }

    /**
     * 撤销审核
     */
    @RequestMapping(value = "/cancelReview")
    @ResponseBody
    public Object cancelReview(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = receiveService.cancelReview(id);
        return result;
    }
}


