package com.tdt.modular.instorage.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.modular.instorage.entity.Warehousing;
import com.tdt.modular.instorage.entity.WarehousingDetail;
import com.tdt.modular.instorage.model.params.WarehousingDetailParam;
import com.tdt.modular.instorage.model.params.WarehousingParam;
import com.tdt.modular.instorage.service.WarehousingDetailService;
import com.tdt.modular.instorage.service.WarehousingService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import com.tdt.modular.instorage.wrapper.WarehousingWrapper;
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
import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author chenc
 * @Date 2019-08-21 10:55:52
 */
@Controller
@RequestMapping("/warehousing")
public class WarehousingController extends BaseController {

    @Autowired
    private WarehousingDetailService warehousingDetailService;

    @Autowired
    private WarehousingService warehousingService;

    private String PREFIX = "/modular/instorage/warehousing";

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("")
    public String index(Model model) {
        List<Dict> dicts = ConstantFactory.me().queryDictByCode("warehousing_state");
        model.addAttribute("dicts", dicts);
        return PREFIX + "/warehousing.html";
    }

    /**
     * 新增页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/warehousing_add.html";
    }

    /**
     * 跳转到添加入库明细页面
     */
    @RequestMapping("/warehousing_detail_add")
    public String warehousingDetailAdd(Long id, String warehousingno, Model model){
        model.addAttribute("id", id);
        model.addAttribute("warehousingno", warehousingno);
        return PREFIX + "/warehousingDetail_add.html";
    }

    /**
     * 查看明细页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/warehousing_detail")
    public String detail(Long id) {
        return PREFIX + "/warehousingDetail.html";
    }

    /**
     * 编辑页面
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/warehousing_edit.html";
    }

    /**
     * 新增接口
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(WarehousingParam warehousingParam) {
        this.warehousingService.add(warehousingParam);
        return ResponseData.success();
    }

    /**
     * 直接入库
     */
    @RequestMapping(value = "/addEnd")
    @ResponseBody
    public JSONObject addEnd(@Valid Warehousing warehousing, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject resultObj = this.warehousingService.addEnd(warehousing);
        return resultObj;
    }

    /**
     * 新增入库
     */
    @RequestMapping(value = "/addHead")
    @ResponseBody
    public JSONObject addHead(@Valid Warehousing warehousing, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        return this.warehousingService.addHead(warehousing);
    }

    /**
     * 添加入库明细信息
     */
    @RequestMapping(value = "/addDetail")
    @ResponseBody
    public JSONObject addDetail(WarehousingDetail warehousingDetail) {
        if (ToolUtil.isOneEmpty(warehousingDetail.getCommoditybar(),warehousingDetail.getPid(),warehousingDetail.getQty())) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = this.warehousingDetailService.addDetail(warehousingDetail);
        return result;
    }

    /**
     * 结束入库
     */
    @RequestMapping(value = "/finish")
    @ResponseBody
    public Object finish(@Valid WarehousingDetail warehousingDetail, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        return this.warehousingService.finish(warehousingDetail);
    }

    /**
     * 编辑接口
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(WarehousingParam warehousingParam) {
        this.warehousingService.update(warehousingParam);
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
    public ResponseData delete(WarehousingParam warehousingParam) {
        this.warehousingService.deleteAll(warehousingParam);
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
    public ResponseData detail(WarehousingParam warehousingParam) {
        Warehousing detail = this.warehousingService.getById(warehousingParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/list")
    public Object list(WarehousingParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = warehousingService.list(page, paramCondition);
        page.setRecords(new WarehousingWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查询入库详情列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/addDetailList")
    public Object addDetailList(WarehousingDetailParam paramCondition) {
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = warehousingDetailService.detailList(page, paramCondition);
        page.setRecords(new DeptWrapper(result).wrap());
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 查看详情列表
     *
     * @author chenc
     * @Date 2019-08-21
     */
    @ResponseBody
    @RequestMapping("/detailList")
    public Object detailList(Long id) {
        Warehousing warehousing = warehousingService.getById(id);
        //获取分页参数
        Page page = LayuiPageFactory.defaultPage();
        //根据条件查询日志
        List<Map<String, Object>> result = warehousingDetailService.selectByPid(page,warehousing.getId());
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
        JSONObject result = warehousingService.review(id);
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
        JSONObject result = warehousingService.cancelReview(id);
        return result;
    }

}


