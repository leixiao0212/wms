package com.tdt.modular.instorage.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.modular.instorage.entity.Otherin;
import com.tdt.modular.instorage.model.params.OtherinParam;
import com.tdt.modular.instorage.service.OtherinService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.instorage.wrapper.OtherinWrapper;
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
 * @Date 2019-08-21 10:55:52
 */
@Controller
@RequestMapping("/otherin")
public class OtherinController extends BaseController {

    private String PREFIX = "/modular/instorage/otherin";

    @Autowired
    private OtherinService otherinService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("")
    public String index(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("otherin_state");
        model.addAttribute("dicts", dicts);
        return PREFIX + "/otherin.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/otherin_add.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/otherin_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(OtherinParam otherinParam) {
        this.otherinService.add(otherinParam);
        return ResponseData.success();
    }

    /**
     * 新增其他入库
     */
    @RequestMapping(value = "/addHead")
    @ResponseBody
    public JSONObject add(@Valid Otherin otherin, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        return this.otherinService.addHead(otherin);
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(OtherinParam otherinParam) {
        this.otherinService.update(otherinParam);
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
    public ResponseData delete(OtherinParam otherinParam) {
        this.otherinService.delete(otherinParam);
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
    public ResponseData detail(OtherinParam otherinParam) {
        Otherin detail = this.otherinService.getById(otherinParam.getId());
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
    public Object list(OtherinParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = otherinService.list(page, paramCondition);
        page.setRecords(new OtherinWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 审核
     */
    @RequestMapping(value = "/review")
    @ResponseBody
    public Object review(@RequestParam Integer id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = otherinService.review(id);
        return result;
    }

    /**
     * 撤销审核
     */
    @RequestMapping(value = "/cancelReview")
    @ResponseBody
    public Object cancelReview(@RequestParam Integer id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = otherinService.cancelReview(id);
        return result;
    }
}


