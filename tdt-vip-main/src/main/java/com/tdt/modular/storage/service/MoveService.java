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
import com.tdt.core.util.DateUtil;
import com.tdt.modular.base.entity.Commodity;
import com.tdt.modular.base.entity.Locator;
import com.tdt.modular.base.entity.Warehouse;
import com.tdt.modular.base.service.CommodityService;
import com.tdt.modular.base.service.LocatorService;
import com.tdt.modular.base.service.WarehouseService;
import com.tdt.modular.repertory.entity.Stock;
import com.tdt.modular.repertory.entity.StockLog;
import com.tdt.modular.repertory.mapper.StockLogMapper;
import com.tdt.modular.repertory.service.StockService;
import com.tdt.modular.storage.entity.Move;
import com.tdt.modular.storage.mapper.MoveMapper;
import com.tdt.modular.storage.model.params.MoveParam;
import com.tdt.modular.storage.model.result.MoveResult;
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
 * 移库管理服务类
 */
@Service
public class MoveService extends ServiceImpl<MoveMapper, Move> {

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private LocatorService locatorService;

    @Autowired
    private StockService stockService;

    @Autowired
    private WarehouseService warehouseService;

    @Resource
    private StockLogMapper stockLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public void add(MoveParam param){
        Move entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addHead(Move move) {
        JSONObject result = new JSONObject();
        Commodity commodity = commodityService.getOne(new QueryWrapper<Commodity>().eq("bar",move.getCommoditybar()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
        //判断商品编码是否存在且属于当前仓库
        if (commodity!=null){
            Locator oldLocator = locatorService.getOne(new QueryWrapper<Locator>().eq("code",move.getOldlocatorcode()).eq("state",Constants.LocatorState.NORMAL).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
            //判断原货位是否存在,状态是否正常,是否属于当前仓库
            if (oldLocator!=null){
                Locator locator = locatorService.getOne(new QueryWrapper<Locator>().eq("code",move.getLocatorcode()).eq("state",Constants.LocatorState.NORMAL).eq("type",oldLocator.getType()).eq("warehouseid",oldLocator.getWarehouseid()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                //判断新货位是否存在,状态是否正常,货位类型是否与原货位类型一致,是否与原货位在同一仓库,是否属于当前仓库
                if (locator!=null){
                    Stock oldStock= stockService.getOne(new QueryWrapper<Stock>().eq("commoditybar",move.getCommoditybar()).eq("locatorcode",move.getOldlocatorcode()));
                    //判断原货位是否有这个商品库存
                    if (oldStock!=null){
                        //判断原货位的商品库存的可用数量是否大于或等于移库数量
                        if (oldStock.getUqty()-move.getQty()>=0){
                            move.setMoveno("MO"+ DateUtil.getAllMsTime());
                            move.setState(Constants.MoveState.UNREVIEW);
                            move.setCommodityid(commodity.getId());
                            move.setCommodityname(commodity.getName());
                            move.setOldlocatorid(oldLocator.getId());
                            move.setOldlocatorname(oldLocator.getName());
                            move.setLocatorid(locator.getId());
                            move.setLocatorname(locator.getName());
                            move.setWarehouseid(ShiroKit.getUser().getWarehouseId());
                            move.setCreateid(ShiroKit.getUser().getId());
                            move.setCreator(ShiroKit.getUser().getName());
                            move.setCreatetime(new Date());
                            this.baseMapper.insert(move);
                            //将原货位库存锁定
                            oldStock.setUqty(oldStock.getUqty()-move.getQty());
                            oldStock.setLqty(oldStock.getLqty()+move.getQty());
                            oldStock.setUpdateid(ShiroKit.getUser().getId());
                            oldStock.setUpdator(ShiroKit.getUser().getName());
                            oldStock.setUpdatetime(new Date());
                            stockService.updateById(oldStock);
                            //库存日志记录
                            stockService.addStockLog(move.getMoveno(),oldStock.getWarehouseid(),oldStock.getLocatorid(),oldStock.getLocatorcode(),
                                    oldStock.getLocatorname(),oldStock.getCommodityid(),oldStock.getCommoditybar(),oldStock.getCommodityname(),
                                    move.getQty(),Constants.StockLogType.LOCKSTOCK);
                        } else {
                            result.put("code", 500);
                            result.put("message", "原货位该商品库存可用数量为："+oldStock.getUqty()+",不能满足移库需求");
                            return result;
                        }
                    } else {
                        result.put("code", 500);
                        result.put("message", "原货位没有此商品库存,无法移库");
                        return result;
                    }
                } else {
                    result.put("code", 500);
                    result.put("message", "新货位不符合移库的条件");
                    return result;
                }
            } else {
                result.put("code", 500);
                result.put("message", "原货位不符合移库的条件");
                return result;
            }
        } else {
            result.put("code", 500);
            result.put("message", "商品编码不存在或此商品编码不属于当前仓库,请重新输入");
            return result;
        }
        result.put("code", 200);
        result.put("message", "移库信息添加成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject review(Long id) {
        JSONObject result = new JSONObject();
        Move move = this.getById(id);
        //判断移库状态是否为待审核
        if(move.getState().equals(Constants.MoveState.UNREVIEW)){
            move.setState(Constants.MoveState.REVIEW);
            move.setAuditid(ShiroKit.getUser().getId());
            move.setAuditor(ShiroKit.getUser().getName());
            move.setAudittime(new Date());
            this.updateById(move);
            //将移库的日志置为失效
            StockLog stockLog = stockLogMapper.selectOne(new QueryWrapper<StockLog>().eq("billno",move.getMoveno()).eq("type",Constants.StockLogType.LOCKSTOCK));
            stockLog.setState(Constants.StockLogState.INVALID);
            stockLogMapper.updateById(stockLog);
            Stock oldStock= stockService.getOne(new QueryWrapper<Stock>().eq("commoditybar",move.getCommoditybar()).eq("locatorcode",move.getOldlocatorcode()));
            ////判断原库存移库后总数量是否为0
            if (oldStock.getCqty()-move.getQty()==0){
                stockService.removeById(oldStock.getId());
            } else {
                oldStock.setCqty(oldStock.getCqty()-move.getQty());
                oldStock.setLqty(oldStock.getLqty()-move.getQty());
                oldStock.setUpdateid(ShiroKit.getUser().getId());
                oldStock.setUpdator(ShiroKit.getUser().getName());
                oldStock.setUpdatetime(new Date());
                stockService.updateById(oldStock);
            }
            //记录原货位的库存日志
            stockService.addStockLog(move.getMoveno(),oldStock.getWarehouseid(),oldStock.getLocatorid(),oldStock.getLocatorcode(),
                    oldStock.getLocatorname(),oldStock.getCommodityid(),oldStock.getCommoditybar(),oldStock.getCommodityname(),
                    move.getQty()*(-1),Constants.StockLogType.OUTSTOCK);
            Stock newStock= stockService.getOne(new QueryWrapper<Stock>().eq("commoditybar",move.getCommoditybar()).eq("locatorcode",move.getLocatorcode()));
            //判断新货位是否有此商品库存
            if (newStock!=null){
                newStock.setCqty(newStock.getCqty()+move.getQty());
                newStock.setUqty(newStock.getUqty()+move.getQty());
                newStock.setUpdateid(ShiroKit.getUser().getId());
                newStock.setUpdator(ShiroKit.getUser().getName());
                newStock.setUpdatetime(new Date());
                stockService.updateById(newStock);
                //记录新货位的库存日志
                stockService.addStockLog(move.getMoveno(),newStock.getWarehouseid(),newStock.getLocatorid(),
                        newStock.getLocatorcode(),newStock.getLocatorname(),newStock.getCommodityid(),
                        newStock.getCommoditybar(),newStock.getCommodityname(),move.getQty(),Constants.StockLogType.INSTOCK);
            } else {
                Stock stock = new Stock();
                Locator locator = locatorService.getById(move.getLocatorid());
                Commodity commodity = commodityService.getById(move.getCommodityid());
                stock.setBillno(move.getMoveno());
                stock.setLocatorid(locator.getId());
                stock.setLocatorcode(locator.getCode());
                stock.setLocatorname(locator.getName());
                stock.setLocatorstate(locator.getState());
                stock.setCommodityid(commodity.getId());
                stock.setCommoditybar(commodity.getBar());
                stock.setCommodityname(commodity.getName());
                stock.setCqty(move.getQty());
                stock.setUqty(move.getQty());
                stock.setLqty(0);
                Warehouse warehouse = warehouseService.getOne(new QueryWrapper<Warehouse>().eq("id",locator.getWarehouseid()));
                stock.setWarehouseid(warehouse.getId());
                stock.setCreateid(ShiroKit.getUser().getId());
                stock.setCreator(ShiroKit.getUser().getName());
                stock.setCreatetime(new Date());
                stockService.save(stock);
                //记录新货位的库存日志
                stockService.addStockLog(move.getMoveno(),warehouse.getId(),locator.getId(),locator.getCode(),locator.getName(),commodity.getId(),
                        commodity.getBar(),commodity.getName(),move.getQty(),Constants.StockLogType.INSTOCK);
            }
        } else {
            result.put("code", 500);
            result.put("message", "移库信息已审核，请勿重复审核");
            return result;
        }
        result.put("code", 200);
        result.put("message", "审核成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject cancelReview(Long id) {
        JSONObject result = new JSONObject();
        Move move = this.getById(id);
        //判断移库状态是否审核
        if (move.getState().equals(Constants.MoveState.REVIEW)){
            move.setState(Constants.MoveState.UNREVIEW);
            this.updateById(move);
            //将审核时入库和出库的库存日志置为失效
            List<StockLog> stockLogList = stockLogMapper.selectList(new QueryWrapper<StockLog>().eq("billno",move.getMoveno()).and(i -> i.eq("type",Constants.StockLogType.INSTOCK)).or().eq("type",Constants.StockLogType.OUTSTOCK));
            for (StockLog _stockLog:stockLogList){
                _stockLog.setState(Constants.StockLogState.INVALID);
                stockLogMapper.updateById(_stockLog);
            }
            //将原货位扣除的数量加到总数量和锁定数量上
            Stock oldStock= stockService.getOne(new QueryWrapper<Stock>().eq("commoditybar",move.getCommoditybar()).eq("locatorcode",move.getOldlocatorcode()));
            oldStock.setCqty(oldStock.getCqty()+move.getQty());
            oldStock.setLqty(oldStock.getLqty()+move.getQty());
            oldStock.setUpdateid(ShiroKit.getUser().getId());
            oldStock.setUpdator(ShiroKit.getUser().getName());
            oldStock.setUpdatetime(new Date());
            stockService.updateById(oldStock);
            //将新货位上增加的总数量和可用数量扣除
            Stock newStock= stockService.getOne(new QueryWrapper<Stock>().eq("commoditybar",move.getCommoditybar()).eq("locatorcode",move.getLocatorcode()));
            //判断新货位上的总数量扣除掉上架的数量是否为空
            if (newStock.getCqty()-move.getQty()==0){
                stockService.removeById(newStock.getId());
            } else {
                newStock.setCqty(newStock.getCqty()-move.getQty());
                newStock.setUqty(newStock.getUqty()-move.getQty());
                newStock.setUpdateid(ShiroKit.getUser().getId());
                newStock.setUpdator(ShiroKit.getUser().getName());
                newStock.setUpdatetime(new Date());
                stockService.updateById(newStock);
            }
            //将原库存锁定的日志状态修改为正常
            StockLog stockLog = stockLogMapper.selectOne(new QueryWrapper<StockLog>().eq("billno",move.getMoveno()).eq("type",Constants.StockLogType.LOCKSTOCK));
            stockLog.setState(Constants.StockLogState.NORMAL);
            stockLogMapper.updateById(stockLog);
        } else {
            result.put("code", 500);
            result.put("message", "此移货记录还未审核，不用撤销审核");
            return result;
        }
        result.put("code", 200);
        result.put("message", "撤销审核成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(MoveParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(MoveParam param){
        Move oldEntity = getOldEntity(param);
        Move newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, MoveParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public MoveResult findBySpec(MoveParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<MoveResult> findListBySpec(MoveParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(MoveParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(MoveParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Move getOldEntity(MoveParam param) {
        return this.getById(getKey(param));
    }

    private Move getEntity(MoveParam param) {
        Move entity = new Move();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
