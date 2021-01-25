layui.use(['table', 'ax', 'func', 'form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;
    var form = layui.form;

    /**
     * 管理
     */
    var Locator = {
        tableId: "locatorTable"
    };

    /**
     * 初始化表格的列
     */
    Locator.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'name', sort: true, title: '货位名称'},
            {field: 'code', sort: true, title: '货位编码'},
            {field: 'typeName', sort: true, title: '库位类型'},
            {field: 'stateName', sort: true, title: '货位状态'},
            {field: 'warehouseName', sort: true, title: '所属仓库'},
            {align: 'center', toolbar: '#tableBar', title: '操作',fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Locator.search = function () {
        var queryData = {};
        queryData['name'] = $("#name").val();
        queryData['code'] = $("#code").val();
        queryData['type'] = $("#type").val();
        table.reload(Locator.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 弹出添加对话框
     */
    Locator.openAddDlg = function () {
        func.open({
            title: '添加',
            content: Feng.ctxPath + '/locator/add',
            tableId: Locator.tableId
        });
    };

    /**
     * 点击重置按钮
     */
    Locator.reset = function () {
        $("#name").val("");
        $("#code").val("");
        $("#type").val("");
        form.render('select', 'test');
        Locator.search();
    };

    /**
    * 点击编辑
    *
    * @param data 点击按钮时候的行数据
    */
    Locator.openEditDlg = function (data) {
        func.open({
            title: '修改',
            content: Feng.ctxPath + '/locator/edit?id=' + data.id,
            tableId: Locator.tableId
        });
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Locator.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/locator/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Locator.tableId);
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
    Locator.printBar = function (data) {
        var code = data.code;
        var operation = function () {
            var ajax = new $ax(Feng.printerAddress, function (data) {
                Feng.success("打印成功!");
                table.reload(Locator.tableId);
            }, function (data) {
                Feng.error("打印失败!");
            });
            ajax.set("barcode", code);
            ajax.start();
        };
        Feng.confirm("是否打印"+ code +"?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Locator.tableId,
        url: Feng.ctxPath + '/locator/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Locator.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Locator.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Locator.reset();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Locator.openAddDlg();
    });

    // 工具条点击事件
    table.on('tool(' + Locator.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Locator.openEditDlg(data);
        } else if (layEvent === 'delete') {
            Locator.onDeleteItem(data);
        } else if (layEvent === 'print') {
            Locator.printBar(data);
        }
    });
});
