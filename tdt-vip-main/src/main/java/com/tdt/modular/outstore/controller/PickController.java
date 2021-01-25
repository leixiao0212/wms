package com.tdt.modular.outstore.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.outstore.entity.Pick;
import com.tdt.modular.outstore.entity.PickDetail;
import com.tdt.modular.outstore.model.params.PickDetailParam;
import com.tdt.modular.outstore.model.params.PickParam;
import com.tdt.modular.outstore.service.PickDeailService;
import com.tdt.modular.outstore.service.PickService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.outstore.wrapper.PickWrapper;
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
 * @Date 2019-09-17 10:33:04
 */
@Controller
@RequestMapping("/pick")
public class PickController extends BaseController {

    private String PREFIX = "/modular/outstore/pick";

    @Autowired
    private PickService pickService;

    @Autowired
    private PickDeailService pickDeailService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("")
    public String index(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("pick_state");
        model.addAttribute("dicts", dicts);
        return PREFIX + "/pick.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("/add")
    public String add(Model model) {
        return PREFIX + "/pick_add.html";
    }

    /**
     * 添加拣货明细界面页面
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("/add_detail")
    public String addDetail(Model model,Integer id,String pickno) {
        model.addAttribute("id", id);
        model.addAttribute("pickno", pickno);
        return PREFIX + "/pickDetail_add.html";
    }

    /**
     * 查看拣货详情页面
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("/checkDetail")
    public String checkDetail() {
        return PREFIX + "/pickDetail.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/pick_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(PickParam pickParam) {
        this.pickService.add(pickParam);
        return ResponseData.success();
    }

    /**
     * 新增拣货信息
     */
    @RequestMapping(value = "/addHead")
    @ResponseBody
    public JSONObject addHead(@Valid Pick pick, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject resultObj = pickService.addHead(pick);
        return resultObj;
    }

    /**
     * 添加采购订单明细信息
     */
    @RequestMapping(value = "/addPickDetail")
    @ResponseBody
    public JSONObject addDetail(PickDetail pickDetail) {
        if (ToolUtil.isOneEmpty(pickDetail.getCommoditybar(),pickDetail.getLocatorcode(),pickDetail.getQty())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = pickDeailService.addDetail(pickDetail);
        return result;
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(PickParam pickParam) {
        this.pickService.update(pickParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(PickParam pickParam) {
        this.pickService.delete(pickParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(PickParam pickParam) {
        Pick detail = this.pickService.getById(pickParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(PickParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = pickService.list(page, paramCondition);
        page.setRecords(new PickWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询添加拣货详情列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/addDetailList")
    public LayuiPageInfo addDetailList(PickDetailParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = pickDeailService.list(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 点击添加拣货明细的结束按钮
     */
    @RequestMapping(value = "/finish")
    @ResponseBody
    public Object finish(@Valid PickDetail pickDetail, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        return pickService.finish(pickDetail);
    }

    /**
     * 点击还原按钮
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("/restore")
    @ResponseBody
    public ResponseData restore(PickParam pickParam) {
        this.pickService.restore(pickParam);
        return ResponseData.success();
    }

    /**
     * 将拣货状态标记为异常
     * @param pickid
     * @return
     */
    @RequestMapping("/confirmChangeState")
    @ResponseBody
    public ResponseData confirmChangeState(Long pickid) {
        this.pickService.confirmChangeState(pickid);
        return ResponseData.success();
    }
}


