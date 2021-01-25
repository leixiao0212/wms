layui.use(['table', 'ax', 'form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var form = layui.form;

    /**
     * 管理
     */
    var Puton = {
        tableId: "putonTable"
    };

    /**
     * 初始化表格的列
     */
    Puton.initColumn = function () {
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
    Puton.search = function () {
        var queryData = {};
        queryData['commoditybar'] = $("#commoditybar").val();
        queryData['commodityname'] = $("#commodityname").val();
        queryData['locatorcode'] = $("#locatorcode").val();
        queryData['locatorname'] = $("#locatorname").val();
        queryData['state'] = $("#state").val();
        table.reload(Puton.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Puton.reset = function () {
        $("#commoditybar").val("");
        $("#commodityname").val("");
        $("#locatorcode").val("");
        $("#locatorname").val("");
        $("#state").val("");
        form.render('select', 'test');
        Puton.search();
    };

    /**
     * 检查是否选中
     */
    Puton.check = function () {
        var selected = layui.table.checkStatus('putonTable').data;
        if(selected.length == 0){
            Feng.info('请先选中表格中的某一记录');
            return false;
        }else{
            Puton.seItem = selected[0];
            return true;
        }
    };

    /**
     * 点击审核按钮
     */
    Puton.review = function () {
        if (Puton.check()) {
            if(Puton.seItem.state == "2"){
                Feng.info("审核失败:此订单已经审核,请勿重复审核");
                return;
            }
            if(Puton.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,不能审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/puton/review", function (data) {
                    if(data.code == 200){
                        Feng.success(data.message);
                        table.reload(Puton.tableId);
                    }else{
                        Feng.info("审核失败:" + data.responseJSON.message+"!");
                    }
                }, function (data) {
                    Feng.error("审核失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Puton.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认审核?", operation);
        }
    };

    /**
     * 点击撤销审核按钮
     */
    Puton.cancelReview = function () {
        if (Puton.check()) {
            if(Puton.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,还未审核，无法撤销审核");
                return;
            }
            if(Puton.seItem.state == "1"){
                Feng.info("审核失败:此订单尚未审核,不能撤销审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/puton/cancelReview", function (data) {
                    if (data.code == 200) {
                        Feng.success(data.message);
                        table.reload(Puton.tableId);
                    } else {
                        Feng.info("撤销失败:" + data.message + "!");
                    }
                }, function (data) {
                    Feng.error("撤销失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Puton.seItem.id);
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
    Puton.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/puton/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Puton.tableId);
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
        elem: '#' + Puton.tableId,
        url: Feng.ctxPath + '/puton/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Puton.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Puton.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Puton.reset();
    });

    // 审核按钮点击事件
    $('#btnReview').click(function () {
        Puton.review();
    });

    // 撤销审核按钮点击事件
    $('#btnCancelReview').click(function () {
        Puton.cancelReview();
    });

    // 工具条点击事件
    table.on('tool(' + Puton.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Puton.openEditDlg(data);
        } else if (layEvent === 'delete') {
            Puton.onDeleteItem(data);
        }
    });
});
