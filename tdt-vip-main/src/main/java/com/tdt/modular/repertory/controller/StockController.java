package com.tdt.modular.repertory.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.model.params.StockParam;
import com.tdt.modular.repertory.service.StockService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.repertory.wrapper.StockWrapper;
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
 * @Date 2019-08-28 15:50:09
 */
@Controller
@RequestMapping("/stock")
public class StockController extends BaseController {

    private String PREFIX = "/modular/repertory/stock";

    @Autowired
    private StockService stockService;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/stock.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/stock_add.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/stock_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(StockParam stockParam) {
        this.stockService.add(stockParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-08-28
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(StockParam stockParam) {
        this.stockService.update(stockParam);
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
    public ResponseData delete(StockParam stockParam) {
        this.stockService.delete(stockParam);
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
    public ResponseData detail(StockParam stockParam) {
        Stock detail = this.stockService.getById(stockParam.getId());
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
    public Object list(StockParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = stockService.list(page, paramCondition);
        page.setRecords(new StockWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

}


