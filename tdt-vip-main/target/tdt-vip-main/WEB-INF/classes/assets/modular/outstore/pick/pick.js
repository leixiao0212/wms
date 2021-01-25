layui.use(['table', 'ax', 'func', 'form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;
    var form = layui.form;

    /**
     * 管理
     */
    var Pick = {
        tableId: "pickTable"
    };

    /**
     * 初始化表格的列
     */
    Pick.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'pickno', sort: true, title: '拣货单号'},
            {field: 'picktaskno', sort: true, title: '拣货任务单号'},
            {field: 'stateName', sort: true, title: '状态'},
            {field: 'creator', sort: true, title: '创建人'},
            {field: 'createtime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作',fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Pick.search = function () {
        var queryData = {};
        queryData['pickno'] = $("#pickno").val();
        queryData['picktaskno'] = $("#picktaskno").val();
        queryData['state'] = $("#state").val();
        table.reload(Pick.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 清空
     */
    Pick.reset = function () {
        $("#pickno").val("");
        $("#picktaskno").val("");
        $("#state").val("");
        form.render('select', 'test');
        Pick.search();
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Pick.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/pick/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Pick.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    /**
     * 点击查看明细按钮
     */
    Pick.checkDetail = function (data) {
        func.open({
            title: '查看明细',
            content: Feng.ctxPath + '/pick/checkDetail?id=' + data.id,
        });
    };

    /**
     * 点击录入明细按钮
     */
    Pick.addDetail = function (data) {
        func.open({
            title: '录入明细',
            content: Feng.ctxPath + '/pick/add_detail?id='+data.id+'&pickno='+data.pickno,
        });
    };

    /**
     * 点击还原按钮
     */
    Pick.restore = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + '/pick/restore?id='+data.id, function (data) {
                Feng.success("还原成功!");
                table.reload(Pick.tableId);
            }, function (data) {
                Feng.error("还原失败!" + data.responseJSON.message + "!");
            });
            ajax.start();
        };
        Feng.confirm("是否还原?", operation);
    }

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Pick.tableId,
        url: Feng.ctxPath + '/pick/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Pick.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Pick.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Pick.reset();
    });

    // 删除按钮点击事件
    $('#btnDelete').click(function () {
        Pick.onDeleteItem();
    });

    // 工具条点击事件
    table.on('tool(' + Pick.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'addDetail') {
            Pick.addDetail(data);
        }  else if (layEvent === 'checkDetail') {
            Pick.checkDetail(data);
        } else if (layEvent === 'restore') {
            Pick.restore(data);
        }
    });
});
