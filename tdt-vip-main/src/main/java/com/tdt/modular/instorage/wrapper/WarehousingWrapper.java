package com.tdt.modular.instorage.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class WarehousingWrapper extends BaseControllerWrapper {

    public WarehousingWrapper(Map<String, Object> single) {
        super(single);
    }

    public WarehousingWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public WarehousingWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public WarehousingWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将入库状态码转化为入库审核状态(0为入库中，1为待审核，2为已审核)
        map.put("stateName", ConstantFactory.me().getDictsByName("入库管理审核状态",map.get("state").toString()));
    }
}
