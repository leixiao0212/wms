layui.use(['form', 'admin', 'ax','table'], function () {
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
    var ReceiveDetail = {
        tableId: "receiveDetailTable",
        ischeck:false
    };

    /**
     * 初始化表格的列
     */
    ReceiveDetail.initColumn = function () {
        return [[
            {type: 'radio'},
            {field: 'id', hide: true, title: '接货详情id'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'qty', sort: true, title: '数量'},
        ]];
    };

    /**
     * 添加
     */
    ReceiveDetail.openAdd = function () {
        if($("#commoditybar").val()==""){
            Feng.info("商品编码不能为空");
            $("#commoditybar").focus();
        } else if($("#qty").val()==""){
            Feng.info("数量不能为空");
            $("#qty").focus();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/receive/addDetail", function (data) {
                if (data.code=='500'){
                    Feng.error('添加失败:' + data.message + "!");
                    ReceiveDetail.resetSearch();
                } else {
                    Feng.success(data.message);
                    table.reload(ReceiveDetail.tableId);
                    ReceiveDetail.resetSearch();
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
    ReceiveDetail.resetSearch = function () {
        $("#commoditybar").val("");
        if(!ReceiveDetail.ischeck){
            $("#qty").val("1");
        }
        $("#commoditybar").focus();
    };

    /**
     * 结束
     */
    ReceiveDetail.Finish = function () {
        //关掉对话框
        admin.closeThisDialog();
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + ReceiveDetail.tableId,
        url: Feng.ctxPath + '/receive/addDetailList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: ReceiveDetail.initColumn(),
        where: {pid:$("#pid").val()}
    });

    // 添加
    $('#btnAdd').click(function () {
        ReceiveDetail.openAdd();
    });

    // 清除
    $('#btnClean').click(function () {
        ReceiveDetail.resetSearch();
    });

    // 结束
    $('#btnFinish').click(function () {
        ReceiveDetail.Finish();
    });

    //锁定
    form.on('checkbox(lock)', function(data){
        if($("#qty").val()==''){
        }else{
            if(data.elem.checked){
                ReceiveDetail.ischeck=true;
                $("#qty").attr("readonly","readonly");
            }else{
                ReceiveDetail.ischeck=false;
                $("#qty").removeAttr("readonly");
            }
        }
    });

});