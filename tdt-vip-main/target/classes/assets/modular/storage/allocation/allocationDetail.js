layui.use(['table'], function () {
    var table = layui.table;

    /**
     * 管理
     */
    var AllocationDetail = {
        tableId: "allocationDetailTable"
    };

    /**
     * 初始化表格的列
     */
    AllocationDetail.initColumn = function () {
        return [[
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'qty', sort: true, title: '数量'},
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + AllocationDetail.tableId,
        url: Feng.ctxPath + '/allocation/detailList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: AllocationDetail.initColumn(),
        where:{id:Feng.getUrlParam("id")}
    });

});
