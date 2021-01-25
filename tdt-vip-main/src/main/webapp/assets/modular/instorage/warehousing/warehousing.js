layui.use(['table', 'ax', 'func', 'form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;
    var form = layui.form;

    /**
     * 管理
     */
    var Warehousing = {
        tableId: "warehousingTable"
    };

    /**
     * 初始化表格的列
     */
    Warehousing.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: '编号'},
            {field: 'warehousingno', sort: true, title: '入库单号'},
            {field: 'receiveno', sort: true, title: '接货单号'},
            {field: 'locatorcode', sort: true, title: '货位编码'},
            {field: 'stateName', sort: true, title: '状态'},
            {align: 'center', toolbar: '#tableBar', title: '操作',fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Warehousing.search = function () {
        var queryData = {};
        queryData['warehousingno'] = $("#warehousingno").val();
        queryData['receiveno'] = $("#receiveno").val();
        queryData['state'] = $("#state").val();
        table.reload(Warehousing.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Warehousing.reset = function () {
        $("#warehousingno").val("");
        $("#receiveno").val("");
        $("#state").val("");
        form.render('select', 'test');
        Warehousing.search();
    };

    /**
     * 检查是否选中
     */
    Warehousing.check = function () {
        var selected = layui.table.checkStatus('warehousingTable').data;
        if(selected.length == 0){
            Feng.info('请先选中表格中的某一记录');
            return false;
        }else{
            Warehousing.seItem = selected[0];
            return true;
        }
    };

    /**
     * 点击审核按钮
     */
    Warehousing.review = function () {
        if (Warehousing.check()) {
            if(Warehousing.seItem.state == "2"){
                Feng.info("审核失败:此订单已经审核,请勿重复审核");
                return;
            }
            if(Warehousing.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,不能审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/warehousing/review", function (data) {
                    if(data.code == 200){
                        Feng.success(data.message);
                        table.reload(Warehousing.tableId);
                    }else{
                        Feng.info("审核失败:" + data.message+"!");
                    }
                }, function (data) {
                    Feng.error("审核失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Warehousing.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认审核?"+ Warehousing.seItem.warehousingno + "?", operation);
        }
    };

    /**
     * 点击撤销审核按钮
     */
    Warehousing.cancelReview = function () {
        if (Warehousing.check()) {
            if(Warehousing.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,还未审核，无法撤销审核");
                return;
            }
            if(Warehousing.seItem.state == "1"){
                Feng.info("审核失败:此订单尚未审核,不能撤销审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/warehousing/cancelReview", function (data) {
                    if (data.code == 200) {
                        Feng.success(data.message);
                        table.reload(Warehousing.tableId);
                    } else {
                        Feng.info("撤销失败:" + data.message + "!");
                    }
                }, function (data) {
                    Feng.error("撤销失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Warehousing.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认撤销审核?" + Warehousing.seItem.warehousingno + "?", operation);
        }
    };

    /**
    * 点击编辑
    *
    * @param data 点击按钮时候的行数据
    */
    Warehousing.openEditDlg = function (data) {
        if(data.state == "2"){
            Feng.info("此订单已经审核,不能修改");
            return;
        }
        func.open({
            title: '修改',
            content: Feng.ctxPath + '/warehousing/edit?id=' + data.id,
            tableId: Warehousing.tableId
        });
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Warehousing.onDeleteItem = function (data) {
        if(data.state == "2"){
            Feng.info("此订单已经审核,不能删除");
            return;
        }
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/warehousing/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Warehousing.tableId);
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
    Warehousing.checkDetail = function (data) {
        func.open({
            title: '查看明细',
            content: Feng.ctxPath + '/warehousing/warehousing_detail?id=' + data.id,
        });
    };

    /**
     * 点击录入明细按钮
     */
    Warehousing.addDetail = function (data) {
        func.open({
            title: '录入明细',
            content: Feng.ctxPath + '/warehousing/warehousing_detail_add?id='+data.id+'&warehousingno='+data.warehousingno,
        });
    };

    /**
     * 点击打印
     *
     * @param data 点击按钮时候的行数据
     */
    Warehousing.printBar = function (data) {
        var warehousingno = data.warehousingno;
        var operation = function () {
            var ajax = new $ax(Feng.printerAddress, function (data) {
                Feng.success("打印成功!");
                table.reload(Warehousing.tableId);
            }, function (data) {
                Feng.error("打印失败!");
            });
            ajax.set("barcode", warehousingno);
            ajax.start();
        };
        Feng.confirm("是否打印"+ warehousingno +"?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Warehousing.tableId,
        url: Feng.ctxPath + '/warehousing/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Warehousing.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Warehousing.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Warehousing.reset();
    });

    // 审核按钮点击事件
    $('#btnReview').click(function () {
        Warehousing.review();
    });

    // 撤销审核按钮点击事件
    $('#btnCancelReview').click(function () {
        Warehousing.cancelReview();
    });


    // 工具条点击事件
    table.on('tool(' + Warehousing.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Warehousing.openEditDlg(data);
        } else if (layEvent === 'delete') {
            Warehousing.onDeleteItem(data);
        } else if (layEvent === 'checkDetail') {
            Warehousing.checkDetail(data);
        } else if (layEvent === 'addDetail') {
            Warehousing.addDetail(data);
        } else if (layEvent === 'print') {
            Warehousing.printBar(data);
        }
    });
});
