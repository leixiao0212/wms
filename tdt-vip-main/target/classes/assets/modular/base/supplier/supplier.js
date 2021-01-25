layui.use(['table', 'ax', 'func'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;

    /**
     * 管理
     */
    var Supplier = {
        tableId: "supplierTable"
    };

    /**
     * 初始化表格的列
     */
    Supplier.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: '编号'},
            {field: 'name', sort: true, title: '供应商名称'},
            {field: 'contact', sort: true, title: '联系人'},
            {field: 'phone', sort: true, title: '联系电话'},
            {field: 'remark', sort: true, title: '备注'},
            {align: 'center', toolbar: '#tableBar', title: '操作',fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Supplier.search = function () {
        var queryData = {};
        queryData['name'] = $("#name").val();
        table.reload(Supplier.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Supplier.reset = function () {
        $("#name").val("");
        Supplier.search();
    };

    /**
     * 弹出添加对话框
     */
    Supplier.openAddDlg = function () {
        func.open({
            title: '添加',
            content: Feng.ctxPath + '/supplier/add',
            tableId: Supplier.tableId
        });
    };

    /**
    * 点击编辑
    *
    * @param data 点击按钮时候的行数据
    */
    Supplier.openEditDlg = function (data) {
        func.open({
            title: '修改',
            content: Feng.ctxPath + '/supplier/edit?id=' + data.id,
            tableId: Supplier.tableId
        });
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Supplier.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/supplier/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Supplier.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Supplier.tableId,
        url: Feng.ctxPath + '/supplier/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Supplier.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Supplier.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Supplier.reset();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Supplier.openAddDlg();
    });

    // 工具条点击事件
    table.on('tool(' + Supplier.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Supplier.openEditDlg(data);
        } else if (layEvent === 'delete') {
            Supplier.onDeleteItem(data);
        }
    });
});
