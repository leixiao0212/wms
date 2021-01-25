package com.tdt.modular.instorage.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class ReceiveWrapper extends BaseControllerWrapper {

    public ReceiveWrapper(Map<String, Object> single) {
        super(single);
    }

    public ReceiveWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public ReceiveWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public ReceiveWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将接货状态码转化为货位状态(0为初始，1为接货中，2为已审核)
        map.put("stateName", ConstantFactory.me().getDictsByName("接货订单状态",map.get("state").toString()));
    }
}
