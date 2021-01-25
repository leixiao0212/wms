package com.tdt.modular.base.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.RequestEmptyException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.modular.base.entity.Locator;
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.model.params.LocatorParam;
import com.tdt.modular.base.service.LocatorService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.base.service.WarehouseService;
import com.tdt.modular.base.wrapper.LocatorWrapper;
import com.tdt.sys.core.constant.factory.ConstantFactory;
import com.tdt.sys.modular.system.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author chenc
 * @Date 2019-08-19 12:00:01
 */
@Controller
@RequestMapping("/locator")
public class LocatorController extends BaseController {

    private String PREFIX = "/modular/base/locator";

    @Autowired
    private LocatorService locatorService;

    @Autowired
    private WarehouseService warehouseService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("")
    public String index(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("locator_type");
        model.addAttribute("dicts", dicts);
        return PREFIX + "/locator.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/add")
    public String add(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("locator_type");
        model.addAttribute("dicts", dicts);
        List<Warehouse> warehouses = warehouseService.list();
        model.addAttribute("warehouses", warehouses);
        return PREFIX + "/locator_add.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/edit")
    public String edit(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("locator_type");
        model.addAttribute("dicts", dicts);
        List<Warehouse> warehouses = warehouseService.list();
        model.addAttribute("warehouses", warehouses);
        return PREFIX + "/locator_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(LocatorParam locatorParam) {
        this.locatorService.add(locatorParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public Object editItem(LocatorParam locatorParam) {
        if (ToolUtil.isEmpty(locatorParam)) {
            throw new RequestEmptyException();
        }
        return this.locatorService.update(locatorParam);
    }

    /**
     * 删除接口
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(LocatorParam locatorParam) {
        if (ToolUtil.isEmpty(locatorParam)) {
            throw new RequestEmptyException();
        }
        this.locatorService.delete(locatorParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(LocatorParam locatorParam) {
        Locator detail = this.locatorService.getById(locatorParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @ResponseBody
    @RequestMapping("/list")
    public Object list(LocatorParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = locatorService.list(page, paramCondition);
        page.setRecords(new LocatorWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

}


