layui.use(['table'], function () {
    var table = layui.table;

    /**
     * 管理
     */
    var OutorderDetail = {
        tableId: "outorderDetailTable"
    };

    /**
     * 初始化表格的列
     */
    OutorderDetail.initColumn = function () {
        return [[
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名'},
            {field: 'qty', sort: true, title: '数量'},
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + OutorderDetail.tableId,
        url: Feng.ctxPath + '/outorder/addDetailList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: OutorderDetail.initColumn(),
        where:{pid:Feng.getUrlParam("id")}
    });
});
