package com.tdt.modular.outstore.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class ReviewWrapper extends BaseControllerWrapper {

    public ReviewWrapper(Map<String, Object> single) {
        super(single);
    }

    public ReviewWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public ReviewWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public ReviewWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将出库复核状态码转化为出库订单状态(1为初始，2为复核中，2为完成，4为异常)
        map.put("stateName", ConstantFactory.me().getDictsByName("出库复核状态",map.get("state").toString()));
    }
}
