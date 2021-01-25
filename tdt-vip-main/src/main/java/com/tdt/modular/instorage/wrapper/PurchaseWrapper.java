package com.tdt.modular.instorage.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.core.common.constant.ConstantsContext;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class PurchaseWrapper extends BaseControllerWrapper {

    public PurchaseWrapper(Map<String, Object> single) {
        super(single);
    }

    public PurchaseWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public PurchaseWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public PurchaseWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将供应商id转化为供应商名称
        map.put("supplierName",ConstantsContext.me().getSuppliers().get(Long.valueOf(map.get("supplierid").toString())));
        //将仓库id转化为仓库名称
        map.put("warehouseName",ConstantsContext.me().getWarehouses().get(Long.valueOf(map.get("warehouseid").toString())));
        //将货位状态码转化为货位状态(0为未审核，1为待审核，2为已审核)
        map.put("stateName", ConstantFactory.me().getDictsByName("采购订单状态",map.get("state").toString()));
    }
}
