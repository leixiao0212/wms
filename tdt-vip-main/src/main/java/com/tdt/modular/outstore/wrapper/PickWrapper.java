package com.tdt.modular.outstore.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class PickWrapper extends BaseControllerWrapper {

    public PickWrapper(Map<String, Object> single) {
        super(single);
    }

    public PickWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public PickWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public PickWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将出库订单状态码转化为出库订单状态(0为初始，1为拣货中，2为已完成，3为异常待处理)
        map.put("stateName", ConstantFactory.me().getDictsByName("拣货状态",map.get("state").toString()));
    }
}
