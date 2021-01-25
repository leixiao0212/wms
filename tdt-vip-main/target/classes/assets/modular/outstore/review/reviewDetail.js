layui.use(['table'], function () {
    var table = layui.table;

    /**
     * 管理
     */
    var ReviewDetail = {
        tableId: "reviewDetailTable"
    };

    /**
     * 初始化表格的列
     */
    ReviewDetail.initColumn = function () {
        return [[
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'qty', sort: true, title: '数量'}
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + ReviewDetail.tableId,
        url: Feng.ctxPath + '/review/addDetailList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: ReviewDetail.initColumn(),
        where:{pid:Feng.getUrlParam("id")}
    });
});