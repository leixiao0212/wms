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
import com.tdt.modular.base.entity.Commodity;
import com.tdt.modular.base.service.CommodityService;
import com.tdt.modular.instorage.entity.PurchaseDetail;
import com.tdt.modular.instorage.entity.Receive;
import com.tdt.modular.instorage.entity.ReceiveDetail;
import com.tdt.modular.instorage.mapper.ReceiveDetailMapper;
import com.tdt.modular.instorage.model.params.ReceiveDetailParam;
import com.tdt.modular.instorage.model.result.ReceiveDetailResult;
import com.tdt.modular.storage.entity.AllocationDetail;
import com.tdt.modular.storage.service.AllocationDetailService;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 接货管理详情服务类
 */
@Service
public class ReceiveDetailService extends ServiceImpl<ReceiveDetailMapper, ReceiveDetail> {

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private AllocationDetailService allocationDetailService;

    @Autowired
    private CommodityService commodityService;

    @Transactional(rollbackFor = Exception.class)
    public void add(ReceiveDetailParam param){
        ReceiveDetail entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(ReceiveDetailParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ReceiveDetailParam param){
        ReceiveDetail oldEntity = getOldEntity(param);
        ReceiveDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> selectByPid(Page page,Long pid) {
        return this.baseMapper.selectByPid(page,pid);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> detailList(Page page, ReceiveDetailParam paramCondition) {
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addDetail(ReceiveDetail receiveDetail) {
        JSONObject result = new JSONObject();
        Receive receive = receiveService.getById(receiveDetail.getPid());
        //判断接货状态是否为已审核
        if(!receive.getState().equals(Constants.ReceiveState.auditoed)){
            //判断是否为无单接货
            if(receive.getPurchaseid()!=null&&receive.getPurchaseid()!=0){
                PurchaseDetail purchaseDetail = purchaseDetailService.getOne(new QueryWrapper<PurchaseDetail>().eq("pid", receive.getPurchaseid()).eq("commoditybar", receiveDetail.getCommoditybar()));
                if(purchaseDetail==null){
                    result.put("code", 500);
                    result.put("message", "此订单为采购订单接货，商品明细需与采购订单一致，请联系管理员处理");
                    return result;
                }
            } else if (receive.getAllocationid()!=null&&receive.getAllocationid()!=0){
                AllocationDetail allocationDetail = allocationDetailService.getOne(new QueryWrapper<AllocationDetail>().eq("pid",receive.getAllocationid()).eq("commoditybar", receiveDetail.getCommoditybar()));
                if(allocationDetail==null){
                    result.put("code", 500);
                    result.put("message", "此订单为调拨订单接货，商品明细需与调拨订单一致，请联系管理员处理");
                    return result;
                }
            }
            ReceiveDetail receiveDetail1 = this.baseMapper.selectOne(new QueryWrapper<ReceiveDetail>().eq("pid", receiveDetail.getPid()).eq("commoditybar", receiveDetail.getCommoditybar()));
            //判断是否存在接货订单详情
            if(receiveDetail1 != null){
                receiveDetail1.setQty(receiveDetail1.getQty()+receiveDetail.getQty());
                receiveDetail1.setUpdateid(ShiroKit.getUser().getId());
                receiveDetail1.setUpdator(ShiroKit.getUser().getName());
                receiveDetail1.setUpdatetime(new Date());
                this.baseMapper.updateById(receiveDetail1);
                result.put("code", 200);
                result.put("message", "修改采购订单详情成功");
            }else{
                Commodity commodity = commodityService.getOne(new QueryWrapper<Commodity>().eq("bar", receiveDetail.getCommoditybar()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                //判断是否存在此商品编码且商品编码属于当前仓库
                if(commodity == null){
                    result.put("code", 500);
                    result.put("message", "商品编码不存在或此商品编码不属于当前仓库,请重新输入");
                }else{
                    receiveDetail.setCommodityid(commodity.getId());
                    receiveDetail.setCommodityname(commodity.getName());
                    receiveDetail.setCreateid(ShiroKit.getUser().getId());
                    receiveDetail.setCreator(ShiroKit.getUser().getName());
                    receiveDetail.setCreatetime(new Date());
                    this.baseMapper.insert(receiveDetail);
                    receive.setState(Constants.ReceiveState.receiving);
                    receiveService.updateById(receive);
                    result.put("code", 200);
                    result.put("message", "新增接货成功");
                }
            }
        }else{
            result.put("code", 500);
            result.put("message", "此订单已经审核,不能修改");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public ReceiveDetailResult findBySpec(ReceiveDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<ReceiveDetailResult> findListBySpec(ReceiveDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(ReceiveDetailParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(ReceiveDetailParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private ReceiveDetail getOldEntity(ReceiveDetailParam param) {
        return this.getById(getKey(param));
    }

    private ReceiveDetail getEntity(ReceiveDetailParam param) {
        ReceiveDetail entity = new ReceiveDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
