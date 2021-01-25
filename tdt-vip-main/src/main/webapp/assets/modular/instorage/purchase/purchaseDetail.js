layui.use(['table'], function () {
    var table = layui.table;

    /**
     * 管理
     */
    var PurchaseDetail = {
        tableId: "purchaseDetailTable"
    };

    /**
     * 初始化表格的列
     */
    PurchaseDetail.initColumn = function () {
        return [[
            {field: 'id', hide: true, title: '编号'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'qty', sort: true, title: '数量'}
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + PurchaseDetail.tableId,
        url: Feng.ctxPath + '/purchase/detailList?',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: PurchaseDetail.initColumn(),
        where:{id:Feng.getUrlParam("id")}
    });
});
