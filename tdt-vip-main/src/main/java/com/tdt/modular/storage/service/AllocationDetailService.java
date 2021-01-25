package com.tdt.modular.storage.service;

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
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.service.StockService;
import com.tdt.modular.storage.entity.Allocation;
import com.tdt.modular.storage.entity.AllocationDetail;
import com.tdt.modular.storage.mapper.AllocationDetailMapper;
import com.tdt.modular.storage.model.params.AllocationDetailParam;
import com.tdt.modular.storage.model.result.AllocationDetailResult;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 调拨明细服务类
 */
@Service
public class AllocationDetailService extends ServiceImpl<AllocationDetailMapper, AllocationDetail> {

    @Autowired
    private AllocationService allocationService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private StockService stockService;

    @Autowired
    private LocatorService locatorService;

    @Transactional(rollbackFor = Exception.class)
    public void add(AllocationDetailParam param){
        AllocationDetail entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addDetail(AllocationDetail detail,String locatorcode) {
        JSONObject result = new JSONObject();
        Allocation _allocation = allocationService.getById(detail.getPid());
        //判断调拨的状态是否不为已审核
        if (!_allocation.getState().equals(Constants.AllocationState.REVIEW)){
            AllocationDetail _temp = this.getOne(new QueryWrapper<AllocationDetail>().eq("pid",detail.getPid()).eq("commoditybar",detail.getCommoditybar()));
            //判断是否存在此调拨明细
            if(_temp != null){
                _temp.setQty(_temp.getQty()+detail.getQty());
                _temp.setUpdateid(ShiroKit.getUser().getId());
                _temp.setUpdator(ShiroKit.getUser().getName());
                _temp.setUpdatetime(new Date());
                this.updateById(_temp);
                result.put("code", 200);
                result.put("message", "调拨修改成功");
            }else{
                Commodity commodity = this.commodityService.getOne(new QueryWrapper<Commodity>().eq("bar", detail.getCommoditybar()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                //判断商品编码是否存在且商品编码属于当前仓库
                if (commodity!=null){
                    //判断货位编码是否为空
                    if (!"".equals(locatorcode)){
                        Locator locator = locatorService.getOne(new QueryWrapper<Locator>().eq("code",locatorcode));
                        //判断货位编码是否存在
                        if (locator!=null){
                            //判断货位是否属于原仓库货位
                            if (locator.getWarehouseid().equals(_allocation.getBwarehouseid())){
                                //判断货位状态是否正常
                                if (locator.getState().equals(Constants.LocatorState.NORMAL)){
                                    Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",locator.getId()).eq("commoditybar",detail.getCommoditybar()));
                                    //判断此仓库的此货位是否有此商品库存
                                    if (stock!=null){
                                        //判断此货位的库存数量是否大于调拨数量
                                        if (stock.getUqty()>=detail.getQty()){
                                            detail.setLocatorid(locator.getId());
                                            detail.setCommodityid(commodity.getId());
                                            detail.setCommodityname(commodity.getName());
                                            detail.setCreateid(ShiroKit.getUser().getId());
                                            detail.setCreator(ShiroKit.getUser().getName());
                                            detail.setCreatetime(new Date());
                                            this.baseMapper.insert(detail);
                                            _allocation.setState(Constants.AllocationState.UNREVIEW);
                                            allocationService.updateById(_allocation);
                                            result.put("code", 200);
                                            result.put("message", "操作成功");
                                        } else {
                                            result.put("code", 500);
                                            result.put("message", "此货位的此商品的可用库存为："+stock.getUqty()+",不足以调拨");
                                        }
                                    } else {
                                        result.put("code", 500);
                                        result.put("message", "此货位没有此商品的库存");
                                    }
                                } else {
                                    result.put("code", 500);
                                    result.put("message", "此货位状态不是正常状态，无法调拨");
                                }
                            } else {
                                result.put("code", 500);
                                result.put("message", "此货位编码不属于原仓库货位，请重新输入");
                            }
                        } else {
                            result.put("code", 500);
                            result.put("message", "货位编码不存在");
                        }
                    } else {
                        List<Locator> locatorList = locatorService.list(new QueryWrapper<Locator>().eq("warehouseid",_allocation.getBwarehouseid()));
                        Integer sum = 0;
                        for (Locator locator:locatorList){
                            //当库存状态正常时才将可用数量做累加计算
                            if (locator.getState().equals(Constants.LocatorState.NORMAL)){
                                Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",locator.getId()).eq("commoditybar",detail.getCommoditybar()));
                                if (stock!=null){
                                    sum = sum+stock.getUqty();
                                }
                            }
                        }
                        //判断原仓库是否有此商品的库存
                        if (sum!=0){
                            //判断原仓库的库存总数是否大于调拨数量
                            if (sum>=detail.getQty()){
                                detail.setCommodityid(commodity.getId());
                                detail.setCommodityname(commodity.getName());
                                detail.setCreateid(ShiroKit.getUser().getId());
                                detail.setCreator(ShiroKit.getUser().getName());
                                detail.setCreatetime(new Date());
                                this.baseMapper.insert(detail);
                                _allocation.setState(Constants.AllocationState.UNREVIEW);
                                allocationService.updateById(_allocation);
                                result.put("code", 200);
                                result.put("message", "操作成功");
                            } else {
                                result.put("code", 500);
                                result.put("message", "此仓库的此商品的可用库存为："+sum+",不足以调拨");
                            }
                        } else {
                            result.put("code", 500);
                            result.put("message", "此商品编码在原仓库中没有库存");
                        }
                    }
                } else {
                    result.put("code", 500);
                    result.put("message", "商品编码不存在或此商品编码不属于当前仓库,请重新输入");
                }
            }
        } else {
            result.put("code", 500);
            result.put("message", "此调拨单号已审核，无法修改调拨明细");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(AllocationDetailParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(AllocationDetailParam param){
        AllocationDetail oldEntity = getOldEntity(param);
        AllocationDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, AllocationDetailParam paramCondition) {
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> detailList(Page page,Long allocationId) {
        return this.baseMapper.detailList(page, allocationId);
    }

    @Transactional(rollbackFor = Exception.class)
    public AllocationDetailResult findBySpec(AllocationDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<AllocationDetailResult> findListBySpec(AllocationDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(AllocationDetailParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(AllocationDetailParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private AllocationDetail getOldEntity(AllocationDetailParam param) {
        return this.getById(getKey(param));
    }

    private AllocationDetail getEntity(AllocationDetailParam param) {
        AllocationDetail entity = new AllocationDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
}
