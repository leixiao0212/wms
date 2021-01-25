package com.tdt.modular.storage.wrapper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tdt.sys.core.constant.factory.ConstantFactory;

import java.util.List;
import java.util.Map;

public class MoveWrapper extends BaseControllerWrapper {

    public MoveWrapper(Map<String, Object> single) {
        super(single);
    }

    public MoveWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public MoveWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public MoveWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        //将移货状态码转化为调拨状态(1为待审核,2为已审核)
        map.put("stateName", ConstantFactory.me().getDictsByNames("移库状态").get(map.get("state").toString()));
    }
}
