package com.tdt.modular.outstore.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class OutorderWrapper extends BaseControllerWrapper {

    public OutorderWrapper(Map<String, Object> single) {
        super(single);
    }

    public OutorderWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public OutorderWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public OutorderWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将出库订单状态码转化为出库订单状态(0为初始，1为待审核，2为已审核)
        map.put("stateName", ConstantFactory.me().getDictsByName("出库订单状态",map.get("state").toString()));
    }
}
