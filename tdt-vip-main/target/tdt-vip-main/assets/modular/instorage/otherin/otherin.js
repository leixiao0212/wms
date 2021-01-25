layui.use(['table', 'ax', 'func','form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;
    var form = layui.form;

    /**
     * 管理
     */
    var Otherin = {
        tableId: "otherinTable"
    };

    /**
     * 初始化表格的列
     */
    Otherin.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'otherinno', sort: true, title: '入库单号'},
            {field: 'locatorname', sort: true, title: '货位名称'},
            {field: 'locatorcode', sort: true, title: '货位编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'qty', sort: true, title: '数量'},
            {field: 'stateName', sort: true, title: '状态'},
            {align: 'center', toolbar: '#tableBar', title: '操作',fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Otherin.search = function () {
        var queryData = {};
        queryData['otherinno'] = $("#otherinno").val();
        queryData['state'] = $("#state").val();
        table.reload(Otherin.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Otherin.reset = function () {
        $("#otherinno").val("");
        $("#state").val("");
        form.render('select', 'test');
        Otherin.search();
    };

    /**
     * 检查是否选中
     */
    Otherin.check = function () {
        var selected = layui.table.checkStatus('otherinTable').data;
        if(selected.length == 0){
            Feng.info('请先选中表格中的某一记录');
            return false;
        }else{
            Otherin.seItem = selected[0];
            return true;
        }
    };

    /**
     * 点击审核按钮
     */
    Otherin.review = function () {
        if (Otherin.check()) {
            if(Otherin.seItem.state == "1"){
                Feng.info("审核失败:此订单已经审核,请勿重复审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/otherin/review", function (data) {
                    if(data.code == 200){
                        Feng.success(data.message);
                        table.reload(Otherin.tableId);
                    }else{
                        Feng.info("审核失败:" + data.responseJSON.message+"!");
                    }
                }, function (data) {
                    Feng.error("审核失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Otherin.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认审核?"+ Otherin.seItem.otherinno + "?", operation);
        }
    };

    /**
     * 点击撤销审核按钮
     */
    Otherin.cancelReview = function () {
        if (Otherin.check()) {
            if(Otherin.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未审核,不能撤销审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/otherin/cancelReview", function (data) {
                    if (data.code == 200) {
                        Feng.success(data.message);
                        table.reload(Otherin.tableId);
                    } else {
                        Feng.info("撤销失败:" + data.message + "!");
                    }
                }, function (data) {
                    Feng.error("撤销失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Otherin.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认撤销审核?" + Otherin.seItem.otherinno + "?", operation);
        }
    };

    /**
    * 点击编辑
    *
    * @param data 点击按钮时候的行数据
    */
    Otherin.openEditDlg = function (data) {
        if(data.state == "1"){
            Feng.info("此订单已经审核,不能修改");
            return;
        }
        func.open({
            title: '修改',
            content: Feng.ctxPath + '/otherin/edit?id=' + data.id,
            tableId: Otherin.tableId
        });
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Otherin.onDeleteItem = function (data) {
        if(data.state == "1"){
            Feng.info("此订单已经审核,不能删除");
            return;
        }
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/otherin/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Otherin.tableId);
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
    Otherin.printBar = function (data) {
        var otherinno = data.otherinno;
        var operation = function () {
            var ajax = new $ax(Feng.printerAddress, function (data) {
                Feng.success("打印成功!");
                table.reload(Otherin.tableId);
            }, function (data) {
                Feng.error("打印失败!");
            });
            ajax.set("barcode", otherinno);
            ajax.start();
        };
        Feng.confirm("是否打印"+ otherinno +"?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Otherin.tableId,
        url: Feng.ctxPath + '/otherin/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Otherin.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Otherin.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Otherin.reset();
    });

    // 审核按钮点击事件
    $('#btnReview').click(function () {
        Otherin.review();
    });

    // 撤销审核按钮点击事件
    $('#btnCancelReview').click(function () {
        Otherin.cancelReview();
    });

    // 工具条点击事件
    table.on('tool(' + Otherin.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Otherin.openEditDlg(data);
        } else if (layEvent === 'delete') {
            Otherin.onDeleteItem(data);
        } else if (layEvent === 'print') {
            Otherin.printBar(data);
        }
    });
});
