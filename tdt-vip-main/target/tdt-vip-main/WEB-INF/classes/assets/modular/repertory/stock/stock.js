layui.use(['table'], function () {
    var $ = layui.$;
    var table = layui.table;

    /**
     * 管理
     */
    var Stock = {
        tableId: "stockTable"
    };

    /**
     * 初始化表格的列
     */
    Stock.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'billno', sort: true, title: '数据来源单号'},
            {field: 'locatorcode', sort: true, title: '货位编码'},
            {field: 'locatorname', sort: true, title: '货位名称'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'cqty', sort: true, title: '总数量'},
            {field: 'uqty', sort: true, title: '可用数量'},
            {field: 'lqty', sort: true, title: '锁定数量'},
            {field: 'locatorstateName', sort: true, title: '货位状态'},
        ]];
    };

    /**
     * 点击查询按钮
     */
    Stock.search = function () {
        var queryData = {};
        queryData['locatorcode'] = $("#locatorcode").val();
        queryData['locatorname'] = $("#locatorname").val();
        queryData['commoditybar'] = $("#commoditybar").val();
        queryData['commodityname'] = $("#commodityname").val();
        table.reload(Stock.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Stock.reset = function () {
        $("#locatorcode").val("");
        $("#locatorname").val("");
        $("#commoditybar").val("");
        $("#commodityname").val("");
        Stock.search();
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Stock.tableId,
        url: Feng.ctxPath + '/stock/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Stock.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Stock.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Stock.reset();
    });

});
