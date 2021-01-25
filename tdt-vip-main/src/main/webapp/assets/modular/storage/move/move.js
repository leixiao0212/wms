layui.use(['table', 'ax', 'form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var form = layui.form;

    /**
     * 管理
     */
    var Move = {
        tableId: "moveTable"
    };

    /**
     * 初始化表格的列
     */
    Move.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: '编号'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'locatorcode', sort: true, title: '货位编码'},
            {field: 'locatorname', sort: true, title: '货位名称'},
            {field: 'oldlocatorcode', sort: true, title: '源货位编码'},
            {field: 'oldlocatorname', sort: true, title: '源货位名称'},
            {field: 'qty', sort: true, title: '数量'},
            {field: 'stateName', sort: true, title: '状态'},
            {field: 'creator', sort: true, title: '创建人'},
            {field: 'createtime', sort: true, title: '创建时间'},
            {field: 'auditor', sort: true, title: '审核人'},
            {field: 'audittime', sort: true, title: '审核时间'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Move.search = function () {
        var queryData = {};
        queryData['commoditybar'] = $("#commoditybar").val();
        queryData['commodityname'] = $("#commodityname").val();
        queryData['locatorcode'] = $("#locatorcode").val();
        queryData['locatorname'] = $("#locatorname").val();
        queryData['state'] = $("#state").val();
        table.reload(Move.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Move.reset = function () {
        $("#commoditybar").val("");
        $("#commodityname").val("");
        $("#locatorcode").val("");
        $("#locatorname").val("");
        $("#state").val("");
        form.render('select', 'test');
        Move.search();
    };

    /**
     * 检查是否选中
     */
    Move.check = function () {
        var selected = layui.table.checkStatus('moveTable').data;
        if(selected.length == 0){
            Feng.info('请先选中表格中的某一记录');
            return false;
        }else{
            Move.seItem = selected[0];
            return true;
        }
    };

    /**
     * 点击审核按钮
     */
    Move.review = function () {
        if (Move.check()) {
            if(Move.seItem.state == "2"){
                Feng.info("审核失败:此订单已经审核,请勿重复审核");
                return;
            }
            if(Move.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,不能审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/move/review", function (data) {
                    if(data.code == 200){
                        Feng.success(data.message);
                        table.reload(Move.tableId);
                    }else{
                        Feng.info("审核失败:" + data.responseJSON.message+"!");
                    }
                }, function (data) {
                    Feng.error("审核失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Move.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认审核?", operation);
        }
    };

    /**
     * 点击撤销审核按钮
     */
    Move.cancelReview = function () {
        if (Move.check()) {
            if(Move.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,还未审核，无法撤销审核");
                return;
            }
            if(Move.seItem.state == "1"){
                Feng.info("审核失败:此订单尚未审核,不能撤销审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/move/cancelReview", function (data) {
                    if (data.code == 200) {
                        Feng.success(data.message);
                        table.reload(Move.tableId);
                    } else {
                        Feng.info("撤销失败:" + data.message + "!");
                    }
                }, function (data) {
                    Feng.error("撤销失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Move.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认撤销审核?", operation);
        }
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Move.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/move/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Move.tableId);
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
        elem: '#' + Move.tableId,
        url: Feng.ctxPath + '/move/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Move.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Move.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Move.reset();
    });

    // 审核按钮点击事件
    $('#btnReview').click(function () {
        Move.review();
    });

    // 撤销审核按钮点击事件
    $('#btnCancelReview').click(function () {
        Move.cancelReview();
    });

    // 工具条点击事件
    table.on('tool(' + Move.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Move.openEditDlg(data);
        } else if (layEvent === 'delete') {
            Move.onDeleteItem(data);
        }
    });
});
