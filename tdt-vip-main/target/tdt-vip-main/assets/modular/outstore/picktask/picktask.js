layui.use(['table', 'ax', 'func', 'form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;
    var form = layui.form;

    /**
     * 管理
     */
    var Picktask = {
        tableId: "picktaskTable"
    };

    /**
     * 初始化表格的列
     */
    Picktask.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'picktaskno', sort: true, title: '拣货任务单号'},
            {field: 'receiver', sort: true, title: '领取人'},
            {field: 'receivetime', sort: true, title: '领取时间'},
            {field: 'creator', sort: true, title: '创建人'},
            {field: 'createtime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作',fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Picktask.search = function () {
        var queryData = {};
        queryData['outorderno'] = $("#outorderno").val();
        queryData['picktaskno'] = $("#picktaskno").val();
        queryData['isReceive'] = $("#isReceive").val();
        queryData['receiver'] = $("#receiver").val();
        table.reload(Picktask.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Picktask.reset = function () {
        $("#outorderno").val("");
        $("#picktaskno").val("");
        $("#isReceive").val("");
        $("#receiver").val("");
        form.render('select', 'test');
        Picktask.search();
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Picktask.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/picktask/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Picktask.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    /**
     * 点击查看明细按钮按钮
     */
    Picktask.checkDetail = function (data) {
        func.open({
            title: '查看明细',
            content: Feng.ctxPath + '/picktask/picktaskDetail?id=' + data.id,
        });
    };

    /**
     * 点击打印
     *
     * @param data 点击按钮时候的行数据
     */
    Picktask.printBar = function (data) {
        var picktaskno = data.picktaskno;
        var operation = function () {
            var ajax = new $ax(Feng.printerAddress, function (data) {
                Feng.success("打印成功!");
                table.reload(Picktask.tableId);
            }, function (data) {
                Feng.error("打印失败!");
            });
            ajax.set("barcode", picktaskno);
            ajax.start();
        };
        Feng.confirm("是否打印"+ picktaskno +"?", operation);
    };

    /**
     * 点击还原按钮
     */
    Picktask.restore = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + '/picktask/restore?id='+data.id, function (data) {
                Feng.success("还原成功!");
                table.reload(Picktask.tableId);
            }, function (data) {
                Feng.error("还原失败!" + data.responseJSON.message + "!");
            });
            ajax.start();
        };
        Feng.confirm("是否还原?", operation);
    }

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Picktask.tableId,
        url: Feng.ctxPath + '/picktask/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Picktask.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Picktask.search();
    });

    $('#btnReset').click(function () {
        Picktask.reset();
    });

    // 工具条点击事件
    table.on('tool(' + Picktask.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'delete') {
            Picktask.onDeleteItem(data);
        }  else if (layEvent === 'checkDetail') {
            Picktask.checkDetail(data);
        } else if (layEvent === 'print') {
            Picktask.printBar(data);
        } else if (layEvent === 'restore') {
            Picktask.restore(data);
        }
    });
});
