layui.use(['table', 'ax', 'func', 'form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;
    var form = layui.form;

    /**
     * 管理
     */
    var Receive = {
        tableId: "receiveTable"
    };

    /**
     * 初始化表格的列
     */
    Receive.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'receiveno', sort: true, title: '接货单号'},
            {field: 'purchaseno', sort: true, title: '采购单号'},
            {field: 'allocationno', sort: true, title: '调拨单号'},
            {field: 'remarks', sort: true, title: '备注'},
            {field: 'creator', sort: true, title: '接货人'},
            {field: 'createtime', sort: true, title: '接货时间'},
            {field: 'auditor', sort: true, title: '审批人'},
            {field: 'audittime', sort: true, title: '审批时间'},
            {field: 'stateName', sort: true, title: '状态'},
            {align: 'center', toolbar: '#tableBar', title: '操作',minWidth:250,fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Receive.search = function () {
        var queryData = {};
        queryData['receiveno'] = $("#receiveno").val();
        queryData['purchaseno'] = $("#purchaseno").val();
        queryData['allocationno'] = $("#allocationno").val();
        queryData['state'] = $("#state").val();
        table.reload(Receive.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Receive.reset = function () {
        $("#receiveno").val("");
        $("#purchaseno").val("");
        $("#allocationno").val("");
        $("#state").val("");
        form.render('select', 'test');
        Receive.search();
    };

    /**
     * 检查是否选中
     */
    Receive.check = function () {
        var selected = layui.table.checkStatus('receiveTable').data;
        if(selected.length == 0){
            Feng.info('请先选中表格中的某一记录');
            return false;
        }else{
            Receive.seItem = selected[0];
            return true;
        }
    };

    /**
     * 点击审核按钮
     */
    Receive.review = function () {
        if (Receive.check()) {
            if(Receive.seItem.state == "2"){
                Feng.info("审核失败:此订单已经审核,请勿重复审核");
                return;
            }
            if(Receive.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,不能审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/receive/review", function (data) {
                    if(data.code == 200){
                        Feng.success(data.message);
                        table.reload(Receive.tableId);
                    }else{
                        Feng.info("审核失败:" + data.responseJSON.message+"!");
                    }
                }, function (data) {
                    Feng.error("审核失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Receive.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认审核?"+ Receive.seItem.receiveno + "?", operation);
        }
    };

    /**
     * 点击撤销审核按钮
     */
    Receive.cancelReview = function () {
        if (Receive.check()) {
            if(Receive.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,还未审核，无法撤销审核");
                return;
            }
            if(Receive.seItem.state == "1"){
                Feng.info("审核失败:此订单尚未审核,不能撤销审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/receive/cancelReview", function (data) {
                    if (data.code == 200) {
                        Feng.success(data.message);
                        table.reload(Receive.tableId);
                    } else {
                        Feng.info("撤销失败:" + data.message + "!");
                    }
                }, function (data) {
                    Feng.error("撤销失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Receive.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认撤销审核?" + Receive.seItem.receiveno + "?", operation);
        }
    };


    /**
    * 点击编辑
    *
    * @param data 点击按钮时候的行数据
    */
    Receive.openEditDlg = function (data) {
        if(data.state == "2"){
            Feng.info("此订单已经审核,不能修改");
            return;
        }
        func.open({
            title: '修改',
            content: Feng.ctxPath + '/receive/edit?id=' + data.id,
            tableId: Receive.tableId
        });
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Receive.onDeleteItem = function (data) {
        if(data.state == "2"){
            Feng.info("此订单已经审核,不能删除");
            return;
        }
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/receive/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Receive.tableId);
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
    Receive.checkDetail = function (data) {
        func.open({
            title: '查看明细',
            content: Feng.ctxPath + '/receive/receive_detail?id=' + data.id,
        });
    };

    /**
     * 点击打印
     *
     * @param data 点击按钮时候的行数据
     */
    Receive.printBar = function (data) {
        var receiveno = data.receiveno;
        var operation = function () {
            var ajax = new $ax(Feng.printerAddress, function (data) {
                Feng.success("打印成功!");
                table.reload(Receive.tableId);
            }, function (data) {
                Feng.error("打印失败!");
            });
            ajax.set("barcode", receiveno);
            ajax.start();
        };
        Feng.confirm("是否打印"+ receiveno +"?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Receive.tableId,
        url: Feng.ctxPath + '/receive/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Receive.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Receive.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Receive.reset();
    });

    // 审核按钮点击事件
    $('#btnReview').click(function () {
        Receive.review();
    });

    // 撤销审核按钮点击事件
    $('#btnCancelReview').click(function () {
        Receive.cancelReview();
    });

    // 工具条点击事件
    table.on('tool(' + Receive.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Receive.openEditDlg(data);
        } else if (layEvent === 'delete') {
            Receive.onDeleteItem(data);
        } else if (layEvent === 'checkDetail') {
            Receive.checkDetail(data);
        } else if (layEvent === 'print') {
            Receive.printBar(data);
        }
    });
});
