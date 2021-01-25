layui.use(['table', 'admin', 'ax', 'func','form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;
    var form = layui.form;

    /**
     * 管理
     */
    var Purchase = {
        tableId: "purchaseTable"
    };
    /**
     * 初始化表格的列
     */
    Purchase.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: '编号'},
            {field: 'purchaseno', sort: true, title: '采购订单编号'},
            {field: 'supplierName', sort: true, title: '供应商名称'},
            {field: 'commoditynum', sort: true, title: '商品总数量'},
            {field: 'estimatearrivaltime', sort: true, title: '预计到货时间'},
            {field: 'warehouseName', sort: true, title: '仓库名称'},
            {field: 'stateName', sort: true, title: '状态'},
            {align: 'center', toolbar: '#tableBar', title: '操作',minWidth:250,fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Purchase.search = function () {
        var queryData = {};
        queryData['purchaseno'] = $("#purchaseno").val();
        queryData['suppliername'] = $("#suppliername").val();
        queryData['state'] = $("#state").val();
        table.reload(Purchase.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Purchase.reset = function () {
        $("#purchaseno").val("");
        $("#suppliername").val("");
        $("#state").val("");
        form.render('select', 'test');
        Purchase.search();
    };

    /**
     * 弹出添加对话框
     */
    Purchase.openAddDlg = function () {
        func.open({
            title: '添加',
            content: Feng.ctxPath + '/purchase/add',
            tableId: Purchase.tableId,
            endCallback:function () {
                func.open({
                    title: '添加采购订单详情',
                    tableId: Purchase.tableId,
                    content: Feng.ctxPath + '/purchase/purchase_detail_add?id='+admin.getTempData('id') +'&purchaseno='+admin.getTempData('purchaseno') ,
                });
            }
        });
    };

    /**
     * 检查是否选中
     */
    Purchase.check = function () {
        var selected = layui.table.checkStatus('purchaseTable').data;
        if(selected.length == 0){
            Feng.info('请先选中表格中的某一记录');
            return false;
        }else{
            Purchase.seItem = selected[0];
            return true;
        }
    };

    /**
     * 点击审核按钮
     */
    Purchase.review = function () {
        if (Purchase.check()) {
            if(Purchase.seItem.state == "2"){
                Feng.info("审核失败:此订单已经审核,请勿重复审核");
                return;
            }
            if(Purchase.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,不能审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/purchase/review", function (data) {
                    if(data.code == 200){
                        Feng.success(data.message);
                        table.reload(Purchase.tableId);
                    }else{
                        Feng.info("审核失败:" + data.responseJSON.message+"!");
                    }
                }, function (data) {
                    Feng.error("审核失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Purchase.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认审核?"+ Purchase.seItem.purchaseno + "?", operation);
        }
    };

    /**
     * 点击撤销审核按钮
     */
    Purchase.cancelReview = function () {
        if (Purchase.check()) {
            if(Purchase.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,还未审核，无法撤销审核");
                return;
            }
            if(Purchase.seItem.state == "1"){
                Feng.info("审核失败:此订单尚未审核,不能撤销审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/purchase/cancelReview", function (data) {
                    if (data.code == 200) {
                        Feng.success(data.message);
                        table.reload(Purchase.tableId);
                    } else {
                        Feng.info("撤销失败:" + data.message + "!");
                    }
                }, function (data) {
                    Feng.error("撤销失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Purchase.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认撤销审核?" + Purchase.seItem.purchaseno + "?", operation);
        }
    };

    /**
    * 点击编辑
    *
    * @param data 点击按钮时候的行数据
    */
    Purchase.openEditDlg = function (data) {
            if(data.state == "2"){
                Feng.info("此订单已经审核,不能修改");
                return;
            }
            func.open({
                title: '修改',
                content: Feng.ctxPath + '/purchase/edit?id=' + data.id,
                tableId: Purchase.tableId
            });
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Purchase.onDeleteItem = function (data) {
            if(data.state == "2"){
                Feng.info("删除失败:此订单已经审核,不能删除");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/purchase/delete", function (data) {
                    Feng.success("删除成功!");
                    table.reload(Purchase.tableId);
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
    Purchase.checkDetail = function (data) {
        func.open({
            title: '查看明细',
            content: Feng.ctxPath + '/purchase/purchase_detail?id=' + data.id,
        });
    };

    /**
     * 点击录入明细按钮
     */
    Purchase.addDetail = function (data) {
        func.open({
            title: '录入明细',
            content: Feng.ctxPath + '/purchase/purchase_detail_add?id='+data.id+'&purchaseno='+data.purchaseno,
        });
    };

    /**
     * 点击打印
     *
     * @param data 点击按钮时候的行数据
     */
    Purchase.printBar = function (data) {
        var purchaseno = data.purchaseno;
        var operation = function () {
            layer.open({
                type: 2,
                id: "LAY_adminPopupR",
                offset: "r",
                anim: -1,
                title: !1,
                closeBtn: !1,
                shade: .1,
                shadeClose: !0,
                area:["0px", "0px"],
                content: Feng.ctxPath +"/purchase/prints/1"
            });
            /*var ajax = new $ax(Feng.printerAddress, function (data) {
                Feng.success("打印成功!");
                table.reload(Purchase.tableId);
            }, function (data) {
                Feng.error("打印失败!");
            });
            ajax.set("barcode", purchaseno);
            ajax.start();*/
        };
        Feng.confirm("是否打印"+ purchaseno +"?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Purchase.tableId,
        url: Feng.ctxPath + '/purchase/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Purchase.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Purchase.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Purchase.reset();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Purchase.openAddDlg();
    });

    // 审核按钮点击事件
    $('#btnReview').click(function () {
        Purchase.review();
    });

    // 撤销审核按钮点击事件
    $('#btnCancelReview').click(function () {
        Purchase.cancelReview();
    });

    // 工具条点击事件
    table.on('tool(' + Purchase.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Purchase.openEditDlg(data);
        } else if (layEvent === 'delete') {
            Purchase.onDeleteItem(data);
        } else if (layEvent === 'checkDetail') {
            Purchase.checkDetail(data);
        } else if (layEvent === 'addDetail') {
            Purchase.addDetail(data);
        } else if (layEvent === 'print'){
            Purchase.printBar(data);
        }
    });
});
