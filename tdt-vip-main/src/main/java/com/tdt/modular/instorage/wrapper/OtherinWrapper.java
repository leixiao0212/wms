package com.tdt.modular.instorage.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class OtherinWrapper extends BaseControllerWrapper {

    public OtherinWrapper(Map<String, Object> single) {
        super(single);
    }

    public OtherinWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public OtherinWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public OtherinWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将入库状态码转化为入库审核状态(0为未审核，1为已审核)
        map.put("stateName", ConstantFactory.me().getDictsByName("其他入库审核状态",map.get("state").toString()));
    }
}
