package com.tdt.modular.base.controller;

import cn.afterturn.easypoi.entity.vo.MapExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.base.entity.Commodity;
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.model.params.CommodityParam;
import com.tdt.modular.base.service.CommodityService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.base.service.WarehouseService;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.sys.core.properties.TDTProperties;
import com.tdt.sys.core.shiro.ShiroKit;
import com.tdt.sys.modular.system.warpper.DeptWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


/**
 * 控制器
 *
 * @author chenc
 * @Date 2019-08-16 14:41:57
 */
@Controller
@RequestMapping("/commodity")
public class CommodityController extends BaseController {

    private String PREFIX = "/modular/base/commodity";

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private TDTProperties tdtProperties;

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-08-16
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/commodity.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-08-16
     */
    @RequestMapping("/add")
    public String add(Model model) {
        List<Warehouse> warehouses = warehouseService.list();
        model.addAttribute("warehouses", warehouses);
        return PREFIX + "/commodity_add.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-08-16
     */
    @RequestMapping("/edit")
    public String edit(Model model) {
        List<Warehouse> warehouses = warehouseService.list();
        model.addAttribute("warehouses", warehouses);
        return PREFIX + "/commodity_edit.html";
    }

    /**
     * 导入页面
     *
     * @author chenc
     * @Date 2019-08-16
     */
    @RequestMapping("/commodity_import")
    public String commodity_import() {
        return PREFIX + "/commodity_import.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-08-16
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(CommodityParam commodityParam) {
        this.commodityService.add(commodityParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-08-16
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(CommodityParam commodityParam) {
        this.commodityService.update(commodityParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author chenc
     * @Date 2019-08-16
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(CommodityParam commodityParam) {
        this.commodityService.delete(commodityParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author chenc
     * @Date 2019-08-16
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(CommodityParam commodityParam) {
        Commodity detail = this.commodityService.getById(commodityParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author chenc
     * @Date 2019-08-16
     */
    @ResponseBody
    @RequestMapping("/list")
    public Object list(CommodityParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = commodityService.list(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 下载模板
     * @throws IOException
     */
    @RequestMapping(value = "/download")
    public void exportOrder(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) throws IOException {
        //初始化表头
        List<ExcelExportEntity> entity = new ArrayList<>();
        entity.add(new ExcelExportEntity("编号", "id"));
        entity.add(new ExcelExportEntity("条码", "bar"));
        entity.add(new ExcelExportEntity("商品名", "name"));
        entity.add(new ExcelExportEntity("别名", "alias"));
        entity.add(new ExcelExportEntity("长", "length"));
        entity.add(new ExcelExportEntity("宽", "width"));
        entity.add(new ExcelExportEntity("高", "height"));
        entity.add(new ExcelExportEntity("价值", "worth"));
        entity.add(new ExcelExportEntity("净重", "nweight"));
        entity.add(new ExcelExportEntity("毛重", "gweight"));
        entity.add(new ExcelExportEntity("成分", "component"));
        entity.add(new ExcelExportEntity("品类", "category"));
        entity.add(new ExcelExportEntity("有效天数", "validday"));
        entity.add(new ExcelExportEntity("备注", "remarks"));

        ExportParams params = new ExportParams("商品导入模板", "商品表", ExcelType.XSSF);
        modelMap.put(MapExcelConstants.MAP_LIST, new ArrayList<>());
        modelMap.put(MapExcelConstants.ENTITY_LIST, entity);
        modelMap.put(MapExcelConstants.PARAMS, params);
        modelMap.put(MapExcelConstants.FILE_NAME, "商品导入模板");
        PoiBaseView.render(modelMap, request, response, MapExcelConstants.EASYPOI_MAP_EXCEL_VIEW);
    }

    /**
     * 上传excel填报
     */
    @RequestMapping("/uploadExcel")
    @ResponseBody
    public ResponseData uploadExcel(@RequestPart("file") MultipartFile file, HttpServletRequest request) {
        String name = file.getOriginalFilename();
        request.getSession().setAttribute("upFile", name);
        String fileSavePath = tdtProperties.getFileUploadPath();
        try {
            file.transferTo(new File(fileSavePath + name));
        } catch (Exception e) {
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }
        if (name != null) {
            File newfile = new File(fileSavePath + name);
            try {
                ImportParams params = new ImportParams();
                params.setTitleRows(1);
                params.setHeadRows(1);
                List<Commodity> result = ExcelImportUtil.importExcel(newfile, Commodity.class, params);

                LayuiPageInfo returns = new LayuiPageInfo();
                CommodityParam commodityParam = new CommodityParam();
                for (int i=0;i<result.size();i++){
                    Commodity commodity = result.get(i);
                    BeanUtils.copyProperties(commodity,commodityParam);
                    commodityService.add(commodityParam);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseData.success(0, "上传成功", null);
    }

}


