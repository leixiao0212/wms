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
import com.tdt.modular.instorage.entity.Receive;
import com.tdt.modular.instorage.entity.ReceiveDetail;
import com.tdt.modular.instorage.entity.Warehousing;
import com.tdt.modular.instorage.entity.WarehousingDetail;
import com.tdt.modular.instorage.mapper.WarehousingDetailMapper;
import com.tdt.modular.instorage.model.params.WarehousingDetailParam;
import com.tdt.modular.instorage.model.result.WarehousingDetailResult;
import com.tdt.sys.core.shiro.ShiroKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *入库详情服务类
 */
@Service
public class WarehousingDetailService extends ServiceImpl<WarehousingDetailMapper, WarehousingDetail> {

    @Autowired
    private WarehousingService warehousingService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private ReceiveDetailService receiveDetailService;

    @Transactional(rollbackFor = Exception.class)
    public void add(WarehousingDetailParam param){
        WarehousingDetail entity = getEntity(param);
        this.save(entity);
    }
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addDetail(WarehousingDetail detail) {
        JSONObject result = new JSONObject();
        Warehousing warehousing = warehousingService.getById(detail.getPid());
        //判断入库状态是否不为已审核
        if(!warehousing.getState().equals(Constants.WarehousingState.REVIEWED)){
            Receive receive = receiveService.getOne(new QueryWrapper<Receive>().eq("receiveno",warehousing.getReceiveno()));
            ReceiveDetail receiveDetail = receiveDetailService.getOne(new QueryWrapper<ReceiveDetail>().eq("pid",receive.getId()).eq("commoditybar",detail.getCommoditybar()));
            WarehousingDetail temp = this.baseMapper.selectOne(new QueryWrapper<WarehousingDetail>().eq("pid",detail.getPid()).eq("commoditybar",detail.getCommoditybar()));
            //判断是否有次入库明细
            if(temp != null){
                //判断原数量加上当前数量是否大于接货明细中的数量
                if (temp.getQty()+detail.getQty()<=receiveDetail.getQty()){
                    temp.setQty(temp.getQty()+detail.getQty());
                    temp.setUpdateid(ShiroKit.getUser().getId());
                    temp.setUpdator(ShiroKit.getUser().getName());
                    temp.setUpdatetime(new Date());
                    this.baseMapper.updateById(temp);
                    result.put("code", 200);
                    result.put("message", "修改入库明细成功");
                } else {
                    result.put("code", 500);
                    result.put("message", "此接货单号中此商品的数量为"+receiveDetail.getQty()+",不能超出");
                }
            }else{
                Commodity commodity = commodityService.getOne(new QueryWrapper<Commodity>().eq("bar", detail.getCommoditybar()).eq("warehouseid",ShiroKit.getUser().getWarehouseId()));
                //判断商品编码是否存在且商品编码属于当前仓库
                if(commodity == null){
                    result.put("code", 500);
                    result.put("message", "商品编码不存在或此商品编码不属于当前仓库,请重新输入");
                }else{
                    //判断入库明细与接货明细是否一致
                    if (receiveDetail!=null){
                        //判断数量是否大于接货明细中的数量
                        if (detail.getQty()<=receiveDetail.getQty()){
                            detail.setCommodityid(commodity.getId());
                            detail.setCommodityname(commodity.getName());
                            detail.setCreateid(ShiroKit.getUser().getId());
                            detail.setCreator(ShiroKit.getUser().getName());
                            detail.setCreatetime(new Date());
                            this.baseMapper.insert(detail);
                            warehousing.setState(Constants.WarehousingState.UNREVIEW);
                            warehousing.setUpdateid(ShiroKit.getUser().getId());
                            warehousing.setUpdator(ShiroKit.getUser().getName());
                            warehousing.setUpdatetime(new Date());
                            warehousingService.updateById(warehousing);
                            result.put("code", 200);
                            result.put("message", "新增入库明细成功");
                        } else {
                            result.put("code", 500);
                            result.put("message", "此接货单号中此商品的数量为"+receiveDetail.getQty()+",不能超出");
                        }
                    } else {
                        result.put("code", 500);
                        result.put("message", "此入库订单明细需与接货订单一致，请联系管理员处理");
                    }
                }
            }
        }else{
            result.put("code", 500);
            result.put("message", "入库信息已经审核,不能修改");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(WarehousingDetailParam param){
        this.removeById(getKey(param));
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(WarehousingDetailParam param){
        WarehousingDetail oldEntity = getOldEntity(param);
        WarehousingDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> selectByPid(Page page,Long pid) {
        return this.baseMapper.selectByPid(page,pid);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Map<String, Object>> detailList(Page page, WarehousingDetailParam paramCondition) {
        return this.baseMapper.customMapList(page, paramCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    public WarehousingDetailResult findBySpec(WarehousingDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<WarehousingDetailResult> findListBySpec(WarehousingDetailParam param){
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public LayuiPageInfo findPageBySpec(WarehousingDetailParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(WarehousingDetailParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private WarehousingDetail getOldEntity(WarehousingDetailParam param) {
        return this.getById(getKey(param));
    }

    private WarehousingDetail getEntity(WarehousingDetailParam param) {
        WarehousingDetail entity = new WarehousingDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
