package com.tdt.modular.base.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.node.ZTreeNode;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.model.params.WarehouseParam;
import com.tdt.modular.base.service.WarehouseService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.sys.modular.system.warpper.DeptWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/warehouse")
public class WarehouseController extends BaseController {

    private String PREFIX = "/modular/base/warehouse";

    @Autowired
    private WarehouseService warehouseService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/warehouse.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/warehouse_add.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/warehouse_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-08-19
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(WarehouseParam warehouseParam) {
        this.warehouseService.add(warehouseParam);
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
    public ResponseData editItem(WarehouseParam warehouseParam) {
        this.warehouseService.update(warehouseParam);
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
    public ResponseData delete(WarehouseParam warehouseParam) {
        this.warehouseService.delete(warehouseParam);
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
    public ResponseData detail(WarehouseParam warehouseParam) {
        Warehouse detail = this.warehouseService.getById(warehouseParam.getId());
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
    public Object list(WarehouseParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = warehouseService.list(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }
    /**
     * 获取角色的菜单列表
     *
     * @author thedreamtree
     * @Date 2018/12/23 5:54 PM
     */
    @RequestMapping(value = "/warehouseTreeListByDeptId/{deptId}")
    @ResponseBody
    public List<ZTreeNode> warehouseTreeListByDeptId(@PathVariable Long deptId) {
        if (ToolUtil.isEmpty(deptId)) {
            return this.warehouseService.warehouseTreeListByDeptIds(0l);
        } else {
            return this.warehouseService.warehouseTreeListByDeptIds(deptId);
        }
    }
}


