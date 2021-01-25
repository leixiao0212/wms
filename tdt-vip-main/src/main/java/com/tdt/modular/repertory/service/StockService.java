package com.tdt.modular.repertory.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.core.common.constant.Constants;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.entity.StockLog;
import com.tdt.modular.repertory.mapper.StockLogMapper;
import com.tdt.modular.repertory.mapper.StockMapper;
import com.tdt.modular.repertory.model.params.StockParam;
import com.tdt.modular.repertory.model.result.StockResult;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 库存管理服务类
 */
@Service
public class StockService extends ServiceImpl<StockMapper, Stock> {

    @Resource
    private StockLogMapper stockLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public void add(StockParam param){
        Stock entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(StockParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(StockParam param){
        Stock oldEntity = getOldEntity(param);
        Stock newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, StockParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public StockResult findBySpec(StockParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<StockResult> findListBySpec(StockParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(StockParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    /**
     * 库存变动日志记录
     */
    @Transactional(rollbackFor = Exception.class)
    public StockLog addStockLog(String billno, Long warehouseid, Long locatorid, String locatorcode, String locatorname,
                            Long commodityid, String commoditybar, String commodityname, Integer qty,String type){
        StockLog stockLog = new StockLog();
        stockLog.setBillno(billno);
        stockLog.setWarehouseid(warehouseid);
        stockLog.setLocatorid(locatorid);
        stockLog.setLocatorcode(locatorcode);
        stockLog.setLocatorname(locatorname);
        stockLog.setCommodityid(commodityid);
        stockLog.setCommoditybar(commoditybar);
        stockLog.setCommodityname(commodityname);
        stockLog.setQty(qty);
        stockLog.setType(type);
        stockLog.setState(Constants.StockLogState.NORMAL);
        stockLog.setCreateid(ShiroKit.getUser().getId());
        stockLog.setCreator(ShiroKit.getUser().getName());
        stockLog.setCreatetime(new Date());
        stockLogMapper.insert(stockLog);
        return stockLog;
    }

    /**
     * 撤销审核时，库存日志状态应该置为无效
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeStockLogState(String billno){
        List<StockLog> stockLogList = stockLogMapper.selectList(new QueryWrapper<StockLog>().eq("billno",billno));
        for (StockLog stockLog:stockLogList){
            stockLog.setState(Constants.StockLogState.INVALID);
            stockLog.setUpdateid(ShiroKit.getUser().getId());
            stockLog.setUpdator(ShiroKit.getUser().getName());
            stockLog.setUpdatetime(new Date());
            stockLogMapper.updateById(stockLog);
        }
    }

    private Serializable getKey(StockParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Stock getOldEntity(StockParam param) {
        return this.getById(getKey(param));
    }

    private Stock getEntity(StockParam param) {
        Stock entity = new Stock();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
}
