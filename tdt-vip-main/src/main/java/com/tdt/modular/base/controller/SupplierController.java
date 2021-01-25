package com.tdt.modular.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.modular.base.entity.Supplier;
import com.tdt.modular.base.model.params.SupplierParam;
import com.tdt.modular.base.service.SupplierService;
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
 * @Date 2019-08-19 12:00:01
 */
@Controller
@RequestMapping("/supplier")
public class SupplierController extends BaseController {

    private String PREFIX = "/modular/base/supplier";

    @Autowired
    private SupplierService supplierService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/supplier.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/supplier_add.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/supplier_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(SupplierParam supplierParam) {
        this.supplierService.add(supplierParam);
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
    public ResponseData editItem(SupplierParam supplierParam) {
        this.supplierService.update(supplierParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(SupplierParam supplierParam) {
        this.supplierService.delete(supplierParam);
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
    public ResponseData detail(SupplierParam supplierParam) {
        Supplier detail = this.supplierService.getById(supplierParam.getId());
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
    public Object list(SupplierParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = supplierService.list(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

}


