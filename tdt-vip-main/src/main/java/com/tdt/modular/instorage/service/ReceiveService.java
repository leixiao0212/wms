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
import com.tdt.core.util.DateUtil;
import com.tdt.modular.instorage.entity.Purchase;
import com.tdt.modular.instorage.entity.Receive;
import com.tdt.modular.instorage.mapper.ReceiveMapper;
import com.tdt.modular.instorage.model.params.ReceiveParam;
import com.tdt.modular.instorage.model.result.ReceiveResult;
import com.tdt.modular.storage.entity.Allocation;
import com.tdt.modular.storage.service.AllocationService;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 接货管理服务类
 */
@Service
public class ReceiveService extends ServiceImpl<ReceiveMapper, Receive> {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private AllocationService allocationService;

    @Transactional(rollbackFor = Exception.class)
    public void add(ReceiveParam param){
        Receive entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addhead(Receive receive) {
        JSONObject result = new JSONObject();
        Receive temp = null;
        if (receive.getPurchaseno()!=null&&!receive.getPurchaseno().equals("")) {
            temp=this.baseMapper.selectOne(new QueryWrapper<Receive>().eq("purchaseno", receive.getPurchaseno()).or().eq("receiveno", receive.getPurchaseno()).or().eq("allocationno", receive.getPurchaseno()));
        }//判断接货记录里面是否有此接货记录
        if (temp!=null){
            //判断订单状态是否审核
            if (temp.getState().equals(Constants.ReceiveState.auditoed)) {
                result.put("code", 500);
                result.put("message", "此订单已审核，无法接货");
            } else {
                result.put("code", 200);
                result.put("message", "此订单已接货");
                result.put("receive", temp);
            }
        } else {
            //判断是否为无单接货
            if (receive.getPurchaseno().equals("")){
                receive.setReceiveno("IR" + DateUtil.getAllMsTime());
                receive.setState(Constants.ReceiveState.NEW);
                receive.setWarehouseid(ShiroKit.getUser().getWarehouseId());
                receive.setCreateid(ShiroKit.getUser().getId());
                receive.setCreator(ShiroKit.getUser().getName());
                receive.setCreatetime(new Date());
                this.save(receive);
                result.put("code", 200);
                result.put("message", "订单接货成功");
                result.put("receive", receive);
            } else {
                Allocation allocation = allocationService.getOne(new QueryWrapper<Allocation>().eq("allocationno", receive.getPurchaseno()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                Purchase purchase = purchaseService.getOne(new QueryWrapper<Purchase>().eq("purchaseno", receive.getPurchaseno()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                //判断采购订单是否存在且是当前仓库的
                if (purchase != null){
                    //判断此采购订单是否已被审核
                    if (purchase.getState().equals(Constants.PurchaseState.REVIEWED)) {
                        receive.setReceiveno("IR" + receive.getPurchaseno());
                        receive.setPurchaseid(purchase.getId());
                        receive.setState(Constants.ReceiveState.NEW);
                        receive.setWarehouseid(ShiroKit.getUser().getWarehouseId());
                        receive.setCreateid(ShiroKit.getUser().getId());
                        receive.setCreator(ShiroKit.getUser().getName());
                        receive.setCreatetime(new Date());
                        this.save(receive);
                        result.put("code", 200);
                        result.put("message", "订单接货成功");
                        result.put("receive", receive);
                    } else {
                        result.put("code", 500);
                        result.put("message", "采购订单还未审核，无法进行接货");
                    }
                    //判断调拨单号是否存在且是当前仓库的
                } else if (allocation != null){
                    //判断调拨状态是否为已审核
                    if (allocation.getState().equals(Constants.AllocationState.REVIEW)) {
                        receive.setReceiveno("IR" + receive.getPurchaseno());
                        receive.setAllocationid(allocation.getId());
                        receive.setState(Constants.ReceiveState.NEW);
                        receive.setWarehouseid(ShiroKit.getUser().getWarehouseId());
                        receive.setCreateid(ShiroKit.getUser().getId());
                        receive.setCreator(ShiroKit.getUser().getName());
                        receive.setCreatetime(new Date());
                        this.save(receive);
                        result.put("code", 200);
                        result.put("message", "订单接货成功");
                        result.put("receive", receive);
                    } else {
                        result.put("code", 500);
                        result.put("message", "此调拨订单还未审核，无法进行接货");
                    }
                } else {
                    result.put("code", 500);
                    result.put("message", "此单号不存在或此单号不属于当前仓库,请核对");
                }
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(ReceiveParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(ReceiveParam receiveParam){
        this.baseMapper.deleteAll(receiveParam.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ReceiveParam param){
        Receive oldEntity = getOldEntity(param);
        Receive newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        newEntity.setUpdateid(ShiroKit.getUser().getId());
        newEntity.setUpdator(ShiroKit.getUser().getName());
        newEntity.setUpdatetime(new Date());
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, ReceiveParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public ReceiveResult findBySpec(ReceiveParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<ReceiveResult> findListBySpec(ReceiveParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(ReceiveParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject review(Long id) {
        JSONObject result = new JSONObject();
        Receive receive = this.baseMapper.selectById(id);
        //判断接获订单是否存在
        if(receive != null){
            //判断接货订单状态是否为接货中
            if(receive.getState().equals(Constants.ReceiveState.receiving)){
                receive.setState(Constants.ReceiveState.auditoed);
                receive.setAuditid(ShiroKit.getUser().getId());
                receive.setAuditor(ShiroKit.getUser().getName());
                receive.setAudittime(new Date());
                this.baseMapper.updateById(receive);
                result.put("code", 200);
                result.put("message", "审核成功");
            }else{
                result.put("code", 500);
                result.put("message", "此订单未完成接货,无法审核");
            }
        }else {
            result.put("code", 500);
            result.put("message", "订单不存在");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject cancelReview(Long id) {
        JSONObject result = new JSONObject();
        Receive receive = this.baseMapper.selectById(id);
        if(receive != null){
            if(receive.getState().equals(Constants.ReceiveState.auditoed)){
                receive.setState(Constants.ReceiveState.receiving);
                this.baseMapper.updateById(receive);
                result.put("code", 200);
                result.put("message", "撤销审核成功");
            }else{
                result.put("code", 500);
                result.put("message", "此订单尚未审核,不用撤销审核");
            }
        }else {
            result.put("code", 500);
            result.put("message", "订单不存在");
        }
        return result;
    }

    private Serializable getKey(ReceiveParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Receive getOldEntity(ReceiveParam param) {
        return this.getById(getKey(param));
    }

    private Receive getEntity(ReceiveParam param) {
        Receive entity = new Receive();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
