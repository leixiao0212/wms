package com.tdt.modular.instorage.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.core.common.constant.Constants;
import com.tdt.modular.base.entity.Supplier;
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.mapper.SupplierMapper;
import com.tdt.modular.base.mapper.WarehouseMapper;
import com.tdt.modular.instorage.entity.Purchase;
import com.tdt.modular.instorage.entity.PurchaseDetail;
import com.tdt.modular.instorage.mapper.PurchaseDetailMapper;
import com.tdt.modular.instorage.mapper.PurchaseMapper;
import com.tdt.modular.instorage.model.params.PurchaseDetailParam;
import com.tdt.modular.instorage.model.params.PurchaseParam;
import com.tdt.modular.instorage.model.result.PurchaseResult;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 采购订单服务类
 */
@Service
public class PurchaseService extends ServiceImpl<PurchaseMapper, Purchase> {

    @Resource
    private SupplierMapper supplierMapper;

    @Resource
    private WarehouseMapper warehouseMapper;

    @Resource
    private PurchaseDetailMapper purchaseDetailMapper;

    @Transactional(rollbackFor = Exception.class)
    public Purchase add(PurchaseParam param){
        Purchase entity = getEntity(param);
        Supplier supplier = supplierMapper.selectById(param.getSupplierid());
        entity.setSuppliername(supplier.getName());
        entity.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        Warehouse warehouse = warehouseMapper.selectById(ShiroKit.getUser().getWarehouseId());
        entity.setWarehousename(warehouse.getName());
        entity.setState(Constants.WarehousingState.UNFINISH);
        entity.setCreateid(ShiroKit.getUser().getId());
        entity.setCreator(ShiroKit.getUser().getName());
        entity.setCreatetime(new Date());
        this.save(entity);
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(PurchaseParam param){
        purchaseDetailMapper.delete(new QueryWrapper<PurchaseDetail>().eq("pid",param.getId()));
        this.removeById(param.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(PurchaseParam purchaseParam){
        this.baseMapper.deleteAll(purchaseParam.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PurchaseParam param){
        Purchase oldEntity = getOldEntity(param);
        Purchase newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        Supplier supplier = supplierMapper.selectById(param.getSupplierid());
        newEntity.setSuppliername(supplier.getName());
        Warehouse warehouse = warehouseMapper.selectById(param.getWarehouseid());
        newEntity.setWarehousename(warehouse.getName());
        newEntity.setUpdateid(ShiroKit.getUser().getId());
        newEntity.setUpdator(ShiroKit.getUser().getName());
        newEntity.setUpdatetime(new Date());
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, PurchaseParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> detailList(Page page, PurchaseDetailParam paramCondition) {
        return purchaseDetailMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject finish(PurchaseDetail purchaseDetail) {
        JSONObject resultObj = new JSONObject();
        Purchase purchase = this.baseMapper.selectById(purchaseDetail.getPid());
        //如果采购订单还未审核,将订单状态设置为待审核
        if(!purchase.getState().equals(Constants.PurchaseState.REVIEWED)){
            //计算此时商品总数量添加到主表中
            int sum = 0;
            List<PurchaseDetail> purchaseDetailList = purchaseDetailMapper.selectList(new QueryWrapper<PurchaseDetail>().eq("pid",purchaseDetail.getPid()));
            for (PurchaseDetail _purchaseDetail:purchaseDetailList){
                sum = sum + _purchaseDetail.getQty();
            }
            purchase.setCommoditynum(sum);
            this.updateById(purchase);
            resultObj.put("code", 200);
            resultObj.put("message", "采购订单录入完成");
        }else{
            resultObj.put("code", 500);
            resultObj.put("message", "此采购订单已经结束录入,不要重复操作");
        }
        return resultObj;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject review(Integer id) {
        JSONObject result = new JSONObject();
        Purchase purchase = this.baseMapper.selectById(id);
        if(purchase != null){
            //订单状态是否为待审核
            if(purchase.getState().equals(Constants.PurchaseState.UNREVIEW)){
                purchase.setState(Constants.PurchaseState.REVIEWED);
                purchase.setAuditid(ShiroKit.getUser().getId());
                purchase.setAuditor(ShiroKit.getUser().getName());
                purchase.setAudittime(new Date());
                this.baseMapper.updateById(purchase);
                result.put("code", 200);
                result.put("message", "审核成功");
                //订单状态是否录入完成
            }else if(purchase.getState().equals(Constants.PurchaseState.UNFINISH)){
                result.put("code", 500);
                result.put("message", "采购订单未录入完成,不能审核");
            }else{
                result.put("code", 500);
                result.put("message", "此订单已经审核,请勿重复审核");
            }
        }else {
            result.put("code", 500);
            result.put("message", "订单不存在");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject cancelReview(Integer id) {
        JSONObject result = new JSONObject();
        Purchase purchase = this.baseMapper.selectById(id);
        if(purchase != null){
            //订单状态是否已审核
            if(purchase.getState().equals(Constants.PurchaseState.UNREVIEW)){
                result.put("code", 500);
                result.put("message", "此订单尚未审核,不用撤销审核");
            }else if(purchase.getState().equals(Constants.PurchaseState.REVIEWED)){
                purchase.setState(Constants.PurchaseState.UNREVIEW);
                this.updateById(purchase);
                result.put("code", 200);
                result.put("message", "撤销审核成功");
            } else {
                result.put("code", 500);
                result.put("message", "此订单尚未录入完成,不用撤销审核");
            }
        }else {
            result.put("code", 500);
            result.put("message", "订单不存在");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public PurchaseResult findBySpec(PurchaseParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<PurchaseResult> findListBySpec(PurchaseParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(PurchaseParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(PurchaseParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Purchase getOldEntity(PurchaseParam param) {
        return this.getById(getKey(param));
    }

    private Purchase getEntity(PurchaseParam param) {
        Purchase entity = new Purchase();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
