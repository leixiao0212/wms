package com.tdt.modular.storage.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.modular.base.model.params.LocatorParam;
import com.tdt.modular.base.service.LocatorService;
import com.tdt.modular.base.wrapper.LocatorWrapper;
import com.tdt.modular.storage.entity.Inventory;
import com.tdt.modular.storage.entity.InventoryLocatorDetail;
import com.tdt.modular.storage.model.params.InventoryLocatorParam;
import com.tdt.modular.storage.model.params.InventoryParam;
import com.tdt.modular.storage.service.InventoryLocatorDetailService;
import com.tdt.modular.storage.service.InventoryLocatorService;
import com.tdt.modular.storage.service.InventoryService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.storage.wrapper.InventoryLocatorWrapper;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.sys.modular.system.warpper.DeptWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author chenc
 * @Date 2019-08-28 09:37:05
 */
@Controller
@RequestMapping("/inventory")
public class InventoryController extends BaseController {

    private String PREFIX = "/modular/storage/inventory";

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private LocatorService locatorService;

    @Autowired
    private InventoryLocatorDetailService inventoryLocatorDetailService;

    @Autowired
    private InventoryLocatorService inventoryLocatorService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/inventory.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/inventory_add.html";
    }

    /**
     * 盘点明细页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/inventoryDetail_add")
    public String inventoryDetailAdd(int id, String inventoryno, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("inventoryno", inventoryno);
        return PREFIX + "/inventoryDetail_add.html";
    }

    /**
     * 跳转到盘点结果页面
     */
    @RequestMapping(value = "/inventory_report")
    public String inventoryDetailReport(Model model,Integer inventoryId) {
        model.addAttribute("inventoryId", inventoryId);
        return PREFIX + "/inventory_report.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/inventory_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(InventoryParam inventoryParam) {
        this.inventoryService.add(inventoryParam);
        return ResponseData.success();
    }

    /**
     * 新增盘点记录
     */
    @RequestMapping(value = "/addInventory")
    @ResponseBody
    public Object addInventory(String locatorCodes) {
        if (ToolUtil.isEmpty(locatorCodes)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = inventoryService.addInventory(locatorCodes);
        return result;
    }

    /**
     * 盘点全部货位
     */
    @RequestMapping(value = "/addAll")
    @ResponseBody
    public Object addAll() {
        JSONObject result = inventoryService.addAll();
        return result;
    }

    /**
     * 新增盘点明细
     */
    @RequestMapping(value = "/addDetail")
    @ResponseBody
    public Object addDetail(InventoryLocatorDetail inventoryLocatorDetail, String inventoryno,BindingResult result) {
        if (ToolUtil.isOneEmpty(inventoryno,inventoryLocatorDetail.getLocatorcode(),inventoryLocatorDetail.getCommoditybar(),inventoryLocatorDetail.getQty())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject resultObj = inventoryLocatorDetailService.addDetail(inventoryLocatorDetail,inventoryno);
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
    public ResponseData editItem(InventoryParam inventoryParam) {
        this.inventoryService.update(inventoryParam);
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
    public ResponseData delete(InventoryParam inventoryParam) {
        this.inventoryService.delete(inventoryParam);
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
    public ResponseData detail(InventoryParam inventoryParam) {
        Inventory detail = this.inventoryService.getById(inventoryParam.getId());
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
    public Object list(InventoryLocatorParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = inventoryLocatorService.list(page, paramCondition);
        page.setRecords(new InventoryLocatorWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询货位详情列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @ResponseBody
    @RequestMapping("/locatorList")
    public Object locatorList(LocatorParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = locatorService.list(page, paramCondition);
        page.setRecords(new LocatorWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询货位明细详情列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @ResponseBody
    @RequestMapping("/inventoryDetailList")
    public Object inventoryDetailList(InventoryLocatorParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = inventoryLocatorService.detailList(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 结束盘点
     */
    @RequestMapping(value = "/endInventory")
    @ResponseBody
    public JSONObject endInventory(Integer inventoryId) {
        if (ToolUtil.isEmpty(inventoryId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = this.inventoryService.endInventory(inventoryId);
        return result;
    }

    /**
     * 盘点明细列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @ResponseBody
    @RequestMapping("/inventory_detail_list")
    public Object inventoryDetailLists(InventoryLocatorParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = inventoryLocatorService.inventoryDetailLists(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 盘盈列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @ResponseBody
    @RequestMapping("/inventory_profit_list")
    public Object inventoryProfitList(Long pid) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = inventoryService.inventoryProfitList(page, pid);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 盘亏列表
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @ResponseBody
    @RequestMapping("/inventory_loss_list")
    public Object inventoryLossList(Long pid) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = inventoryService.inventoryLossList(page, pid);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }
}


