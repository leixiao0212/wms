package com.tdt.modular.repertory.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class StockWrapper extends BaseControllerWrapper {

    public StockWrapper(Map<String, Object> single) {
        super(single);
    }

    public StockWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public StockWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public StockWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将货位状态码转化为货位状态(0为正常，1为失效，2为锁定)
        map.put("locatorstateName", ConstantFactory.me().getDictsByName("货位状态",map.get("locatorstate").toString()));
    }
}
