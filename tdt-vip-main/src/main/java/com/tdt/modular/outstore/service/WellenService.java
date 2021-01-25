package com.tdt.modular.outstore.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tdt.core.common.constant.Constants;
import com.tdt.core.util.SequenceBuilder;
import com.tdt.modular.base.entity.Commodity;
import com.tdt.modular.base.service.CommodityService;
import com.tdt.modular.outstore.entity.*;
import com.tdt.modular.outstore.mapper.OutorderDetailMapper;
import com.tdt.modular.outstore.mapper.OutorderMapper;
import com.tdt.modular.outstore.mapper.OutorderTagMapper;
import com.tdt.modular.outstore.mapper.PicktaskLockDetailMapper;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.entity.StockLog;
import com.tdt.modular.repertory.mapper.StockLogMapper;
import com.tdt.modular.repertory.mapper.StockMapper;
import com.tdt.modular.repertory.service.StockService;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class WellenService {

    @Autowired
    private OutorderService outorderService;

    @Resource
    private OutorderMapper outorderMapper;

    @Resource
    private OutorderTagMapper outorderTagMapper;

    @Resource
    private OutorderDetailMapper outorderDetailMapper;

    @Autowired
    private OutorderTagService outorderTagService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private StockService stockService;

    @Resource
    private StockMapper stockMapper;

    @Resource
    private StockLogMapper stockLogMapper;

    @Autowired
    private PicktaskService picktaskService;

    @Autowired
    private PicktaskOutorderService picktaskOutorderService;

    @Autowired
    private PicktaskDetailService picktaskDetailService;

    @Autowired
    private PicktaskLockDetailService picktaskLockDetailService;

    @Resource
    private PicktaskLockDetailMapper picktaskLockDetailMapper;

    public JSONObject add(String checkValue,Integer qty){
        JSONObject result = new JSONObject();
        String[] _checkValue = checkValue.split(",");
        String[] _specialValue = {"oneAndOne","oneAndAny","anyAndOne","anyAndAny"};
        StringBuffer sql= new StringBuffer("");
        StringBuffer responseData= new StringBuffer("");
        //将选中的值拼接到sql字符串中
        for(int i=0;_checkValue!=null&&i<_checkValue.length;i++){
            if(!"".equals(sql.toString())){
                sql.append(",");
            }
            sql.append(_checkValue[i]);
        }
        //如果选中单品单件，单品多件，多品单件，多品多件其中一项或多项，则将type拼接到sql字符串中
        for(int i=0;_specialValue!=null&&i<_specialValue.length;i++){
            if(sql.indexOf("type")==-1){
                if (sql.lastIndexOf(_specialValue[i])!=-1){
                    sql.replace(sql.lastIndexOf(_specialValue[i]),sql.lastIndexOf(_specialValue[i])+_specialValue[i].length(),"type");
                }
            }else{
                if (sql.lastIndexOf(_specialValue[i])!=-1){
                    sql.replace(sql.lastIndexOf(_specialValue[i]),sql.lastIndexOf(_specialValue[i])+_specialValue[i].length()+1,"");
                }
            }
        }
        //如果选中单品单件，单品多件，多品单件，多品多件其中一项或多项，执行后续操作
        for(int i=0;_specialValue!=null&&i<_specialValue.length;i++){
            List<Map<String, Object>> pccQtyMapList=new ArrayList<Map<String, Object>>();
            String errormsg="";
            if (Arrays.asList(_checkValue).contains(_specialValue[i])) {
                String typevalue="";
                if(_specialValue[i]=="oneAndOne"){
                    typevalue=Constants.OutorderTagType.ONEANDONE;
                    errormsg="单品单件生成拣货任务成功";
                }else if(_specialValue[i]=="oneAndAny"){
                    typevalue=Constants.OutorderTagType.ONEANDANY;
                    errormsg="单品多件生成拣货任务成功";
                } else if(_specialValue[i]=="anyAndOne"){
                    typevalue=Constants.OutorderTagType.ANYANDONE;
                    errormsg="多品单件生成拣货任务成功";
                } else if(_specialValue[i]=="anyAndAny"){
                    typevalue=Constants.OutorderTagType.ANYANDANY;
                    errormsg="多品多件生成拣货任务成功";
                }
                //根据条件查询出数量符合条件可生成波次的map集合
                pccQtyMapList = outorderTagMapper.groupList(ShiroKit.getUser().getWarehouseId(), typevalue, sql.toString(), qty);
            }
            if (pccQtyMapList.size()>0){
                int flagIsTrue = 0;
                String[] str = sql.toString().split(",");
                for (int j=0;j<pccQtyMapList.size();j++){
                    Map<String,String> map=new HashMap();
                    Map<String, Object> pccQtyMap = pccQtyMapList.get(j);
                    for(int n=0;str!=null&&n<str.length;n++){
                        if (Arrays.asList(str).contains(str[n])){
                            map.put(str[n],pccQtyMap.get(str[n]).toString());
                        }
                    }
                    while (1==1){
                        List<Long> ids = outorderMapper.pcctOutorderList(map,0,qty);
                        if (ids==null||qty>ids.size()){
                            break;
                        } else {
                            String[] _ids = new String[ids.size()];
                            for(int x=0;x<ids.size();x++){
                                _ids[x]=ids.get(x).toString();
                            }
                            boolean flag = picktaskTag(ids,_ids);
                            if (flag==true){
                                flagIsTrue++;
                                break;
                            }
                        }
                    }
                }
                responseData.append(errormsg+flagIsTrue+"次");
            }
        }
        //如果未选中单品单件，单品多件，多品单件，多品多件其中一项或多项，执行后续操作
        if (Arrays.asList(_checkValue).contains("oneAndOne")||Arrays.asList(_checkValue).contains("oneAndAny")||Arrays.asList(_checkValue).contains("anyAndOne")||Arrays.asList(_checkValue).contains("anyAndAny")){

        }else {
            List<Map<String, Object>> pccQtyMapList = outorderTagMapper.groupList(ShiroKit.getUser().getWarehouseId(),null,sql.toString(),qty);
            if (pccQtyMapList.size()>0){
                int flagIsTrue = 0;
                String[] str = sql.toString().split(",");
                for (int j=0;j<pccQtyMapList.size();j++){
                    Map<String,String> map=new HashMap();
                    Map<String, Object> pccQtyMap = pccQtyMapList.get(j);
                    for(int n=0;str!=null&&n<str.length;n++){
                        if (Arrays.asList(str).contains(str[n])){
                            map.put(str[n],pccQtyMap.get(str[n]).toString());
                        }
                    }
                    while (1==1){
                        List<Long> ids = outorderMapper.pcctOutorderList(map,0,qty);
                        if (ids==null||qty>ids.size()){
                            break;
                        } else {
                            String[] _ids = new String[ids.size()];
                            for(int x=0;x<ids.size();x++){
                                _ids[x]=ids.get(x).toString();
                            }
                            boolean flag = picktaskTag(ids,_ids);
                            if (flag==true){
                                flagIsTrue++;
                                break;
                            }
                        }
                    }
                }
                responseData.append("其他情况生成拣货任务成功"+flagIsTrue+"次");
            }
        }
        result.put("code", 200);
        result.put("message", "生成波次完成,"+responseData);
        return result;
    }

    private boolean picktaskTag(List<Long> ids,String[] _ids){
        //根据查询出来此拣货任务中所有出库订单明细id来统计此拣货任务中某种商品及对应的数量
        List<Long> noenough = outorderDetailMapper.noenough(_ids);
        if(noenough!=null&&noenough.size()>0){
            return false;
        }
        List<Outorder> outorders = (List<Outorder>)outorderService.listByIds(ids);
        for(int i=0;outorders!=null&&i<outorders.size();i++){
            outorders.get(i).setState(Constants.OutorderState.DISTRIBUTED);
        }
        outorderService.updateBatchById(outorders);
        outorderTagService.remove(new QueryWrapper<OutorderTag>().in("pid",ids));
        //添加picktask表信息
        Picktask picktask = new Picktask();
        String picktaskno = SequenceBuilder.generateNo(Constants.PREFIX_NO.PICKTASK_NO);
        picktask.setPicktaskno(picktaskno);
        picktask.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        picktask.setCreateid(ShiroKit.getUser().getId());
        picktask.setCreator(ShiroKit.getUser().getName());
        picktask.setCreatetime(new Date());
        picktaskService.save(picktask);
        //遍历符合条件的出库订单id
        List<PicktaskOutorder> picktaskOutorders=new ArrayList<PicktaskOutorder>();
        for(int i=0;outorders!=null&&i<outorders.size();i++){
            Outorder outorder=outorders.get(i);
            //添加picktaskoutorder表信息
            PicktaskOutorder picktaskOutorder = new PicktaskOutorder();
            picktaskOutorder.setPid(picktask.getId());
            picktaskOutorder.setOutorderid(outorder.getId());
            picktaskOutorder.setOutorderno(outorder.getOutorderno());
            picktaskOutorders.add(picktaskOutorder);
        }
        picktaskOutorderService.saveBatch(picktaskOutorders);
        List<Map<String, Object>> commodityQtyMapList = outorderDetailMapper.countCommodityQty(_ids);
        //此循环用来保存picktaskdetail数据
        for (int p=0;p<commodityQtyMapList.size();p++){
            //查询具体的商品及所对应的数量
            Long commodityId = Long.valueOf(commodityQtyMapList.get(p).get("commodityid").toString());
            Integer qtySum = Integer.valueOf(commodityQtyMapList.get(p).get("sqty").toString());
            //查出当前商品的详细信息
            //添加拣货任务明细数据（pid,commodityid,commoditybar,commodityname）
            PicktaskDetail picktaskDetail = new PicktaskDetail();
            picktaskDetail.setPid(picktask.getId());
            picktaskDetail.setCommodityid((Long)commodityQtyMapList.get(p).get("commodityid"));
            picktaskDetail.setCommoditybar(commodityQtyMapList.get(p).get("commoditybar").toString());
            picktaskDetail.setCommodityname(commodityQtyMapList.get(p).get("commodityname").toString());
            //查询拥有此商品库存且货位为普通货位的list列表
            List<Stock> stockList = stockMapper.queryCommonLocatorStock(commodityId);
            //遍历拥有此商品的库存列表
            for (Stock stock:stockList){
                //判断当前的库存可用数量是否大于商品所需数量
                if (stock.getUqty()>=qtySum){
                    stock.setUqty(stock.getUqty()-qtySum);
                    stock.setLqty(stock.getLqty()+qtySum);
                    stock.setUpdateid(ShiroKit.getUser().getId());
                    stock.setUpdator(ShiroKit.getUser().getName());
                    stock.setUpdatetime(new Date());
                    StockLog stockLog = stockService.addStockLog(picktask.getPicktaskno(),stock.getWarehouseid(),stock.getLocatorid(),
                            stock.getLocatorcode(),stock.getLocatorname(),stock.getCommodityid(),stock.getCommoditybar(),
                            stock.getCommodityname(),qtySum,Constants.StockLogType.LOCKSTOCK);
                    //添加剩余的拣货任务明细（stockid,stocklogid,qty,locatorid,locatorcode,locatorname）
                    picktaskDetail.setId(null);
                    picktaskDetail.setStockid(stock.getId());
                    picktaskDetail.setStocklogid(stockLog.getId());
                    picktaskDetail.setQty(qtySum);
                    picktaskDetail.setLocatorid(stock.getLocatorid());
                    picktaskDetail.setLocatorcode(stock.getLocatorcode());
                    picktaskDetail.setLocatorname(stock.getLocatorname());
                    picktaskDetailService.save(picktaskDetail);
                    break;
                } else {
                    picktaskDetail.setId(null);
                    picktaskDetail.setQty(stock.getUqty());
                    stock.setLqty(stock.getLqty()+stock.getUqty());
                    qtySum = qtySum-stock.getUqty();
                    stock.setUqty(0);
                    stock.setUpdateid(ShiroKit.getUser().getId());
                    stock.setUpdator(ShiroKit.getUser().getName());
                    stock.setUpdatetime(new Date());
                    StockLog stockLog = stockService.addStockLog(picktask.getPicktaskno(),stock.getWarehouseid(),stock.getLocatorid(),
                            stock.getLocatorcode(),stock.getLocatorname(),stock.getCommodityid(),stock.getCommoditybar(),
                            stock.getCommodityname(),stock.getUqty(),Constants.StockLogType.LOCKSTOCK);
                    picktaskDetail.setStockid(stock.getId());
                    picktaskDetail.setStocklogid(stockLog.getId());
                    picktaskDetail.setLocatorid(stock.getLocatorid());
                    picktaskDetail.setLocatorcode(stock.getLocatorcode());
                    picktaskDetail.setLocatorname(stock.getLocatorname());
                    picktaskDetailService.save(picktaskDetail);
                }
            }
            stockService.updateBatchById(stockList);
        }
        //根据已锁定的库存分配每一条出库订单中每一种商品在哪一个货位上扣除了多少数量
        //根据拣货单号,状态正常，类型为锁定查询出锁定库存的日志记录并根据商品编码排序显示
        List<StockLog> stockLogList = stockLogMapper.selectListByBillno(picktaskno,Constants.StockLogState.NORMAL,Constants.StockLogType.LOCKSTOCK);
        int qty = 0;
        for (StockLog stockLog:stockLogList){
            int stockLogQty = stockLog.getQty();
            //根据当前商品id和出库订单的id集合查询出符合条件的出库订单明细
            List<OutorderDetail> outorderDetailList = outorderDetailMapper.selectListByCommodityIdAndOutorderId(stockLog.getCommodityid(),_ids);
            List<PicktaskLockDetail> picktaskLockDetails=new ArrayList<PicktaskLockDetail>();
            for (OutorderDetail outorderDetail:outorderDetailList){
                //统计当前出库明细已插入到picktasklockdetail表中的数量
                //判断当前出库订单明细是否已分配完成
                if (outorderDetail.getQty()!=qty){
                    int outorderdetailQty = outorderDetail.getQty()-qty;
                    //添加picktask_lock_detail表数据
                    PicktaskLockDetail picktaskLockDetail = new PicktaskLockDetail();
                    picktaskLockDetail.setPid(picktask.getId());
                    picktaskLockDetail.setPicktaskno(picktaskno);
                    picktaskLockDetail.setOutorderid(outorderDetail.getPid());
                    picktaskLockDetail.setOutorderdetailid(outorderDetail.getId());
                    picktaskLockDetail.setCommodityid(stockLog.getCommodityid());
                    picktaskLockDetail.setCommoditybar(stockLog.getCommoditybar());
                    picktaskLockDetail.setCommodityname(stockLog.getCommodityname());
                    picktaskLockDetail.setLocatorid(stockLog.getLocatorid());
                    picktaskLockDetail.setLocatorcode(stockLog.getLocatorcode());
                    picktaskLockDetail.setLocatorname(stockLog.getLocatorname());
                    //判断当前日志的商品数量是否大于当前出库明细数量
                    if (stockLogQty>=outorderdetailQty){
                        picktaskLockDetail.setQty(outorderdetailQty);
                        qty=qty+outorderdetailQty;
                        picktaskLockDetails.add(picktaskLockDetail);
                        stockLogQty = stockLogQty - outorderdetailQty;
                        if (stockLogQty==0){
                            break;
                        }
                    } else {
                        picktaskLockDetail.setQty(stockLogQty);
                        picktaskLockDetails.add(picktaskLockDetail);
                        qty=0;
                        break;
                    }
                }
            }
            picktaskLockDetailService.saveBatch(picktaskLockDetails);
            picktaskLockDetailService.updateoutorderno();
        }
        return true;
    }
}

