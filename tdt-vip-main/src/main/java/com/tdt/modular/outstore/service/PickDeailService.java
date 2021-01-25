package com.tdt.modular.outstore.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.core.common.constant.Constants;
import com.tdt.modular.base.entity.Commodity;
import com.tdt.modular.base.entity.Locator;
import com.tdt.modular.base.service.CommodityService;
import com.tdt.modular.base.service.LocatorService;
import com.tdt.modular.outstore.entity.Pick;
import com.tdt.modular.outstore.entity.PickDetail;
import com.tdt.modular.outstore.entity.PicktaskDetail;
import com.tdt.modular.outstore.mapper.PickDetailMapper;
import com.tdt.modular.outstore.model.params.PickDetailParam;
import com.tdt.modular.outstore.model.result.PickDetailResult;
import com.tdt.modular.repertory.entity.StockLog;
import com.tdt.modular.repertory.service.StockService;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 拣货明细服务类
 */
@Service
public class PickDeailService extends ServiceImpl<PickDetailMapper, PickDetail> {

    @Autowired
    private PickService pickService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private LocatorService locatorService;

    @Autowired
    private PicktaskDetailService picktaskDetailService;

    @Autowired
    private StockService stockService;

    @Transactional(rollbackFor = Exception.class)
    public void add(PickDetailParam param){
        PickDetail entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addDetail(PickDetail pickDetail) {
        JSONObject result = new JSONObject();
        Pick pick = pickService.getOne(new QueryWrapper<Pick>().eq("id",pickDetail.getPid()));
        //判断拣货订单状态是否不为已审核
        if (!pick.getState().equals(Constants.PickState.PICKED)){
            Commodity commodity = commodityService.getOne(new QueryWrapper<Commodity>().eq("bar",pickDetail.getCommoditybar()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
            //判断商品编码是否存在且商品编码属于当前仓库
            if (commodity!=null){
                Locator locator = locatorService.getOne(new QueryWrapper<Locator>().eq("code",pickDetail.getLocatorcode()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                //判断货位编码是否存在且货位编码属于当前仓库
                if (locator!=null){
                    PicktaskDetail picktaskDetail = picktaskDetailService.getOne(new QueryWrapper<PicktaskDetail>().eq("pid",pick.getPicktaskid()).eq("commoditybar",pickDetail.getCommoditybar()).eq("locatorcode",pickDetail.getLocatorcode()));
                    //判断输入的商品编码和货位编码与拣货任务明细中是否匹配
                    if (picktaskDetail!=null){
                        int sum = 0;
                        List<PickDetail> pickDetailList = this.list(new QueryWrapper<PickDetail>().eq("pid",pick.getPicktaskid()).eq("commoditybar",pickDetail.getCommoditybar()).eq("locatorcode",pickDetail.getLocatorcode()));
                        for (PickDetail _pickDetail:pickDetailList){
                            sum = sum + _pickDetail.getQty();
                        }
                        //判断已录入此商品和货位上的数量加上当前数量是否超出拣货任务明细的数量
                        if (sum+pickDetail.getQty()<=picktaskDetail.getQty()){
                            //修改拣货状态为拣货中
                            pick.setState(Constants.PickState.PICKING);
                            pickService.updateById(pick);
                            //添加拣货明细数据
                            pickDetail.setCommodityid(commodity.getId());
                            pickDetail.setCommodityname(commodity.getName());
                            pickDetail.setLocatorid(locator.getId());
                            pickDetail.setLocatorname(locator.getName());
                            pickDetail.setCreateid(ShiroKit.getUser().getId());
                            pickDetail.setCreator(ShiroKit.getUser().getName());
                            pickDetail.setCreatetime(new Date());
                            //记录下架的数据
                            StockLog stockLog = stockService.addStockLog(pick.getPickno(),locator.getWarehouseid(),locator.getId(),
                                    locator.getCode(),locator.getName(),commodity.getId(),commodity.getBar(),commodity.getName(),
                                    pickDetail.getQty()*(-1),Constants.StockLogType.PUTOUTSTOCK);
                            pickDetail.setStocklogid(stockLog.getId());
                            this.save(pickDetail);
                        } else {
                            result.put("code", 500);
                            result.put("message", "此货位上的此商品在拣货任务明细中的数量为"+picktaskDetail.getQty()+",请勿超出");
                            return result;
                        }
                    } else {
                        result.put("code", 500);
                        result.put("message", "商品编码或货位编码与拣货任务不匹配，请重新核对");
                        return result;
                    }
                } else {
                    result.put("code", 500);
                    result.put("message", "此货位编码不存在或货位编码不属于当前仓库，请重新输入");
                    return result;
                }
            } else {
                result.put("code", 500);
                result.put("message", "此商品编码不存在或商品编码不属于当前仓库，请重新输入");
                return result;
            }
        } else {
            result.put("code", 500);
            result.put("message", "此拣货订单已完成，无法录入明细");
            return result;
        }
        result.put("code", 200);
        result.put("message", "拣货明细录入成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(PickDetailParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PickDetailParam param){
        PickDetail oldEntity = getOldEntity(param);
        PickDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, PickDetailParam paramCondition) {
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public PickDetailResult findBySpec(PickDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<PickDetailResult> findListBySpec(PickDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(PickDetailParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(PickDetailParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private PickDetail getOldEntity(PickDetailParam param) {
        return this.getById(getKey(param));
    }

    private PickDetail getEntity(PickDetailParam param) {
        PickDetail entity = new PickDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
