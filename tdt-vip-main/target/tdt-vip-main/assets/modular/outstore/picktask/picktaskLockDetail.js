layui.use(['table', 'ax', 'func'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;

    /**
     * 管理
     */
    var PicktaskLockDetail = {
        tableId: "picktaskLockDetailTable"
    };

    /**
     * 初始化表格的列
     */
    PicktaskLockDetail.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'picktaskno', sort: true, title: '拣货任务单号'},
            {field: 'outorderid', sort: true, title: '出库订单id'},
            {field: 'outorderno', sort: true, title: '出库订单单号'},
            {field: 'commodityid', sort: true, title: '商品id'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'locatorid', sort: true, title: '货位id'},
            {field: 'locatorcode', sort: true, title: '货位编码'},
            {field: 'locatorname', sort: true, title: '货位名称'},
            {field: 'qty', sort: true, title: '数量'},
            {align: 'center', toolbar: '#tableBar', title: '操作',fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    PicktaskLockDetail.search = function () {
        var queryData = {};
        queryData['condition'] = $("#condition").val();
        table.reload(PicktaskLockDetail.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 弹出添加对话框
     */
    PicktaskLockDetail.openAddDlg = function () {
        func.open({
            title: '添加',
            content: Feng.ctxPath + '/picktaskLockDetail/add',
            tableId: PicktaskLockDetail.tableId
        });
    };

    /**
    * 点击编辑
    *
    * @param data 点击按钮时候的行数据
    */
    PicktaskLockDetail.openEditDlg = function (data) {
        func.open({
            title: '修改',
            content: Feng.ctxPath + '/picktaskLockDetail/edit?id=' + data.id,
            tableId: PicktaskLockDetail.tableId
        });
    };

    /**
     * 导出excel按钮
     */
    PicktaskLockDetail.exportExcel = function () {
        var checkRows = table.checkStatus(PicktaskLockDetail.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    PicktaskLockDetail.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/picktaskLockDetail/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(PicktaskLockDetail.tableId);
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
        elem: '#' + PicktaskLockDetail.tableId,
        url: Feng.ctxPath + '/picktaskLockDetail/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: PicktaskLockDetail.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        PicktaskLockDetail.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        PicktaskLockDetail.openAddDlg();
    });

    // 导出excel
    $('#btnExp').click(function () {
        PicktaskLockDetail.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + PicktaskLockDetail.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            PicktaskLockDetail.openEditDlg(data);
        } else if (layEvent === 'delete') {
            PicktaskLockDetail.onDeleteItem(data);
        }
    });
});
