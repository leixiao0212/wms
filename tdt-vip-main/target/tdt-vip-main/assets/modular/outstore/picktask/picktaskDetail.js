layui.use(['table'], function () {
    var table = layui.table;

    /**
     * 管理
     */
    var picktaskDetail = {
        tableId: "picktaskDetailTable"
    };

    /**
     * 初始化表格的列
     */
    picktaskDetail.initColumn = function () {
        return [[
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'locatorcode', sort: true, title: '货位编码'},
            {field: 'locatorname', sort: true, title: '货位名称'},
            {field: 'qty', sort: true, title: '数量'}
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + picktaskDetail.tableId,
        url: Feng.ctxPath + '/picktask/pickTaskDetailList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: picktaskDetail.initColumn(),
        where:{pid:Feng.getUrlParam("id")}
    });

    /**
     * 管理
     */
    var picktaskOutorder = {
        tableId: "picktaskOutorderTable"
    };

    /**
     * 初始化表格的列
     */
    picktaskOutorder.initColumn = function () {
        return [[
            {field: 'outorderno', sort: true, title: '出库订单编号'}
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + picktaskOutorder.tableId,
        url: Feng.ctxPath + '/picktask/pickTaskOutOrderList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: picktaskOutorder.initColumn(),
        where:{pid:Feng.getUrlParam("id")}
    });

});
