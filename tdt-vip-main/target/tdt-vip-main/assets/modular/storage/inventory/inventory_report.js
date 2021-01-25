layui.use(['table'], function () {
    var table = layui.table;

    /**
     * 盘点明细管理
     */
    var InventoryDetail = {
        tableId: "inventoryDetailTable"
    };

    /**
     * 初始化表格的列
     */
    InventoryDetail.initColumn = function () {
        return [[
            {type: 'radio'},
            {field: 'locatorcode', sort: true, title: '盘点货位编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'qty', sort: true, title: '数量'},
            {field: 'creator', sort: true, title: '盘点人'},
            {field: 'createtime', sort: true, title: '盘点时间'},
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + InventoryDetail.tableId,
        url: Feng.ctxPath + '/inventory/inventory_detail_list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: InventoryDetail.initColumn(),
        where:{pid:Feng.getUrlParam("inventoryId")}
    });







    /**
     * 盘盈管理
     */
    var InventoryProfit = {
        tableId: "inventoryProfitTable"
    };

    /**
     * 初始化表格的列
     */
    InventoryProfit.initColumn = function () {
        return [[
            {type: 'radio'},
            {field: 'locatorcode', sort: true, title: '盘点货位编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'qty', sort: true, title: '数量'},
            {field: 'creator', sort: true, title: '盘点人'},
            {field: 'createtime', sort: true, title: '盘点时间'},
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + InventoryProfit.tableId,
        url: Feng.ctxPath + '/inventory/inventory_profit_list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: InventoryProfit.initColumn(),
        where:{pid:Feng.getUrlParam("inventoryId")}
    });






    /**
     * 盘亏管理
     */
    var InventoryLoss = {
        tableId: "inventoryLossTable"
    };

    /**
     * 初始化表格的列
     */
    InventoryLoss.initColumn = function () {
        return [[
            {type: 'radio'},
            {field: 'locatorcode', sort: true, title: '盘点货位编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'qty', sort: true, title: '数量'},
            {field: 'creator', sort: true, title: '盘点人'},
            {field: 'createtime', sort: true, title: '盘点时间'},
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + InventoryLoss.tableId,
        url: Feng.ctxPath + '/inventory/inventory_loss_list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: InventoryLoss.initColumn(),
        where:{pid:Feng.getUrlParam("inventoryId")}
    });

});
