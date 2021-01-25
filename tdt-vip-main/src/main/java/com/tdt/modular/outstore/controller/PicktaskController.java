package com.tdt.modular.outstore.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.outstore.entity.Picktask;
import com.tdt.modular.outstore.model.params.PicktaskDetailParam;
import com.tdt.modular.outstore.model.params.PicktaskOutorderParam;
import com.tdt.modular.outstore.model.params.PicktaskParam;
import com.tdt.modular.outstore.service.PicktaskDetailService;
import com.tdt.modular.outstore.service.PicktaskOutorderService;
import com.tdt.modular.outstore.service.PicktaskService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.sys.modular.system.warpper.DeptWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author chenc
 * @Date 2019-09-12 15:46:19
 */
@Controller
@RequestMapping("/picktask")
public class PicktaskController extends BaseController {

    private String PREFIX = "/modular/outstore/picktask";

    @Autowired
    private PicktaskService picktaskService;

    @Autowired
    private PicktaskOutorderService picktaskOutorderService;

    @Autowired
    private PicktaskDetailService picktaskDetailService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-09-12
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/picktask.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-09-12
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/picktask_add.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-09-12
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/picktask_edit.html";
    }

    /**
     * 拣货订单明细页面
     *
     * @author chenc
     * @Date 2019-09-12
     */
    @RequestMapping("/picktaskDetail")
    public String picktaskDetail() {
        return PREFIX + "/picktaskDetail.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-09-12
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(PicktaskParam picktaskParam) {
        this.picktaskService.add(picktaskParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-09-12
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(PicktaskParam picktaskParam) {
        this.picktaskService.update(picktaskParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author chenc
     * @Date 2019-09-12
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(PicktaskParam picktaskParam) {
        this.picktaskService.delete(picktaskParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author chenc
     * @Date 2019-09-12
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(PicktaskParam picktaskParam) {
        Picktask detail = this.picktaskService.getById(picktaskParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author chenc
     * @Date 2019-09-12
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(PicktaskParam paramCondition,String outorderno,String isReceive) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = picktaskService.list(page, paramCondition,outorderno,isReceive);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询拣货任务与出库订单关联列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/pickTaskOutOrderList")
    public Object pickTaskOutOrderList(PicktaskOutorderParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = picktaskOutorderService.list(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询拣货任务明细列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/pickTaskDetailList")
    public Object pickTaskDetailList(PicktaskDetailParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = picktaskDetailService.list(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 点击还原按钮
     *
     * @author chenc
     * @Date 2019-09-17
     */
    @RequestMapping("/restore")
    @ResponseBody
    public ResponseData restore(PicktaskParam picktaskParam) {
        this.picktaskService.restore(picktaskParam);
        return ResponseData.success();
    }
}


