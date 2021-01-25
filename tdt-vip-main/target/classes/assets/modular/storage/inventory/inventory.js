layui.use(['table', 'ax', 'func'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;

    /**
     * 管理
     */
    var Inventory = {
        tableId: "inventoryTable"
    };

    /**
     * 初始化表格的列
     */
    Inventory.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: 'inventoryId'},
            {field: 'inventoryno', sort: true, title: '盘点单号'},
            {field: 'locatorcodes', sort: true, title: '货位编码'},
            {field: 'stateName', sort: true, title: '盘点状态'},
            {field: 'creator', sort: true, title: '盘点人'},
            {field: 'createtime', sort: true, title: '盘点时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作',fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Inventory.search = function () {
        var queryData = {};
        queryData['inventoryno'] = $("#inventoryno").val();
        queryData['locatorcode'] = $("#locatorcode").val();
        table.reload(Inventory.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Inventory.reset = function () {
        $("#inventoryno").val("");
        $("#locatorcode").val("");
        Inventory.search();
    };

    /**
     * 点击录入明细按钮
     */
    Inventory.checkDetail = function (data) {
        func.open({
            title: '查看盘点明细',
            content: Feng.ctxPath + '/inventory/inventoryDetail_add?id='+data.id+'&inventoryno='+data.inventoryno,
            tableId: Inventory.tableId
        });
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Inventory.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/inventory/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Inventory.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    /**
     * 点击查看盘点结果按钮
     */
    Inventory.checkResult = function (data) {
        func.open({
            title: '查看盘点结果',
            content: Feng.ctxPath + '/inventory/inventory_report?inventoryId='+data.id,
            tableId: Inventory.tableId
        });
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Inventory.tableId,
        url: Feng.ctxPath + '/inventory/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Inventory.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Inventory.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Inventory.reset();
    });

    // 工具条点击事件
    table.on('tool(' + Inventory.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'checkDetail') {
            Inventory.checkDetail(data);
        } else if (layEvent === 'delete') {
            Inventory.onDeleteItem(data);
        } else if (layEvent === 'checkResult') {
            Inventory.checkResult(data);
        }
    });
});
