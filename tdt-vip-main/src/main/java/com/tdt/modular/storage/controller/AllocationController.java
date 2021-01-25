package com.tdt.modular.storage.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.core.common.constant.Constants;
import com.tdt.core.util.SequenceBuilder;
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.service.WarehouseService;
import com.tdt.modular.storage.entity.Allocation;
import com.tdt.modular.storage.entity.AllocationDetail;
import com.tdt.modular.storage.model.params.AllocationDetailParam;
import com.tdt.modular.storage.model.params.AllocationParam;
import com.tdt.modular.storage.service.AllocationDetailService;
import com.tdt.modular.storage.service.AllocationService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.storage.wrapper.AllocationWrapper;
import com.tdt.sys.core.constant.factory.ConstantFactory;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.sys.core.shiro.ShiroKit;
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
 * @Date 2019-08-28 09:37:05
 */
@Controller
@RequestMapping("/allocation")
public class AllocationController extends BaseController {

    private String PREFIX = "/modular/storage/allocation";

    @Autowired
    private AllocationService allocationService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private AllocationDetailService allocationDetailService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("")
    public String index(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("allocation_state");
        model.addAttribute("dicts", dicts);
        return PREFIX + "/allocation.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/add")
    public String add(Model model) {
        List<Warehouse> warehouses = (List<Warehouse>) warehouseService.listByIds(ShiroKit.getUser().getWarehouses());
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("allocationno", SequenceBuilder.generateNo(Constants.PREFIX_NO.ALLOCATION_NO));
        return PREFIX + "/allocation_add.html";
    }

    /**
     * 调拨明细新增页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/allocationDetail_add")
    public String allocationDetailAdd(Integer id, String allocationno, Model model){
        model.addAttribute("id", id);
        model.addAttribute("allocationno", allocationno);
        return PREFIX + "/allocationDetail_add.html";
    }

    /**
     * 查看调拨明细页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/allocation_detail")
    public String allocationDetail(Integer id,Model model){
        model.addAttribute("id", id);
        return PREFIX + "/allocationDetail.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/allocation_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(AllocationParam allocationParam) {
        this.allocationService.add(allocationParam);
        return ResponseData.success();
    }

    /**
     * 新增调拨信息
     */
    @RequestMapping(value = "/addHead")
    @ResponseBody
    public Object add(@Valid Allocation allocation, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject resultObj = this.allocationService.addHead(allocation);
        return resultObj;
    }

    /**
     * 新增调拨明细信息
     */
    @RequestMapping(value = "/addDetail")
    @ResponseBody
    public JSONObject addDetail(AllocationDetail allocationDetail,String locatorcode) {
        if (ToolUtil.isOneEmpty(allocationDetail.getCommoditybar(),allocationDetail.getPid(),allocationDetail.getQty())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = this.allocationDetailService.addDetail(allocationDetail,locatorcode);
        return result;
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(AllocationParam allocationParam) {
        this.allocationService.update(allocationParam);
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
    public ResponseData delete(AllocationParam allocationParam) {
        this.allocationService.delete(allocationParam);
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
    public ResponseData detail(AllocationParam allocationParam) {
        Allocation detail = this.allocationService.getById(allocationParam.getId());
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
    public Object list(AllocationParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = allocationService.list(page, paramCondition);
        page.setRecords(new AllocationWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询调拨新增明细列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @ResponseBody
    @RequestMapping("/allocationDetailList")
    public Object allocationDetailList(AllocationDetailParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = allocationDetailService.list(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询调拨明细列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @ResponseBody
    @RequestMapping("/detailList")
    public Object detailList(@RequestParam("id") Long allocationId) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = allocationDetailService.detailList(page, allocationId);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 调拨结束
     */
    @RequestMapping(value = "/finish")
    @ResponseBody
    public Object finish(Integer pid) {
        return this.allocationService.finish(pid);
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
        JSONObject result = this.allocationService.review(id);
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
        JSONObject result = this.allocationService.cancelReview(id);
        return result;
    }

}


