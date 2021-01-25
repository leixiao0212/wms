layui.use(['table', 'ax', 'func', 'form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;
    var form = layui.form;

    /**
     * 管理
     */
    var Review = {
        tableId: "reviewTable"
    };

    /**
     * 初始化表格的列
     */
    Review.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'outorderno', sort: true, title: '出库订单编号'},
            {field: 'stateName', sort: true, title: '状态'},
            {field: 'creator', sort: true, title: '创建人'},
            {field: 'createtime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作',fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Review.search = function () {
        var queryData = {};
        queryData['outorderno'] = $("#outorderno").val();
        queryData['state'] = $("#state").val();
        table.reload(Review.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 清空
     */
    Review.reset = function () {
        $("#outorderno").val("");
        $("#state").val("");
        form.render('select', 'test');
        Review.search();
    };

    /**
     * 点击查看明细按钮
     */
    Review.checkDetail = function (data) {
        func.open({
            title: '查看明细',
            content: Feng.ctxPath + '/review/checkDetail?id=' + data.id,
        });
    };

    /**
     * 点击录入明细按钮
     */
    Review.addDetail = function (data) {
        func.open({
            title: '录入明细',
            content: Feng.ctxPath + '/review/add_detail?id='+data.id,
        });
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Review.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/review/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Review.tableId);
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
        elem: '#' + Review.tableId,
        url: Feng.ctxPath + '/review/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Review.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Review.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Review.reset();
    });

    // 工具条点击事件
    table.on('tool(' + Review.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'addDetail') {
            Review.addDetail(data);
        }  else if (layEvent === 'checkDetail') {
            Review.checkDetail(data);
        } else if (layEvent === 'delete') {
            Review.onDeleteItem(data);
        }
    });
});
