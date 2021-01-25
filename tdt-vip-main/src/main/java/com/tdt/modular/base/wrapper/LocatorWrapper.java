package com.tdt.modular.base.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.core.common.constant.ConstantsContext;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class LocatorWrapper extends BaseControllerWrapper {

    public LocatorWrapper(Map<String, Object> single) {
        super(single);
    }

    public LocatorWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public LocatorWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public LocatorWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将货位类型code转化为货位类型名称
        map.put("typeName", ConstantFactory.me().getDictsByNames("货位类型").get(map.get("type").toString()));
        //将货位状态码转化为货位状态(0为正常，1为失效,2为锁定)
        map.put("stateName", ConstantFactory.me().getDictsByNames("货位状态").get(map.get("state").toString()));
        //将仓库id转化为仓库名
        map.put("warehouseName", ConstantsContext.me().getWarehouses().get(Long.valueOf(map.get("warehouseid").toString())));
    }
}
