package com.tdt.modular.outstore.service;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tdt.base.pojo.page.LayuiPageFactory;
import com.tdt.base.pojo.page.LayuiPageInfo;
import com.tdt.core.common.constant.Constants;
import com.tdt.modular.outstore.entity.*;
import com.tdt.modular.outstore.mapper.PicktaskMapper;
import com.tdt.modular.outstore.model.params.PicktaskParam;
import com.tdt.modular.outstore.model.result.PicktaskResult;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.entity.StockLog;
import com.tdt.modular.repertory.mapper.StockLogMapper;
import com.tdt.modular.repertory.service.StockService;
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
 * 拣货任务服务
 */
@Service
public class PicktaskService extends ServiceImpl<PicktaskMapper, Picktask> {

    @Autowired
    private PicktaskOutorderService picktaskOutorderService;

    @Autowired
    private PicktaskDetailService picktaskDetailService;

    @Autowired
    private PicktaskLockDetailService picktaskLockDetailService;

    @Autowired
    private StockService stockService;

    @Resource
    private StockLogMapper stockLogMapper;

    @Autowired
    private OutorderService outorderService;

    @Transactional(rollbackFor = Exception.class)
    public void add(PicktaskParam param){
        Picktask entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(PicktaskParam param){
        Picktask picktask = this.getById(param.getId());
        //将生成拣货任务时锁定的库存日志置为失效
        List<StockLog> stockLogList = stockLogMapper.selectList(new QueryWrapper<StockLog>().eq("billno",picktask.getPicktaskno()).eq("state",Constants.StockLogState.NORMAL).eq("type",Constants.StockLogType.LOCKSTOCK));
        for (StockLog stockLog:stockLogList){
            stockLog.setState(Constants.StockLogState.INVALID);
            stockLogMapper.updateById(stockLog);
        }
        //将生成拣货任务时锁定的库存还原
        List<PicktaskDetail> picktaskDetailList = picktaskDetailService.list(new QueryWrapper<PicktaskDetail>().eq("pid",param.getId()));
        for (PicktaskDetail picktaskDetail:picktaskDetailList){
            Stock stock = stockService.getById(picktaskDetail.getStockid());
            stock.setLqty(stock.getLqty()-picktaskDetail.getQty());
            stock.setUqty(stock.getUqty()+picktaskDetail.getQty());
            stock.setUpdateid(ShiroKit.getUser().getId());
            stock.setUpdator(ShiroKit.getUser().getName());
            stock.setUpdatetime(new Date());
            stockService.updateById(stock);
        }
        //删除拣货任务表，拣货明细表和拣货任务与出库订单关联表中相关数据
        picktaskDetailService.remove(new QueryWrapper<PicktaskDetail>().eq("pid",param.getId()));
        picktaskOutorderService.remove(new QueryWrapper<PicktaskOutorder>().eq("pid",param.getId()));
        this.removeById(param.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PicktaskParam param){
        Picktask oldEntity = getOldEntity(param);
        Picktask newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, PicktaskParam paramCondition,String outorderno,String isReceive) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition,outorderno,isReceive);
    }
    @Transactional(rollbackFor = Exception.class)
    public void restore(PicktaskParam picktaskParam){
        Picktask picktask = this.getById(picktaskParam.getId());
        //将生成拣货任务时锁定的库存日志置为失效
        List<StockLog> stockLogList = stockLogMapper.selectList(new QueryWrapper<StockLog>().eq("billno",picktask.getPicktaskno()).eq("state",Constants.StockLogState.NORMAL).eq("type",Constants.StockLogType.LOCKSTOCK));
        for (StockLog stockLog:stockLogList){
            stockLog.setState(Constants.StockLogState.INVALID);
            stockLogMapper.updateById(stockLog);
        }
        //将生成拣货任务时锁定的库存还原
        List<PicktaskLockDetail> picktaskLockDetailList = picktaskLockDetailService.list(new QueryWrapper<PicktaskLockDetail>().eq("pid",picktask.getId()));
        for (PicktaskLockDetail picktaskLockDetail:picktaskLockDetailList){
            Stock stock = stockService.getOne(new QueryWrapper<Stock>().eq("locatorid",picktaskLockDetail.getLocatorid()).eq("commodityid",picktaskLockDetail.getCommodityid()));
            stock.setLqty(stock.getLqty()-picktaskLockDetail.getQty());
            stock.setUqty(stock.getUqty()+picktaskLockDetail.getQty());
            stock.setUpdateid(ShiroKit.getUser().getId());
            stock.setUpdator(ShiroKit.getUser().getName());
            stock.setUpdatetime(new Date());
            stockService.updateById(stock);
        }
        //将此拣货任务中出库订单的状态置为已审核
        List<PicktaskOutorder> picktaskOutorderList = picktaskOutorderService.list(new QueryWrapper<PicktaskOutorder>().eq("pid",picktask.getId()));
        for (PicktaskOutorder picktaskOutorder:picktaskOutorderList){
            Outorder outorder = outorderService.getById(picktaskOutorder.getOutorderid());
            outorder.setState(Constants.OutorderState.REVIEW);
            outorderService.updateById(outorder);
        }
        //删除拣货任务四张表中的相关数据
        picktaskLockDetailService.remove(new QueryWrapper<PicktaskLockDetail>().eq("pid",picktask.getId()));
        picktaskDetailService.remove(new QueryWrapper<PicktaskDetail>().eq("pid",picktask.getId()));
        picktaskOutorderService.remove(new QueryWrapper<PicktaskOutorder>().eq("pid",picktask.getId()));
        this.removeById(picktask);
    }

    @Transactional(rollbackFor = Exception.class)
    public PicktaskResult findBySpec(PicktaskParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<PicktaskResult> findListBySpec(PicktaskParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(PicktaskParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(PicktaskParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Picktask getOldEntity(PicktaskParam param) {
        return this.getById(getKey(param));
    }

    private Picktask getEntity(PicktaskParam param) {
        Picktask entity = new Picktask();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
