package com.tdt.modular.outstore.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tdt.core.common.constant.Constants;
import com.tdt.modular.outstore.entity.Outorder;
import com.tdt.modular.outstore.entity.OutorderDetail;
import com.tdt.modular.outstore.entity.OutorderTag;
import com.tdt.modular.outstore.mapper.OutorderMapper;
import com.tdt.modular.outstore.service.*;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 控制器
 *
 * @author chenc
 * @Date 2019-09-10 16:41:50
 */
@Controller
@RequestMapping("/wellen")
public class WellenController extends BaseController {

    @Autowired
    private WellenService wellenService;

    @Resource
    private OutorderMapper outorderMapper;

    @Autowired
    private OutorderDetailService outorderDetailService;

    @Autowired
    private OutorderTagService outorderTagService;

    private String PREFIX = "/modular/outstore/wellen";

    /**
     * 跳转到主页面
     *
     * @author chenc
     * @Date 2019-09-10
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/wellen.html";
    }

    /**
     * 生成波次
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object addInventory(String checkValue,Integer qty) {
        if (ToolUtil.isEmpty(checkValue)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        JSONObject result = wellenService.add(checkValue,qty);
        return result;
    }

    /**
     * 生成出库订单标签
     */
    @RequestMapping(value = "/increase")
    @ResponseBody
    public Object increase() {
        JSONObject result = new JSONObject();
        //查询状态为已审核的且未生成标签的出库订单列表
        List<Outorder> outorderList = outorderMapper.notCreateOutorderTaglist(ShiroKit.getUser().getWarehouseId());
        List<OutorderTag> outorderTagList=new ArrayList<OutorderTag>();
        for (Outorder outorder:outorderList){
            List<OutorderDetail> outorderDetailList = outorderDetailService.list(new QueryWrapper<OutorderDetail>().eq("pid",outorder.getId()));
            OutorderTag outorderTag = new OutorderTag();
            outorderTag.setPid(outorder.getId());
            outorderTag.setOutorderno(outorder.getOutorderno());
            outorderTag.setWarehouseid(outorder.getWarehouseid());
            outorderTag.setProvince(outorder.getProvince());
            outorderTag.setCity(outorder.getCity());
            outorderTag.setCounty(outorder.getCounty());
            //判断出库订单是否只有一个商品
            if (outorderDetailList.size()==1){
                //如果当前出库订单只有一件商品，判断它的数量是否为1
                OutorderDetail outorderDetail = outorderDetailList.get(0);
                if (outorderDetail.getQty()==1){
                    outorderTag.setType(Constants.OutorderTagType.ONEANDONE);
                } else {
                    outorderTag.setType(Constants.OutorderTagType.ONEANDANY);
                }
            } else {
                int flag = 0;
                //如果当前出库订单有多个商品，判断每个商品的数量是否都为1
                for (OutorderDetail outorderDetail:outorderDetailList){
                    if (outorderDetail.getQty()!=1){
                        flag = 1;
                    }
                }
                if (flag!=0){
                    outorderTag.setType(Constants.OutorderTagType.ANYANDONE);
                } else {
                    outorderTag.setType(Constants.OutorderTagType.ANYANDANY);
                }
            }
            outorderTagList.add(outorderTag);
        }
        outorderTagService.saveBatch(outorderTagList);
        result.put("code", 200);
        result.put("message","生成出库订单标签成功");
        return result;
    }

}


