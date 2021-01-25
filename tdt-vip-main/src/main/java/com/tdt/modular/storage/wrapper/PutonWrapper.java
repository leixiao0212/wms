package com.tdt.modular.storage.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class PutonWrapper extends BaseControllerWrapper {

    public PutonWrapper(Map<String, Object> single) {
        super(single);
    }

    public PutonWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public PutonWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public PutonWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将上架状态码转化为调拨状态(0为初始，1为待审核,2为已审核)
        map.put("stateName", ConstantFactory.me().getDictsByNames("上架状态").get(map.get("state").toString()));
    }
}
