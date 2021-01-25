package com.tdt.modular.outstore.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class OtheroutWrapper extends BaseControllerWrapper {

    public OtheroutWrapper(Map<String, Object> single) {
        super(single);
    }

    public OtheroutWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public OtheroutWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public OtheroutWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将其他出库状态码转化为其他出库单状态(0为未审核，1为已审核)
        map.put("stateName", ConstantFactory.me().getDictsByName("其他出库审核状态",map.get("state").toString()));
    }
}
