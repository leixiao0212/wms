package com.tdt.modular.base.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.modular.base.entity.Locator;
import com.tdt.modular.base.mapper.LocatorMapper;
import com.tdt.modular.base.model.params.LocatorParam;
import com.tdt.modular.base.model.result.LocatorResult;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.service.StockService;
import com.tdt.sys.core.exception.enums.BizExceptionEnum;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 库位管理服务类
 * @author
 */
@Service
public class LocatorService extends ServiceImpl<LocatorMapper, Locator> {

    @Autowired
    private StockService stockService;

    @Transactional(rollbackFor = Exception.class)
    public void add(LocatorParam param){
        Locator entity = getEntity(param);
        entity.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        entity.setCreateid(ShiroKit.getUser().getId());
        entity.setCreator(ShiroKit.getUser().getName());
        entity.setCreatetime(new Date());
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(LocatorParam param){
        Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",param.getId()));
        //判断货位状态是否有库存
        if (stock == null){
            this.removeById(getKey(param));
        } else {
            throw new ServiceException(BizExceptionEnum.STOCK_DELETE);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject update(LocatorParam param){
        JSONObject result = new JSONObject();
        Locator oldEntity = getOldEntity(param);
        Locator newEntity = getEntity(param);
        //判断新货位与原货位的状态是否不同
        if (!newEntity.getState().equals(oldEntity.getState())){
            List<Stock> stockList = stockService.list(new QueryWrapper<Stock>().eq("locatorid",newEntity.getId()));
            //判断货位上是否没有商品的库存
            if (stockList.size()>0){
                result.put("code", 500);
                result.put("message", "此货位尚有商品库存，无法修改状态");
                return result;
            }
        }
        ToolUtil.copyProperties(newEntity, oldEntity);
        newEntity.setUpdateid(ShiroKit.getUser().getId());
        newEntity.setUpdator(ShiroKit.getUser().getName());
        newEntity.setUpdatetime(new Date());
        this.updateById(newEntity);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page,LocatorParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public LocatorResult findBySpec(LocatorParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<LocatorResult> findListBySpec(LocatorParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(LocatorParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(LocatorParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Locator getOldEntity(LocatorParam param) {
        return this.getById(getKey(param));
    }

    private Locator getEntity(LocatorParam param) {
        Locator entity = new Locator();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
