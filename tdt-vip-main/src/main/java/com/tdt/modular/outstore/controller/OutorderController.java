package com.tdt.modular.outstore.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.core.common.constant.Constants;
import com.tdt.core.util.SequenceBuilder;
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.service.WarehouseService;
import com.tdt.modular.outstore.entity.Outorder;
import com.tdt.modular.outstore.entity.OutorderDetail;
import com.tdt.modular.outstore.entity.OutorderTag;
import com.tdt.modular.outstore.model.params.OutorderDetailParam;
import com.tdt.modular.outstore.model.params.OutorderParam;
import com.tdt.modular.outstore.service.OutorderDetailService;
import com.tdt.modular.outstore.service.OutorderService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.outstore.service.OutorderTagService;
import com.tdt.modular.outstore.wrapper.OutorderWrapper;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author chenc
 * @Date 2019-09-10 16:41:50
 */
@Controller
@RequestMapping("/outorder")
public class OutorderController extends BaseController {

    private String PREFIX = "/modular/outstore/outorder";

    @Autowired
    private OutorderService outorderService;

    @Autowired
    private OutorderDetailService outorderDetailService;

    @Autowired
    private WarehouseService warehouseService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-09-10
     */
    @RequestMapping("")
    public String index(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("outorder_state");
        model.addAttribute("dicts", dicts);
        return PREFIX + "/outorder.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-09-10
     */
    @RequestMapping("/add")
    public String add(Model model) {
        String outorderno = SequenceBuilder.generateNo(Constants.PREFIX_NO.OUTORDER_NO);
        model.addAttribute("outorderno", outorderno);
        List<Warehouse> warehouses = warehouseService.list();
        model.addAttribute("warehouses", warehouses);
        return PREFIX + "/outorder_add.html";
    }

    /**
     * 添加出库订单详情页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/outorder_detail_add")
    public String addDetail(Integer id, String outorderno, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("outorderno", outorderno);
        return PREFIX + "/outorderDetail_add.html";
    }

    /**
     * 出库订单详情页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/outorder_detail")
    public String detail(Long id) {
        return PREFIX + "/outorderDetail.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-09-10
     */
    @RequestMapping("/edit")
    public String edit(Model model) {
        List<Warehouse> warehouses = warehouseService.list();
        model.addAttribute("warehouses", warehouses);
        return PREFIX + "/outorder_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-09-10
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(OutorderParam outorderParam) {
        Outorder outorder= outorderService.add(outorderParam);
        HashMap<String, Object> map = new HashMap<>();
        map.put("outorder", outorder);
        return ResponseData.success(map);
    }

    /**
     * 生成拣货任务
     */
    @RequestMapping(value = "/pick")
    @ResponseBody
    public Object addInventory(String ids) {
        if (ToolUtil.isEmpty(ids)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = outorderService.addPick(ids);
        return result;
    }

    /**
     * 添加出库订单明细信息
     */
    @RequestMapping(value = "/addDetail")
    @ResponseBody
    public JSONObject addDetail(OutorderDetail outorderDetail) {
        if (ToolUtil.isOneEmpty(outorderDetail.getPid(),outorderDetail.getCommoditybar(),outorderDetail.getQty())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = outorderDetailService.addDetail(outorderDetail);
        return result;
    }

    /**
     * 点击添加出库订单详情的结束按钮
     */
    @RequestMapping(value = "/finish")
    @ResponseBody
    public Object finish(@Valid OutorderDetail outorderDetail, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        return outorderService.finish(outorderDetail);
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-09-10
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(OutorderParam outorderParam) {
        this.outorderService.update(outorderParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author chenc
     * @Date 2019-09-10
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(OutorderParam outorderParam) {
        this.outorderService.delete(outorderParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author chenc
     * @Date 2019-09-10
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(OutorderParam outorderParam) {
        Outorder detail = this.outorderService.getById(outorderParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author chenc
     * @Date 2019-09-10
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(OutorderParam paramCondition, String startTime,String endTime) throws ParseException {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        if (paramCondition.getProvince()!=null&&paramCondition.getCity()!=null&&paramCondition.getCounty()!=null){
            if (paramCondition.getProvince().equals("请选择省")||paramCondition.getCity().equals("请选择市")||paramCondition.getCounty().equals("请选择区/县")){
                paramCondition.setProvince("");
                paramCondition.setCity("");
                paramCondition.setCounty("");
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date stime = null;
        Date etime = null;
        if (startTime!=null&&!startTime.equals("")){
            stime = simpleDateFormat.parse(startTime);
        }
        if (endTime!=null&&!endTime.equals("")){
            etime = simpleDateFormat.parse(endTime);
        }
        //根据条件查询日志
        List<Map<String, Object>> result = outorderService.list(page, paramCondition,stime,etime);
        page.setRecords(new OutorderWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询添加出库订单详情列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/addDetailList")
    public Object addDetailList(OutorderDetailParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = outorderDetailService.detailList(page, paramCondition);
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
        JSONObject result = outorderService.review(id);
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
        JSONObject result = outorderService.cancelReview(id);
        return result;
    }

    /**
     * 取消出库订单
     */
    @RequestMapping(value = "/removeOutorder")
    @ResponseBody
    public Object removeOutorder(@RequestParam Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = outorderService.removeOutorder(id);
        return result;
    }
}


