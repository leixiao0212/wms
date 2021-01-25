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
import com.tdt.modular.storage.entity.Puton;
import com.tdt.modular.storage.mapper.PutonMapper;
import com.tdt.modular.storage.model.params.PutonParam;
import com.tdt.modular.storage.model.result.PutonResult;
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
 * 上架管理服务类
 */
@Service
public class PutonService extends ServiceImpl<PutonMapper, Puton> {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private LocatorService locatorService;

    @Autowired
    private StockService stockService;

    @Resource
    private StockLogMapper stockLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public void add(PutonParam param){
        Puton entity = getEntity(param);
        this.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addHead(Puton puton) {
        JSONObject result = new JSONObject();
        Commodity commodity = commodityService.getOne(new QueryWrapper<Commodity>().eq("bar",puton.getCommoditybar()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
        //判断商品编码是否存在,是否是当前仓库的商品
        if(commodity != null){
            Locator oldLocator = locatorService.getOne(new QueryWrapper<Locator>().eq("code",puton.getOldlocatorcode()).eq("state",Constants.LocatorState.NORMAL).and(i -> i.eq("type",Constants.LocatorType.INSTORAGE).or().eq("type",Constants.LocatorType.BIG)).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
            //判断原货位编码是否存在,状态是否正常,类型是否为入库区或大货区，是否是当前仓库的
            if (oldLocator!=null){
                Stock oldStock= stockService.getOne(new QueryWrapper<Stock>().eq("commoditybar",puton.getCommoditybar()).eq("locatorcode",puton.getOldlocatorcode()));
                //判断原货位是否有这个商品库存
                if (oldStock!=null){
                    //判断原货位的可用数量是否大于等于上架数量
                    if (oldStock.getUqty()-puton.getQty()>=0){
                        Locator locator = locatorService.getOne(new QueryWrapper<Locator>().eq("code",puton.getLocatorcode()).eq("state",Constants.LocatorState.NORMAL).eq("type",Constants.LocatorType.COMMON).eq("warehouseid",oldLocator.getWarehouseid()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                        //判断新货位编码是否存在,状态是否正常,类型是否为普通货位,是否和原货位属于同一仓库，是否是当前仓库的
                        if (locator!=null){
                            puton.setPutonno("PO"+ DateUtil.getAllMsTime());
                            puton.setCommodityid(commodity.getId());
                            puton.setCommodityname(commodity.getName());
                            puton.setOldlocatorid(oldLocator.getId());
                            puton.setOldlocatorname(oldLocator.getName());
                            puton.setLocatorid(locator.getId());
                            puton.setLocatorname(locator.getName());
                            puton.setState(Constants.PutonState.UNREVIEW);
                            puton.setWarehouseid(ShiroKit.getUser().getWarehouseId());
                            puton.setCreateid(ShiroKit.getUser().getId());
                            puton.setCreator(ShiroKit.getUser().getName());
                            puton.setCreatetime(new Date());
                            this.baseMapper.insert(puton);
                            //将原货位库存锁定
                            oldStock.setUqty(oldStock.getUqty()-puton.getQty());
                            oldStock.setLqty(oldStock.getLqty()+puton.getQty());
                            oldStock.setUpdateid(ShiroKit.getUser().getId());
                            oldStock.setUpdator(ShiroKit.getUser().getName());
                            oldStock.setUpdatetime(new Date());
                            stockService.updateById(oldStock);
                            //库存日志记录
                            stockService.addStockLog(puton.getPutonno(),oldStock.getWarehouseid(),oldStock.getLocatorid(),oldStock.getLocatorcode(),
                                    oldStock.getLocatorname(),oldStock.getCommodityid(),oldStock.getCommoditybar(),oldStock.getCommodityname(),
                                    puton.getQty(),Constants.StockLogType.PUTONSTOCK);
                        } else {
                            result.put("code", 500);
                            result.put("message", "新货位不满足上架条件");
                            return result;
                        }
                    } else {
                        result.put("code", 500);
                        result.put("message", "原货位该商品库存可用数量为："+oldStock.getUqty()+",不能满足上架需求");
                        return result;
                    }
                } else {
                    result.put("code", 500);
                    result.put("message", "原货位没有此商品库存");
                    return result;
                }
            } else {
                result.put("code", 500);
                result.put("message", "原货位不满足上架条件");
                return result;
            }
        } else {
            result.put("code", 500);
            result.put("message", "商品编码不存在或此商品编码不属于当前仓库");
            return result;
        }
        result.put("code", 200);
        result.put("message", "上架信息添加成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject review(Long id) {
        JSONObject result = new JSONObject();
        Puton puton = this.getById(id);
        //判断上架状态是否不为已审核
        if(puton.getState().equals(Constants.PutonState.UNREVIEW)){
            puton.setState(Constants.PutonState.REVIEW);
            puton.setAuditid(ShiroKit.getUser().getId());
            puton.setAuditor(ShiroKit.getUser().getName());
            puton.setAudittime(new Date());
            this.updateById(puton);
            //将上架的日志置为失效
            StockLog stockLog = stockLogMapper.selectOne(new QueryWrapper<StockLog>().eq("billno",puton.getPutonno()).eq("type",Constants.StockLogType.PUTONSTOCK));
            stockLog.setState(Constants.StockLogState.INVALID);
            stockLogMapper.updateById(stockLog);
            Stock oldStock= stockService.getOne(new QueryWrapper<Stock>().eq("commoditybar",puton.getCommoditybar()).eq("locatorcode",puton.getOldlocatorcode()));
            ////判断原库存上架后总数量是否为0
            if (oldStock.getCqty()-puton.getQty()==0){
                stockService.removeById(oldStock.getId());
            } else {
                oldStock.setCqty(oldStock.getCqty()-puton.getQty());
                oldStock.setLqty(oldStock.getLqty()-puton.getQty());
                oldStock.setUpdateid(ShiroKit.getUser().getId());
                oldStock.setUpdator(ShiroKit.getUser().getName());
                oldStock.setUpdatetime(new Date());
                stockService.updateById(oldStock);
            }
            //记录原货位的库存日志
            stockService.addStockLog(puton.getPutonno(),oldStock.getWarehouseid(),oldStock.getLocatorid(),oldStock.getLocatorcode(),
                    oldStock.getLocatorname(),oldStock.getCommodityid(),oldStock.getCommoditybar(),oldStock.getCommodityname(),
                    puton.getQty()*(-1),Constants.StockLogType.OUTSTOCK);
            Stock newStock= stockService.getOne(new QueryWrapper<Stock>().eq("commoditybar",puton.getCommoditybar()).eq("locatorcode",puton.getLocatorcode()));
            //判断新货位是否有此商品库存
            if (newStock!=null){
                newStock.setCqty(newStock.getCqty()+puton.getQty());
                newStock.setUqty(newStock.getUqty()+puton.getQty());
                newStock.setUpdateid(ShiroKit.getUser().getId());
                newStock.setUpdator(ShiroKit.getUser().getName());
                newStock.setUpdatetime(new Date());
                stockService.updateById(newStock);
                //记录新货位的库存日志
                stockService.addStockLog(puton.getPutonno(),newStock.getWarehouseid(),newStock.getLocatorid(),
                        newStock.getLocatorcode(),newStock.getLocatorname(),newStock.getCommodityid(),
                        newStock.getCommoditybar(),newStock.getCommodityname(),puton.getQty(),Constants.StockLogType.INSTOCK);
            } else {
                Stock stock = new Stock();
                Locator locator = locatorService.getById(puton.getLocatorid());
                Commodity commodity = commodityService.getById(puton.getCommodityid());
                stock.setBillno(puton.getPutonno());
                stock.setLocatorid(locator.getId());
                stock.setLocatorcode(locator.getCode());
                stock.setLocatorname(locator.getName());
                stock.setLocatorstate(locator.getState());
                stock.setCommodityid(commodity.getId());
                stock.setCommoditybar(commodity.getBar());
                stock.setCommodityname(commodity.getName());
                stock.setCqty(puton.getQty());
                stock.setUqty(puton.getQty());
                stock.setLqty(0);
                Warehouse warehouse = warehouseService.getOne(new QueryWrapper<Warehouse>().eq("id",locator.getWarehouseid()));
                stock.setWarehouseid(warehouse.getId());
                stock.setCreateid(ShiroKit.getUser().getId());
                stock.setCreator(ShiroKit.getUser().getName());
                stock.setCreatetime(new Date());
                stockService.save(stock);
                //记录新货位的库存日志
                stockService.addStockLog(puton.getPutonno(),warehouse.getId(),locator.getId(),locator.getCode(),locator.getName(),commodity.getId(),
                        commodity.getBar(),commodity.getName(),puton.getQty(),Constants.StockLogType.INSTOCK);
            }
        } else {
            result.put("code", 500);
            result.put("message", "此上架记录已审核，请勿重复操作");
            return result;
        }
        result.put("code", 200);
        result.put("message", "审核成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject cancelReview(Long id) {
        JSONObject result = new JSONObject();
        Puton puton = this.getById(id);
        //判断上架状态是否审核
        if (puton.getState().equals(Constants.PutonState.REVIEW)){
            puton.setState(Constants.PutonState.UNREVIEW);
            this.updateById(puton);
            //将审核时入库和出库的库存日志置为失效
            List<StockLog> stockLogList = stockLogMapper.selectList(new QueryWrapper<StockLog>().eq("billno",puton.getPutonno()).and(i -> i.eq("type",Constants.StockLogType.INSTOCK)).or().eq("type",Constants.StockLogType.OUTSTOCK));
            for (StockLog _stockLog:stockLogList){
                _stockLog.setState(Constants.StockLogState.INVALID);
                stockLogMapper.updateById(_stockLog);
            }
            //将原货位扣除的数量加到总数量和锁定数量上
            Stock oldStock= stockService.getOne(new QueryWrapper<Stock>().eq("commoditybar",puton.getCommoditybar()).eq("locatorcode",puton.getOldlocatorcode()));
            oldStock.setCqty(oldStock.getCqty()+puton.getQty());
            oldStock.setLqty(oldStock.getLqty()+puton.getQty());
            oldStock.setUpdateid(ShiroKit.getUser().getId());
            oldStock.setUpdator(ShiroKit.getUser().getName());
            oldStock.setUpdatetime(new Date());
            stockService.updateById(oldStock);
            //将新货位上增加的总数量和可用数量扣除
            Stock newStock= stockService.getOne(new QueryWrapper<Stock>().eq("commoditybar",puton.getCommoditybar()).eq("locatorcode",puton.getLocatorcode()));
            //判断新货位上的总数量扣除掉上架的数量是否为空
            if (newStock.getCqty()-puton.getQty()==0){
                stockService.removeById(newStock.getId());
            } else {
                newStock.setCqty(newStock.getCqty()-puton.getQty());
                newStock.setUqty(newStock.getUqty()-puton.getQty());
                newStock.setUpdateid(ShiroKit.getUser().getId());
                newStock.setUpdator(ShiroKit.getUser().getName());
                newStock.setUpdatetime(new Date());
                stockService.updateById(newStock);
            }
            //将原库存锁定的日志状态修改为正常
            StockLog stockLog = stockLogMapper.selectOne(new QueryWrapper<StockLog>().eq("billno",puton.getPutonno()).eq("type",Constants.StockLogType.PUTONSTOCK));
            stockLog.setState(Constants.StockLogState.NORMAL);
            stockLogMapper.updateById(stockLog);
        } else {
            result.put("code", 500);
            result.put("message", "此上架记录还未审核，不用撤销审核");
            return result;
        }
        result.put("code", 200);
        result.put("message", "撤销审核成功");
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(PutonParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PutonParam param){
        Puton oldEntity = getOldEntity(param);
        Puton newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> list(Page page, PutonParam paramCondition) {
        paramCondition.setWarehouseid(ShiroKit.getUser().getWarehouseId());
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public PutonResult findBySpec(PutonParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<PutonResult> findListBySpec(PutonParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(PutonParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(PutonParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private Puton getOldEntity(PutonParam param) {
        return this.getById(getKey(param));
    }

    private Puton getEntity(PutonParam param) {
        Puton entity = new Puton();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
