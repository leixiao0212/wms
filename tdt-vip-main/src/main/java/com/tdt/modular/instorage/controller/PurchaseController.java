package com.tdt.modular.instorage.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.core.common.constant.Constants;
import com.tdt.core.util.SequenceBuilder;
import com.tdt.modular.base.entity.Supplier;
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.service.SupplierService;
import com.tdt.modular.base.service.WarehouseService;
import com.tdt.modular.instorage.entity.Purchase;
import com.tdt.modular.instorage.entity.PurchaseDetail;
import com.tdt.modular.instorage.model.params.PurchaseDetailParam;
import com.tdt.modular.instorage.model.params.PurchaseParam;
import com.tdt.modular.instorage.service.PurchaseDetailService;
import com.tdt.modular.instorage.service.PurchaseService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.instorage.wrapper.PurchaseWrapper;
import com.tdt.sys.core.constant.factory.ConstantFactory;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.sys.core.util.PrintUtil;
import com.tdt.sys.modular.system.entity.Dict;
import com.tdt.sys.modular.system.warpper.DeptWrapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author chenc
 * @Date 2019-08-21 10:55:52
 */
@Controller
@RequestMapping("/purchase")
public class PurchaseController extends BaseController {

    private String PREFIX = "/modular/instorage/purchase";

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private WarehouseService warehouseService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("")
    public String index(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("purchase_state");
        model.addAttribute("dicts", dicts);
        return PREFIX + "/purchase.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/add")
    public String add(Model model) {
        String purchaseno = SequenceBuilder.generateNo(Constants.PREFIX_NO.PURCHASE_NO);
        model.addAttribute("purchaseno", purchaseno);
        List<Supplier> suppliers = supplierService.list();
        model.addAttribute("suppliers", suppliers);
        List<Warehouse> warehouses = warehouseService.list();
        model.addAttribute("warehouses", warehouses);
        return PREFIX + "/purchase_add.html";
    }

    /**
     * 添加采购订单详情页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/purchase_detail_add")
    public String addDetail(Integer id, String purchaseno, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("purchaseno", purchaseno);
        return PREFIX + "/purchaseDetail_add.html";
    }

    /**
     * 采购订单详情页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/purchase_detail")
    public String detail(Long id) {
        return PREFIX + "/purchaseDetail.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/edit")
    public String edit(Model model) {
        List<Supplier> suppliers = supplierService.list();
        model.addAttribute("suppliers", suppliers);
        List<Warehouse> warehouses = warehouseService.list();
        model.addAttribute("warehouses", warehouses);
        return PREFIX + "/purchase_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(PurchaseParam purchaseParam) {
        Purchase purchase=this.purchaseService.add(purchaseParam);
        HashMap<String, Object> map = new HashMap<>();
        map.put("purchase", purchase);
        return ResponseData.success(map);
    }

    /**
     * 添加采购订单明细信息
     */
    @RequestMapping(value = "/addDetail")
    @ResponseBody
    public JSONObject addDetail(PurchaseDetail purchaseDetail) {
        if (ToolUtil.isOneEmpty(purchaseDetail.getCommoditybar(),purchaseDetail.getPid(),purchaseDetail.getQty())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = purchaseDetailService.addDetail(purchaseDetail);
        return result;
    }

    /**
     * 点击添加采购订单详情的结束按钮
     */
    @RequestMapping(value = "/finish")
    @ResponseBody
    public Object finish(@Valid PurchaseDetail purchaseDetail, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        return purchaseService.finish(purchaseDetail);
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(PurchaseParam purchaseParam) {
        this.purchaseService.update(purchaseParam);
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
    public ResponseData delete(PurchaseParam purchaseParam) {
        purchaseService.deleteAll(purchaseParam);
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
    public ResponseData detail(PurchaseParam purchaseParam) {
        Purchase detail = this.purchaseService.getById(purchaseParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询采购订单列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/list")
    public Object list(PurchaseParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = purchaseService.list(page, paramCondition);
        page.setRecords(new PurchaseWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询添加采购订单详情列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/addDetailList")
    public Object addDetailList(PurchaseDetailParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = purchaseDetailService.detailList(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询采购订单详情列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/detailList")
    public Object detailList(Long id) {
        Purchase purchase = purchaseService.getById(id);
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = purchaseDetailService.selectByPid(page,purchase.getId());
        page.setRecords(new DeptWrapper(result).wrap());
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
        JSONObject result = purchaseService.review(id);
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
        JSONObject result = purchaseService.cancelReview(id);
        return result;
    }
    /**
     * 打印产品单据信息表
     */
    @RequestMapping("/prints/{num}")
    @ResponseBody
    public void prints(@PathVariable("num") String purchaseNum, HttpServletResponse response) throws IOException, InvalidFormatException {
        Map result = new HashMap();
        result.put("purchaseNum",123456);
        /*Map<String, Object> funcs = new HashMap<String, Object>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        funcs.put("dateFormat", dateFormat);
        String html = PrintUtils.transformerToHtml("/templates/xls_templates/purchaseOrder.xls", map, funcs, new ArrayList<String>());
        ServletOutputStream out = response.getOutputStream();
        out.write(html.getBytes("utf-8"));
        out.flush();
        out.close();*/

        String html = PrintUtil.transformerToHtml("/templates/xls_templates/purchaseOrder.xls", result);
        ServletOutputStream out = response.getOutputStream();
        out.write(html.getBytes("utf-8"));
        out.flush();
        out.close();
    }
}


