package com.tdt.modular.storage.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.modular.storage.entity.Move;
import com.tdt.modular.storage.model.params.MoveParam;
import com.tdt.modular.storage.service.MoveService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.storage.wrapper.MoveWrapper;
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
 * @Date 2019-08-28 09:37:05
 */
@Controller
@RequestMapping("/move")
public class MoveController extends BaseController {

    private String PREFIX = "/modular/storage/move";

    @Autowired
    private MoveService moveService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("")
    public String index(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("move_state");
        model.addAttribute("dicts", dicts);
        return PREFIX + "/move.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/move_add.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/move_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(MoveParam moveParam) {
        this.moveService.add(moveParam);
        return ResponseData.success();
    }

    /**
     * 新增移库信息
     */
    @RequestMapping(value = "/addHead")
    @ResponseBody
    public JSONObject addHead(@Valid Move move, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject resultObj = this.moveService.addHead(move);
        return resultObj;
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(MoveParam moveParam) {
        this.moveService.update(moveParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(MoveParam moveParam) {
        this.moveService.delete(moveParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(MoveParam moveParam) {
        Move detail = this.moveService.getById(moveParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @ResponseBody
    @RequestMapping("/list")
    public Object list(MoveParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = moveService.list(page, paramCondition);
        page.setRecords(new MoveWrapper(result).wrap());
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
        JSONObject result = this.moveService.review(id);
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
        JSONObject result = this.moveService.cancelReview(id);
        return result;
    }

}


