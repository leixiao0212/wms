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
import com.tdt.core.util.SequenceBuilder;
import com.tdt.modular.outstore.entity.*;
import com.tdt.modular.outstore.mapper.PickDetailMapper;
import com.tdt.modular.outstore.mapper.PickMapper;
import com.tdt.modular.outstore.mapper.PicktaskMapper;
import com.tdt.modular.outstore.model.params.PickParam;
import com.tdt.modular.outstore.model.result.PickResult;
import com.tdt.modular.repertory.entity.StockLog;
import com.tdt.modular.repertory.mapper.StockLogMapper;
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
 * 拣货服务类
 */
@Service
public class PickService extends ServiceImpl<PickMapper, Pick> {

    @Autowired
    private PicktaskService picktaskService;

    @Resource
    private PicktaskMapper picktaskMapper;

    @Autowired
    private PickDeailService pickDeailService;

    @Resource
    private StockLogMapper stockLogMapper;

    @Autowired
    private PicktaskOutorderService picktaskOutorderService;

    @Autowired
    private OutorderService outorderService;

    @Resource
    private PickDetailMapper pickDetailMapper;

    @Autowired
    private PicktaskDetailService picktaskDetailService;

    @Transactional(rollbackFor = Exception.class)
    public void add(PickParam param){
        Pick entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addHead(Pick pick) {
        JSONObject result = new JSONObject();
        Picktask picktask = picktaskService.getOne(new QueryWrapper<Picktask>().eq("picktaskno",pick.getPicktaskno()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
        //判断拣货任务中是否有此拣货任务单号且拣货单号是否属于当前仓库
        if (picktask!=null){
            Pick _pick = this.getOne(new QueryWrapper<Pick>().eq("picktaskno",pick.getPicktaskno()));
            //判断此拣货单号是否已被拣货
            if (_pick!=null){
                //判断拣货状态是否为未完成
                if (!_pick.getState().equals(Constants.PickState.PICKED)){
                    result.put("code", 200);
                    result.put("message", "此拣货单号已被拣货");
                    result.put("pick", _pick);
                } else {
                    result.put("code", 500);
                    result.put("message", "此拣货单号已完成，无法继续拣货");
                }
            } else {
                pick.setPickno(SequenceBuilder.generateNo(Constants.PREFIX_NO.PICK_NO));
                pick.setPicktaskid(picktask.getId());
                pick.setState(Constants.PickState.INIT);
                pick.setWarehouseid(ShiroKit.getUser().getWarehouseId());
                pick.setCreateid(ShiroKit.getUser().getId());
                pick.setCreator(ShiroKit.getUser().getName());
                pick.setCreatetime(new Date());
                this.save(pick);
                picktask.setReceiverid(ShiroKit.getUser().getId());
                picktask.setReceiver(ShiroKit.getUser().getName());
                picktask.setReceivetime(new Date());
                picktaskService.updateById(picktask);
                //修改拣货任务中的出库订单状态为拣货中
                List<PicktaskOutorder> picktaskOutorderList = picktaskOutorderService.list(new QueryWrapper<PicktaskOutorder>().eq("pid",picktask.getId()));
                for (PicktaskOutorder picktaskOutorder:picktaskOutorderList){
                    Outorder outorder = outorderService.getById(picktaskOutorder.getOutorderid());
                    outorder.setState(Constants.OutorderState.PICKING);
                    outorderService.updateById(outorder);
                }
                result.put("code", 200);
                result.put("message", "拣货成功");
                result.put("pick", pick);
            }
        } else {
            result.put("code", 500);
            result.put("message", "拣货任务单号不存在，请重新输入");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(PickParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PickParam param){
        Pick oldEntity = getOldEntity(param);
        Pick newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, PickParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject finish(PickDetail pickDetail) {
        JSONObject result = new JSONObject();
        Pick pick = this.getById(pickDetail.getPid());
        if (pick.getState().equals(Constants.PickState.PICKING)){
            //判断拣货明细与拣货任务明细是否一致
            //统计拣货明细中每种货位上的商品的数量
            List<Map<String,Object>> commodityQtyMapList = pickDetailMapper.countCommodityQty(pick.getId());
            for (int i=0;i<commodityQtyMapList.size();i++){
                Long commodityId = (Long)(commodityQtyMapList.get(i).get("commodityid"));
                Long locatorId = (Long)(commodityQtyMapList.get(i).get("locatorid"));
                int qtySum = Integer.parseInt(commodityQtyMapList.get(i).get("SUM(qty)").toString());
                PicktaskDetail picktaskDetail = picktaskDetailService.getOne(new QueryWrapper<PicktaskDetail>().eq("pid",pick.getPicktaskid()).eq("commodityid",commodityId).eq("locatorid",locatorId));
                if (picktaskDetail.getQty()!=qtySum){
                    result.put("code", 500);
                    result.put("message", "货位"+picktaskDetail.getLocatorcode()+"上的商品"+picktaskDetail.getCommoditybar()+"数量为"+picktaskDetail.getQty()+"与拣货任务不一致，请重新核对");
                    result.put("pick", pick);
                    return result;
                }
            }
            pick.setState(Constants.PickState.PICKED);
            this.updateById(pick);
            result.put("code", 200);
            result.put("message", "拣货已结束");
        } else if (pick.getState().equals(Constants.PickState.INIT)){
            result.put("code", 500);
            result.put("message", "拣货明细还未录入，无法结束");
        } else if (pick.getState().equals(Constants.PickState.PICKED)){
            result.put("code", 500);
            result.put("message", "拣货已结束，不用再次点击");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void restore(PickParam param){
        Pick pick = this.getById(param.getId());
        //将拣货任务的领取人id、领取人、领取时间置为空
        picktaskMapper.updateSetNull(pick.getPicktaskid());
        //将与其有关的日志状态置位失效
        List<StockLog> stockLogList = stockLogMapper.selectList(new QueryWrapper<StockLog>().eq("billno",pick.getPickno()).eq("state",Constants.StockLogState.NORMAL).eq("type",Constants.StockLogType.PUTOUTSTOCK));
        for (StockLog stockLog:stockLogList){
            stockLog.setState(Constants.StockLogState.INVALID);
            stockLogMapper.updateById(stockLog);
        }
        //级联删除拣货表和拣货明细中相关的数据
        pickDeailService.remove(new QueryWrapper<PickDetail>().eq("pid",pick.getId()));
        this.removeById(pick.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void confirmChangeState(Long pickid){
        Pick pick = this.getById(pickid);
        pick.setState(Constants.PickState.EXCEPTIONTODO);
        this.updateById(pick);
    }

    @Transactional(rollbackFor = Exception.class)
    public PickResult findBySpec(PickParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<PickResult> findListBySpec(PickParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(PickParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(PickParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Pick getOldEntity(PickParam param) {
        return this.getById(getKey(param));
    }

    private Pick getEntity(PickParam param) {
        Pick entity = new Pick();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
}
