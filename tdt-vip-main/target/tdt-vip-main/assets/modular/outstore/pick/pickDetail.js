layui.use(['table'], function () {
    var table = layui.table;

    /**
     * 管理
     */
    var PickDetail = {
        tableId: "pickDetailTable"
    };

    /**
     * 初始化表格的列
     */
    PickDetail.initColumn = function () {
        return [[
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名'},
            {field: 'locatorcode', sort: true, title: '货位编码'},
            {field: 'locatorname', sort: true, title: '货位名称'},
            {field: 'qty', sort: true, title: '数量'}
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + PickDetail.tableId,
        url: Feng.ctxPath + '/pick/addDetailList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: PickDetail.initColumn(),
        where:{pid:Feng.getUrlParam("id")}
    });
});
