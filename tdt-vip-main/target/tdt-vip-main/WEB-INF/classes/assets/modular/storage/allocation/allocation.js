layui.use(['table', 'ax', 'func', 'form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;
    var form = layui.form;

    /**
     * 管理
     */
    var Allocation = {
        tableId: "allocationTable"
    };

    /**
     * 初始化表格的列
     */
    Allocation.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: '调拨编号'},
            {field: 'allocationno', sort: true, title: '调拨单号'},
            {field: 'bwarehousename', sort: true, title: '原仓库'},
            {field: 'awarehousename', sort: true, title: '目标仓库'},
            {field: 'stateName', sort: true, title: '状态'},
            {field: 'creator', sort: true, title: '创建人'},
            {field: 'createtime', sort: true, title: '创建时间'},
            {field: 'auditor', sort: true, title: '审核人'},
            {field: 'audittime', sort: true, title: '审核时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作',minWidth:200,fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Allocation.search = function () {
        var queryData = {};
        queryData['allocationno'] = $("#allocationno").val();
        queryData['state'] = $("#state").val();
        table.reload(Allocation.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Allocation.Reset = function () {
        $("#allocationno").val("");
        $("#state").val("");
        form.render('select', 'test');
        Allocation.search();
    };

    /**
     * 检查是否选中
     */
    Allocation.check = function () {
        var selected = layui.table.checkStatus('allocationTable').data;
        if(selected.length == 0){
            Feng.info('请先选中表格中的某一记录');
            return false;
        }else{
            Allocation.seItem = selected[0];
            return true;
        }
    };

    /**
     * 点击审核按钮
     */
    Allocation.review = function () {
        if (Allocation.check()) {
            if(Allocation.seItem.state == "2"){
                Feng.info("审核失败:此订单已经审核,请勿重复审核");
                return;
            }
            if(Allocation.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,不能审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/allocation/review", function (data) {
                    if(data.code == 200){
                        Feng.success(data.message);
                        table.reload(Allocation.tableId);
                    }else{
                        Feng.info("审核失败:" + data.responseJSON.message+"!");
                    }
                }, function (data) {
                    Feng.error("审核失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Allocation.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认审核?"+ Allocation.seItem.allocationno + "?", operation);
        }
    };

    /**
     * 点击撤销审核按钮
     */
    Allocation.cancelReview = function () {
        if (Allocation.check()) {
            if(Allocation.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,还未审核，无法撤销审核");
                return;
            }
            if(Allocation.seItem.state == "1"){
                Feng.info("审核失败:此订单尚未审核,不能撤销审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/allocation/cancelReview", function (data) {
                    if (data.code == 200) {
                        Feng.success(data.message);
                        table.reload(Allocation.tableId);
                    } else {
                        Feng.info("撤销失败:" + data.message + "!");
                    }
                }, function (data) {
                    Feng.error("撤销失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Allocation.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认撤销审核?" + Allocation.seItem.allocationno + "?", operation);
        }
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Allocation.onDeleteItem = function (data) {
        if(data.state == 2){
            Feng.info("删除失败:调拨信息已经审核,不能删除");
            return;
        }
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/allocation/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Allocation.tableId);
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
    Allocation.checkDetail = function (data) {
        func.open({
            title: '查看明细',
            content: Feng.ctxPath + '/allocation/allocation_detail?id='+data.id,
        });
    };

    /**
     * 点击录入明细按钮
     */
    Allocation.addDetail = function (data) {
        func.open({
            title: '录入明细',
            content: Feng.ctxPath + '/allocation/allocationDetail_add?id='+data.id+'&allocationno='+data.allocationno,
        });
    };

    /**
     * 点击打印
     *
     * @param data 点击按钮时候的行数据
     */
    Allocation.printBar = function (data) {
        var allocationno = data.allocationno;
        var operation = function () {
            var ajax = new $ax(Feng.printerAddress, function (data) {
                Feng.success("打印成功!");
                table.reload(Allocation.tableId);
            }, function (data) {
                Feng.error("打印失败!");
            });
            ajax.set("barcode", allocationno);
            ajax.start();
        };
        Feng.confirm("是否打印"+ allocationno +"?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Allocation.tableId,
        url: Feng.ctxPath + '/allocation/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Allocation.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Allocation.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Allocation.Reset();
    });

    // 审核按钮点击事件
    $('#btnReview').click(function () {
        Allocation.review();
    });

    // 撤销审核按钮点击事件
    $('#btnCancelReview').click(function () {
        Allocation.cancelReview();
    });

    // 工具条点击事件
    table.on('tool(' + Allocation.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Allocation.openEditDlg(data);
        } else if (layEvent === 'delete') {
            Allocation.onDeleteItem(data);
        } else if (layEvent === 'checkDetail') {
            Allocation.checkDetail(data);
        } else if (layEvent === 'addDetail') {
            Allocation.addDetail(data);
        } else if (layEvent === 'print') {
            Allocation.printBar(data);
        }
    });
});
