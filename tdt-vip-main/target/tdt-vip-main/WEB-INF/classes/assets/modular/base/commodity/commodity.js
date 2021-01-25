layui.use(['table', 'ax', 'func'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;

    /**
     * 管理
     */
    var Commodity = {
        tableId: "commodityTable"
    };

    /**
     * 初始化表格的列
     */
    Commodity.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'bar', sort: true, title: '条码'},
            {field: 'name', sort: true, title: '商品名'},
            {field: 'alias', sort: true, title: '别名'},
            {field: 'worth', sort: true, title: '价值'},
            {field: 'nweight', sort: true, title: '净重'},
            {field: 'gweight', sort: true, title: '毛重'},
            {field: 'validday', sort: true, title: '有效天数'},
            {field: 'warehousename', sort: true, title: '所属仓库名称'},
            {field: 'remarks', sort: true, title: '备注'},
            {align: 'center', toolbar: '#tableBar', title: '操作',minWidth:200,fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Commodity.search = function () {
        var queryData = {};
        queryData['bar'] = $("#bar").val();
        queryData['name'] = $("#name").val();
        queryData['alias'] = $("#alias").val();
        table.reload(Commodity.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Commodity.reset = function () {
        $("#bar").val("");
        $("#name").val("");
        $("#alias").val("");
        Commodity.search();
    };

    /**
     * 弹出添加对话框
     */
    Commodity.openAddDlg = function () {
        func.open({
            title: '添加',
            content: Feng.ctxPath + '/commodity/add',
            tableId: Commodity.tableId
        });
    };

    /**
     * 导入按钮
     */
    Commodity.openImportDlg = function () {
        func.open({
            title: '添加',
            content: Feng.ctxPath + '/commodity/commodity_import',
            tableId: Commodity.tableId
        });
    };

    /**
     * 导出excel按钮
     */
    Commodity.exportExcel = function () {
        var checkRows = table.checkStatus(Commodity.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
    * 点击编辑
    *
    * @param data 点击按钮时候的行数据
    */
    Commodity.openEditDlg = function (data) {
        func.open({
            title: '修改商品信息',
            content: Feng.ctxPath + '/commodity/edit?id=' + data.id,
            tableId: Commodity.tableId
        });
    };


    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Commodity.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/commodity/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Commodity.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    /**
     * 点击打印
     *
     * @param data 点击按钮时候的行数据
     */
    Commodity.printBar = function (data) {
        var bar = data.bar;
        var operation = function () {
            var ajax = new $ax(Feng.printerAddress, function (data) {
                Feng.success("打印成功!");
                table.reload(Commodity.tableId);
            }, function (data) {
                Feng.error("打印失败!");
            });
            ajax.set("barcode", bar);
            ajax.start();
        };
        Feng.confirm("是否打印"+ bar +"?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Commodity.tableId,
        url: Feng.ctxPath + '/commodity/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Commodity.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Commodity.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Commodity.reset();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Commodity.openAddDlg();
    });

    // 导入按钮点击事件
    $('#btnImp').click(function () {
        Commodity.openImportDlg();
    });

    // 导出按钮点击事件
    $('#btnExp').click(function () {
        Commodity.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Commodity.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'edit') {
            Commodity.openEditDlg(data);
        } else if (layEvent === 'delete'){
            Commodity.onDeleteItem(data);
        } else if (layEvent === 'print'){
            Commodity.printBar(data);
        }
    });
});
