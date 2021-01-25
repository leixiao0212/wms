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
import com.tdt.modular.base.mapper.CommodityMapper;
import com.tdt.modular.instorage.entity.Purchase;
import com.tdt.modular.instorage.entity.PurchaseDetail;
import com.tdt.modular.instorage.mapper.PurchaseDetailMapper;
import com.tdt.modular.instorage.mapper.PurchaseMapper;
import com.tdt.modular.instorage.model.params.PurchaseDetailParam;
import com.tdt.modular.instorage.model.result.PurchaseDetailResult;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 采购订单明细服务类
 */
@Service
public class PurchaseDetailService extends ServiceImpl<PurchaseDetailMapper, PurchaseDetail> {

    @Autowired
    private PurchaseService purchaseService;

    @Resource
    private CommodityMapper commodityMapper;

    @Resource
    private PurchaseDetailMapper purchaseDetailMapper;

    @Resource
    private PurchaseMapper purchaseMapper;

    @Transactional(rollbackFor = Exception.class)
    public void add(PurchaseDetailParam param){
        PurchaseDetail entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(PurchaseDetailParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PurchaseDetailParam param){
        PurchaseDetail oldEntity = getOldEntity(param);
        PurchaseDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, PurchaseDetailParam paramCondition) {
        return purchaseDetailMapper.list(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> selectByPid(Page page,Long pid) {
        return purchaseDetailMapper.selectByPid(page,pid);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> detailList(Page page, PurchaseDetailParam paramCondition) {
        return this.baseMapper.customMapList(page, paramCondition);
    }
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addDetail(PurchaseDetail detail){
        JSONObject result = new JSONObject();
        Purchase purchase = purchaseService.getById(detail.getPid());
        //判断采购订单状态是否不是已审核
        if(!purchase.getState().equals(Constants.PurchaseState.REVIEWED)){
            PurchaseDetail purchaseDetail = purchaseDetailMapper.selectOne(new QueryWrapper<PurchaseDetail>().eq("commoditybar",detail.getCommoditybar()).eq("pid",detail.getPid()));
            //判断明细表中是否已经有此商品的明细，如果存在此商品此商品的明细，修改商品明细表数据
            if(purchaseDetail != null){
                purchaseDetail.setQty(purchaseDetail.getQty()+detail.getQty());
                purchaseDetail.setUpdateid(ShiroKit.getUser().getId());
                purchaseDetail.setUpdator(ShiroKit.getUser().getName());
                purchaseDetail.setUpdatetime(new Date());
                purchaseDetailMapper.updateById(purchaseDetail);
                result.put("code", 200);
                result.put("message", "修改采购明细成功");
            }else{
                Commodity commodity = commodityMapper.selectOne(new QueryWrapper<Commodity>().eq("bar", detail.getCommoditybar()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                //根据商品编码判断商品是否存在,是否属于当前仓库
                if(commodity == null){
                    result.put("code", 500);
                    result.put("message", "商品编码不存在或此商品编码不属于当前仓库,请重新输入");
                }else{
                    detail.setCommodityid(commodity.getId());
                    detail.setCommodityname(commodity.getName());
                    detail.setCreateid(ShiroKit.getUser().getId());
                    detail.setCreator(ShiroKit.getUser().getName());
                    detail.setCreatetime(new Date());
                    purchaseDetailMapper.insert(detail);
                    purchase.setState(Constants.PurchaseState.UNREVIEW);
                    purchase.setUpdateid(ShiroKit.getUser().getId());
                    purchase.setUpdator(ShiroKit.getUser().getName());
                    purchase.setUpdatetime(new Date());
                    purchaseMapper.updateById(purchase);
                    result.put("code", 200);
                    result.put("message", "采购明细添加成功");
                }
            }
        }else{
            result.put("code", 500);
            result.put("message", "此订单已经审核,不能修改");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public PurchaseDetailResult findBySpec(PurchaseDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<PurchaseDetailResult> findListBySpec(PurchaseDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(PurchaseDetailParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(PurchaseDetailParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private PurchaseDetail getOldEntity(PurchaseDetailParam param) {
        return this.getById(getKey(param));
    }

    private PurchaseDetail getEntity(PurchaseDetailParam param) {
        PurchaseDetail entity = new PurchaseDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
