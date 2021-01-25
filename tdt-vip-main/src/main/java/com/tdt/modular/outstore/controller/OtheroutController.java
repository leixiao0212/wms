package com.tdt.modular.outstore.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.outstore.entity.Otherout;
import com.tdt.modular.outstore.model.params.OtheroutParam;
import com.tdt.modular.outstore.service.OtheroutService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.outstore.wrapper.OtheroutWrapper;
import com.tdt.sys.core.constant.factory.ConstantFactory;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.sys.modular.system.entity.Dict;
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
 * @Date 2019-09-19 15:07:47
 */
@Controller
@RequestMapping("/otherout")
public class OtheroutController extends BaseController {

    private String PREFIX = "/modular/outstore/otherout";

    @Autowired
    private OtheroutService otheroutService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-09-19
     */
    @RequestMapping("")
    public String index(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("otherout_state");
        model.addAttribute("dicts", dicts);
        return PREFIX + "/otherout.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-09-19
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/otherout_add.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-09-19
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/otherout_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-09-19
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(OtheroutParam otheroutParam) {
        this.otheroutService.add(otheroutParam);
        return ResponseData.success();
    }

    /**
     * 新增其他出库
     */
    @RequestMapping(value = "/addHead")
    @ResponseBody
    public JSONObject add(@Valid Otherout otherout, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        return otheroutService.addHead(otherout);
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-09-19
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(OtheroutParam otheroutParam) {
        this.otheroutService.update(otheroutParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author chenc
     * @Date 2019-09-19
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(OtheroutParam otheroutParam) {
        this.otheroutService.delete(otheroutParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author chenc
     * @Date 2019-09-19
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(OtheroutParam otheroutParam) {
        Otherout detail = this.otheroutService.getById(otheroutParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author chenc
     * @Date 2019-09-19
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(OtheroutParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = otheroutService.list(page, paramCondition);
        page.setRecords(new OtheroutWrapper(result).wrap());
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
        JSONObject result = otheroutService.review(id);
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
        JSONObject result = otheroutService.cancelReview(id);
        return result;
    }
}


