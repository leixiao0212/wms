layui.use(['table', 'ax', 'func','form'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var func = layui.func;
    var form = layui.form;

    /**
     * 管理
     */
    var Otherout = {
        tableId: "otheroutTable"
    };

    /**
     * 初始化表格的列
     */
    Otherout.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'otheroutno', sort: true, title: '其它出库单号'},
            {field: 'locatorcode', sort: true, title: '货位编码'},
            {field: 'locatorname', sort: true, title: '货位名称'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'qty', sort: true, title: '数量'},
            {field: 'stateName', sort: true, title: '状态'},
            {align: 'center', toolbar: '#tableBar', title: '操作',fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Otherout.search = function () {
        var queryData = {};
        queryData['otheroutno'] = $("#otheroutno").val();
        queryData['state'] = $("#state").val();
        table.reload(Otherout.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Otherout.reset = function () {
        $("#otheroutno").val("");
        $("#state").val("");
        form.render('select', 'test');
        Otherout.search();
    };

    /**
     * 检查是否选中
     */
    Otherout.check = function () {
        var selected = layui.table.checkStatus('otheroutTable').data;
        if(selected.length == 0){
            Feng.info('请先选中表格中的某一记录');
            return false;
        }else{
            Otherout.seItem = selected[0];
            return true;
        }
    };

    /**
     * 点击审核按钮
     */
    Otherout.review = function () {
        if (Otherout.check()) {
            if(Otherout.seItem.state == "1"){
                Feng.info("审核失败:此订单已经审核,请勿重复审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/otherout/review", function (data) {
                    if(data.code == 200){
                        Feng.success(data.message);
                        table.reload(Otherout.tableId);
                    }else{
                        Feng.info("审核失败:" + data.responseJSON.message+"!");
                    }
                }, function (data) {
                    Feng.error("审核失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Otherout.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认审核?"+ Otherout.seItem.otheroutno + "?", operation);
        }
    };

    /**
     * 点击撤销审核按钮
     */
    Otherout.cancelReview = function () {
        if (Otherout.check()) {
            if(Otherout.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未审核,不能撤销审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/otherout/cancelReview", function (data) {
                    if (data.code == 200) {
                        Feng.success(data.message);
                        table.reload(Otherout.tableId);
                    } else {
                        Feng.info("撤销失败:" + data.message + "!");
                    }
                }, function (data) {
                    Feng.error("撤销失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Otherout.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认撤销审核?" + Otherout.seItem.otheroutno + "?", operation);
        }
    };

    /**
    * 点击编辑
    *
    * @param data 点击按钮时候的行数据
    */
    Otherout.openEditDlg = function (data) {
        if(data.state == "1"){
            Feng.info("此订单已经审核,不能修改");
            return;
        }
        func.open({
            title: '修改',
            content: Feng.ctxPath + '/otherout/edit?id=' + data.id,
            tableId: Otherout.tableId
        });
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Otherout.onDeleteItem = function (data) {
        if(data.state == "1"){
            Feng.info("此订单已经审核,不能删除");
            return;
        }
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/otherout/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Otherout.tableId);
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
        elem: '#' + Otherout.tableId,
        url: Feng.ctxPath + '/otherout/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Otherout.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Otherout.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Otherout.reset();
    });

    // 审核按钮点击事件
    $('#btnReview').click(function () {
        Otherout.review();
    });

    // 撤销审核按钮点击事件
    $('#btnCancelReview').click(function () {
        Otherout.cancelReview();
    });

    // 工具条点击事件
    table.on('tool(' + Otherout.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Otherout.openEditDlg(data);
        } else if (layEvent === 'delete') {
            Otherout.onDeleteItem(data);
        }
    });
});
