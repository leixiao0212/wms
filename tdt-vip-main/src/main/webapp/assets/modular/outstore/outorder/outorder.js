layui.use(['table', 'admin', 'ax', 'func', 'form', 'layarea', 'laydate'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;
    var form = layui.form;
    var layarea = layui.layarea;
    var laydate = layui.laydate;

    //省市区三级联动
    layarea.render({
        elem: '#area-picker',
    });

    //日期范围
    var startDate=laydate.render({
        elem: '#startTime',
        type: 'datetime',
        trigger: 'click',
        done:function(value,date){
            if(value!=""){
                date.month=date.month-1;
                endDate.config.min=date;
            }else{
                endDate.config.min=startDate.config.min;
            }
        },
    });
    var endDate=laydate.render({
        elem: '#endTime',
        type: 'datetime',
        trigger: 'click',
        done:function(value,date){
            if(value!=""){
                date.month=date.month-1;
                startDate.config.max=date;
            }else{
                startDate.config.max=endDate.config.max;
            }
        }
    });

    var ids =[];

    /**
     * 管理
     */
    var Outorder = {
        tableId: "outorderTable",
    };

    /**
     * 初始化表格的列
     */
    Outorder.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: ''},
            {field: 'outorderno', sort: true, title: '出库单号'},
            {field: 'category', sort: true, title: '种类'},
            {field: 'qty', sort: true, title: '商品总数量'},
            {field: 'stateName', sort: true, title: '状态'},
            {field: 'receiver', sort: true, title: '收件人'},
            {field: 'province', sort: true, title: '所在省'},
            {field: 'city', sort: true, title: '所在市'},
            {field: 'county', sort: true, title: '所在区/县'},
            {field: 'address', sort: true, title: '详细地址'},
            {field: 'depottime', sort: true, title: '出库时间'},
            {field: 'creator', sort: true, title: '创建人'},
            {field: 'createtime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作',minWidth:150,fixed: 'right'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Outorder.search = function () {
        var queryData = {};
        queryData['outorderno'] = $("#outorderno").val();
        queryData['state'] = $("#state").val();
        queryData['receiver'] = $("#receiver").val();
        queryData['startTime'] = $("#startTime").val();
        queryData['endTime'] = $("#endTime").val();
        queryData['province'] = $("#province").val();
        queryData['city'] = $("#city").val();
        queryData['county'] = $("#county").val();
        table.reload(Outorder.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    Outorder.reset = function () {
        $("#outorderno").val("");
        $("#state").val("");
        $("#receiver").val("");
        $("#startTime").val("");
        $("#endTime").val("");
        form.render('select', 'test');
        //省市区三级联动
        layarea.render({
            elem: '#area-picker',
            data: {
                province: "请选择省",
                city: "请选择市",
                county: "请选择区/县"
            }
        });
        Outorder.search();
    };

    /**
     * 弹出添加对话框
     */
    Outorder.openAddDlg = function () {
        func.open({
            title: '添加',
            content: Feng.ctxPath + '/outorder/add',
            tableId: Outorder.tableId,
            endCallback:function () {
                func.open({
                    title: '添加出库订单订单详情',
                    tableId: Outorder.tableId,
                    content: Feng.ctxPath + '/outorder/outorder_detail_add?id='+admin.getTempData('id') +'&outorderno='+admin.getTempData('outorderno') ,
                });
            }
        });
    };

    /**
     * 检查是否选中
     */
    Outorder.check = function () {
        var selected = layui.table.checkStatus('outorderTable').data;
        if(selected.length == 0){
            Feng.info('请先选中表格中的某一记录');
            return false;
        }else{
            Outorder.seItem = selected[0];
            return true;
        }
    };

    /**
     * 点击编辑
     *
     * @param data 点击按钮时候的行数据
     */
    Outorder.openEditDlg = function () {
        if (Outorder.check()) {
            if(Outorder.seItem.state == "2"){
                Feng.info("此订单已经审核,不能修改");
                return;
            } else if (Outorder.seItem.state == "3"){
                Feng.info("此订单已分配,不能修改");
                return;
            } else if (Outorder.seItem.state == "4"){
                Feng.info("此订单在拣货中,不能修改");
                return;
            } else if (Outorder.seItem.state == "5"){
                Feng.info("此订单已出库,不能修改");
                return;
            } else if (Outorder.seItem.state == "9"){
                Feng.info("此订单已取消,不能修改");
                return;
            }
            func.open({
                title: '修改',
                content: Feng.ctxPath + '/outorder/edit?id=' + Outorder.seItem.id,
                tableId: Outorder.tableId
            });
        }
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    Outorder.onDeleteItem = function () {
        if (Outorder.check()) {
            if(Outorder.seItem.state == "2"){
                Feng.info("此订单已经审核,不能删除");
                return;
            } else if (Outorder.seItem.state == "3"){
                Feng.info("此订单已分配,不能删除");
                return;
            } else if (Outorder.seItem.state == "4"){
                Feng.info("此订单在拣货中,不能删除");
                return;
            } else if (Outorder.seItem.state == "5"){
                Feng.info("此订单已出库,不能删除");
                return;
            } else if (Outorder.seItem.state == "9"){
                Feng.info("此订单已取消,不能删除");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/outorder/delete", function (data) {
                    Feng.success("删除成功!");
                    table.reload(Outorder.tableId);
                }, function (data) {
                    Feng.error("删除失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Outorder.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否删除?", operation);
        }
    };

    /**
     * 点击审核按钮
     */
    Outorder.review = function () {
        if (Outorder.check()) {
            if(Outorder.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,不能审核");
                return;
            } else if(Outorder.seItem.state == "2"){
                Feng.info("审核失败:此订单已经审核,请勿重复审核");
                return;
            } else if(Outorder.seItem.state == "3"){
                Feng.info("审核失败:此订单已分配,请勿重复审核");
                return;
            } else if(Outorder.seItem.state == "4"){
                Feng.info("审核失败:此订单在拣货中,请勿重复审核");
                return;
            } else if(Outorder.seItem.state == "5"){
                Feng.info("审核失败:此订单已出库,请勿重复审核");
                return;
            } else if (Outorder.seItem.state == "9"){
                Feng.info("此订单已取消,无需审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/outorder/review", function (data) {
                    if(data.code == 200){
                        Feng.success(data.message);
                        table.reload(Outorder.tableId);
                    }else{
                        Feng.info("审核失败:" + data.responseJSON.message+"!");
                    }
                }, function (data) {
                    Feng.error("审核失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Outorder.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认审核?"+ Outorder.seItem.outorderno + "?", operation);
        }
    };

    /**
     * 点击撤销审核按钮
     */
    Outorder.cancelReview = function () {
        if (Outorder.check()) {
            if(Outorder.seItem.state == "0"){
                Feng.info("审核失败:此订单尚未录入结束,还未审核，无法撤销审核");
                return;
            } else if(Outorder.seItem.state == "1"){
                Feng.info("审核失败:此订单尚未审核,不能撤销审核");
                return;
            } else if(Outorder.seItem.state == "3"){
                Feng.info("审核失败:此订单已分配,不能撤销审核");
                return;
            } else if(Outorder.seItem.state == "4"){
                Feng.info("审核失败:此订单在拣货中,不能撤销审核");
                return;
            } else if(Outorder.seItem.state == "5"){
                Feng.info("审核失败:此订单已出库,不能撤销审核");
                return;
            } else if (Outorder.seItem.state == "9"){
                Feng.info("此订单已取消,无需撤销审核");
                return;
            }
            var operation = function () {
                var ajax = new $ax(Feng.ctxPath + "/outorder/cancelReview", function (data) {
                    if (data.code == 200) {
                        Feng.success(data.message);
                        table.reload(Outorder.tableId);
                    } else {
                        Feng.info("撤销失败:" + data.message + "!");
                    }
                }, function (data) {
                    Feng.error("撤销失败!" + data.responseJSON.message + "!");
                });
                ajax.set("id", Outorder.seItem.id);
                ajax.start();
            };
            Feng.confirm("是否确认撤销审核?" + Outorder.seItem.outorderno + "?", operation);
        }
    };

    /**
     * 点击查看明细按钮
     */
    Outorder.checkDetail = function (data) {
        func.open({
            title: '查看明细',
            content: Feng.ctxPath + '/outorder/outorder_detail?id=' + data.id,
        });
    };

    /**
     * 点击录入明细按钮
     */
    Outorder.addDetail = function (data) {
        func.open({
            title: '录入明细',
            content: Feng.ctxPath + '/outorder/outorder_detail_add?id='+data.id+'&outorderno='+data.outorderno,
        });
    };

    //表格头部工具栏监听事件
    table.on('checkbox(' + Outorder.tableId + ')', function () {
        var datas = [];
        var table_data=table.checkStatus('' + Outorder.tableId + '').data;
        for(var i=0;i<table_data.length;i++){
            datas.push(table_data[i]);
        }
        for (var i=0;i<datas.length;i++){
            ids[i] = datas[i].id;
        }
    });

    /**
     * 点击生成拣货任务按钮
     */
    Outorder.pick = function () {
        if (ids.length > 0){
            var ajax = new $ax(Feng.ctxPath + "/outorder/pick", function (data) {
                if (data.code == "200"){
                    Feng.success(data.message);
                    table.reload(Outorder.tableId);
                } else {
                    Feng.error('生成出库任务失败' + data.message + "!");
                }
            }, function (data) {
                Feng.error("生成出库任务失败!" + data.responseJSON.message + "!");
            });
            ajax.set("ids", ids.toString());
            ajax.start();
        } else {
            Feng.info("必须选中至少一条出库订单数据!");
        }
    };

    /**
     * 点击取消出库订单按钮
     */
    Outorder.remove = function () {
        if (Outorder.check()) {
            if(Outorder.seItem.state == "9"){
                Feng.info("此订单已出库,无法取消");
                return;
            }
            var ajax = new $ax(Feng.ctxPath + "/outorder/removeOutorder", function (data) {
                if (data.code == "200"){
                    Feng.success(data.message);
                    table.reload(Outorder.tableId);
                } else {
                    Feng.error('出库订单取消失败' + data.message + "!");
                }
            }, function (data) {
                Feng.error("出库订单取消失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", Outorder.seItem.id);
            ajax.start();
        }
    };

    /**
     * 点击打印
     *
     * @param data 点击按钮时候的行数据
     */
    Outorder.printBar = function (data) {
        var outorderno = data.outorderno;
        var operation = function () {
            var ajax = new $ax(Feng.printerAddress, function (data) {
                Feng.success("打印成功!");
                table.reload(Outorder.tableId);
            }, function (data) {
                Feng.error("打印失败!");
            });
            ajax.set("barcode", outorderno);
            ajax.start();
        };
        Feng.confirm("是否打印"+ outorderno +"?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Outorder.tableId,
        url: Feng.ctxPath + '/outorder/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: Outorder.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Outorder.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        Outorder.reset();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Outorder.openAddDlg();
    });

    // 修改按钮点击事件
    $('#btnEdit').click(function () {
        Outorder.openEditDlg();
    });

    // 删除按钮点击事件
    $('#btnDelete').click(function () {
        Outorder.onDeleteItem();
    });

    // 审核按钮点击事件
    $('#btnReview').click(function () {
        Outorder.review();
    });

    // 撤销审核按钮点击事件
    $('#btnCancelReview').click(function () {
        Outorder.cancelReview();
    });

    // 生成拣货任务按钮点击事件
    $('#btnPick').click(function () {
        Outorder.pick();
    });

    // 取消出库订单按钮点击事件
    $('#btnRemove').click(function () {
        Outorder.remove();
    });

    // 工具条点击事件
    table.on('tool(' + Outorder.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'addDetail') {
            Outorder.addDetail(data);
        }  else if (layEvent === 'checkDetail') {
            Outorder.checkDetail(data);
        } else if (layEvent === 'print') {
            Outorder.printBar(data);
        }
    });
});
