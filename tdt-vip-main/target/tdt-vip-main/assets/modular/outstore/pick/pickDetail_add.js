layui.use(['form', 'admin', 'ax', 'table'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var table = layui.table;

    //让当前iframe弹层高度适应
    admin.iframeAuto();

    $("#commoditybar").focus();
    $("#qty").val("1");

    /**
     * 管理
     */
    var PickDetail = {
        tableId: "pickDetailTable",
        ischeck:false
    };

    /**
     * 初始化表格的列
     */
    PickDetail.initColumn = function () {
        return [[
            {field: 'id', hide: true, title: '拣货明细id'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'locatorcode', sort: true, title: '货位编码'},
            {field: 'locatorname', sort: true, title: '货位名称'},
            {field: 'qty', sort: true, title: '数量'},
        ]];
    };

    /**
     * 添加
     */
    PickDetail.openAdd = function () {
        if($("#commoditybar").val()==""){
            Feng.info("商品编码不能为空");
            $("#commoditybar").focus();
        } else if($("#locatorcode").val()==""){
            Feng.info("货位编码不能为空");
            $("#locatorcode").focus();
        } else if($("#qty").val()==""){
            Feng.info("数量不能为空");
            $("#qty").focus();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/pick/addPickDetail", function (data) {
                if (data.code=='500'){
                    Feng.error('添加失败:' + data.message + "!");
                    PickDetail.resetSearch();
                } else {
                    Feng.success(data.message);
                    table.reload(PickDetail.tableId);
                    PickDetail.resetSearch();
                }
            }, function (data) {
                Feng.error("添加失败!" + data.responseJSON.message + "!");
            });
            ajax.setData(getFormData());
            ajax.start();
        }
    };

    function getFormData(){
        return $("input,select,textarea").serializeArray();
    };

    /**
     * 清空
     */
    PickDetail.resetSearch = function () {
        $("#commoditybar").val("");
        $("#locatorcode").val("");
        if(!PickDetail.ischeck){
            $("#qty").val("1");
        }
        $("#commoditybar").focus();
    };

    /**
     * 结束
     */
    PickDetail.Finish = function () {
        var ajax = new $ax(Feng.ctxPath + "/pick/finish", function (data) {
            if(data.code == '200'){
                Feng.success(data.message);
                //传给上个页面，刷新table用
                admin.putTempData('formOk', true);
                //关掉对话框
                admin.closeThisDialog();
            }else{
                var operation = function () {
                    Feng.error(data.message);
                    var ajax = new $ax(Feng.ctxPath + "/pick/confirmChangeState", function (data) {
                        Feng.success("标记成功!");
                        admin.closeThisDialog();
                    }, function (data) {
                        Feng.error("标记失败!" + data.responseJSON.message + "!");
                    });
                    ajax.set("pickid", data.pick.id);
                    ajax.start();
                };
                Feng.confirm("是否将此拣货状态标记为异常?", operation);
            }
        }, function (data) {
            Feng.error('拣货录入完成失败' + data.responseJSON.message + "!");
        });
        ajax.setData(getFormData());
        ajax.start();
    };

    //拣货明细与拣货任务不一致，确认是否将此拣货状态标记为异常
    PickDetail.confirmChangeState = function () {
        var operation = function () {
            console.log(1);
        };
        Feng.confirm("是否将此拣货状态标记为异常?", operation);
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + PickDetail.tableId,
        url: Feng.ctxPath + '/pick/addDetailList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: PickDetail.initColumn(),
        where: {pid:$("#pid").val()}
    });

    // 添加
    $('#btnAdd').click(function () {
        PickDetail.openAdd();
    });

    // 清除
    $('#btnClean').click(function () {
        PickDetail.resetSearch();
    });

    // 结束
    $('#btnFinish').click(function () {
        PickDetail.Finish();
    });

    //锁定
    form.on('checkbox(lock)', function(data){
        if($("#qty").val()==''){
        }else{
            if(data.elem.checked){
                PickDetail.ischeck=true;
                $("#qty").attr("readonly","readonly");
            }else{
                PickDetail.ischeck=false;
                $("#qty").removeAttr("readonly");
            }
        }
    });

});