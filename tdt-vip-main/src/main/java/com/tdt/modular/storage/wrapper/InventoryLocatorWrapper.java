package com.tdt.modular.storage.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class InventoryLocatorWrapper extends BaseControllerWrapper {

    public InventoryLocatorWrapper(Map<String, Object> single) {
        super(single);
    }

    public InventoryLocatorWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public InventoryLocatorWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public InventoryLocatorWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将盘点状态码转化为货位状态(0为初始，1为盘点中,2为盘点结束)
        map.put("stateName", ConstantFactory.me().getDictsByNames("盘点状态").get(map.get("state").toString()));
    }
}
