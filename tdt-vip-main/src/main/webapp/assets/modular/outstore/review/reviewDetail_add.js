layui.use(['form', 'admin', 'ax','table'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var table = layui.table;

    /**
     * 管理
     */
    var ReviewDetail = {
        tableId: "reviewDetailTable",
        ischeck:false
    };

    $("#commoditybar").focus();
    $("#qty").val("1");

    /**
     * 初始化表格的列
     */
    ReviewDetail.initColumn = function () {
        return [[
            {field: 'id', hide: true, title: '出库复核明细id'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'qty', sort: true, title: '数量'},
        ]];
    };

    /**
     * 添加
     */
    ReviewDetail.openAdd = function () {
        if($("#commoditybar").val()==""){
            Feng.info("商品编码不能为空");
            $("#commoditybar").focus();
        } else if($("#qty").val()==""){
            Feng.info("数量不能为空");
            $("#qty").focus();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/review/addReviewDetail", function (data) {
                if (data.code=='500'){
                    Feng.error('添加失败:' + data.message + "!");
                } else {
                    Feng.success(data.message);
                    table.reload(ReviewDetail.tableId);
                    ReviewDetail.resetSearch();
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
    ReviewDetail.resetSearch = function () {
        $("#commoditybar").val("");
        if(!ReviewDetail.ischeck){
            $("#qty").val("1");
        }
        $("#commoditybar").focus();
    };

    /**
     * 结束
     */
    ReviewDetail.Finish = function () {
        var ajax = new $ax(Feng.ctxPath + "/review/finish", function (data) {
            if(data.code == '200'){
                Feng.success(data.message);
                //传给上个页面，刷新table用
                admin.putTempData('formOk', true);
                //关掉对话框
                admin.closeThisDialog();
            }else{
                Feng.error('拣货录入完成失败:' + data.message + "!");
            }
        }, function (data) {
            Feng.error('拣货录入完成失败' + data.responseJSON.message + "!");
        });
        ajax.setData(getFormData());
        ajax.start();
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + ReviewDetail.tableId,
        url: Feng.ctxPath + '/review/addDetailList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: ReviewDetail.initColumn(),
        where: {pid:$("#pid").val()}
    });

    // 添加
    $('#btnAdd').click(function () {
        ReviewDetail.openAdd();
    });

    // 清除
    $('#btnClean').click(function () {
        ReviewDetail.resetSearch();
    });

    // 结束
    $('#btnFinish').click(function () {
        ReviewDetail.Finish();
    });

    //锁定
    form.on('checkbox(lock)', function(data){
        if($("#qty").val()==''){
        }else{
            if(data.elem.checked){
                ReviewDetail.ischeck=true;
                $("#qty").attr("readonly","readonly");
            }else{
                ReviewDetail.ischeck=false;
                $("#qty").removeAttr("readonly");
            }
        }
    });

});