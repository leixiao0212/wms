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
    var PurchaseDetail = {
        tableId: "purchaseDetailTable",
        ischeck:false
    };

    /**
     * 初始化表格的列
     */
    PurchaseDetail.initColumn = function () {
        return [[
            {type: 'radio'},
            {field: 'id', hide: true, title: '采购订单详情id'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'qty', sort: true, title: '数量'},
        ]];
    };

    /**
     * 添加
     */
    PurchaseDetail.openAdd = function () {
        if($("#commoditybar").val()==""){
            Feng.info("商品编码不能为空");
            $("#commoditybar").focus();
        } else if($("#qty").val()==""){
            Feng.info("数量不能为空");
            $("#qty").focus();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/purchase/addDetail", function (data) {
                if (data.code=='500'){
                    Feng.error('添加失败:' + data.message + "!");
                    PurchaseDetail.resetSearch();
                } else {
                    Feng.success(data.message);
                    table.reload(PurchaseDetail.tableId);
                    PurchaseDetail.resetSearch();
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
    PurchaseDetail.resetSearch = function () {
        $("#commoditybar").val("");
        if(!PurchaseDetail.ischeck){
            $("#qty").val("1");
        }
        $("#commoditybar").focus();
    };

    /**
     * 结束
     */
    PurchaseDetail.Finish = function () {
        var ajax = new $ax(Feng.ctxPath + "/purchase/finish", function (data) {
            if(data.code == '200'){
                Feng.success(data.message);
                //传给上个页面，刷新table用
                admin.putTempData('formOk', true);
                //关掉对话框
                admin.closeThisDialog();
            }else{
                Feng.error('采购订单录入完成失败:' + data.message + "!");
            }
        }, function (data) {
            Feng.error('采购订单录入完成失败' + data.responseJSON.message + "!");
        });
        ajax.setData(getFormData());
        ajax.start();
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + PurchaseDetail.tableId,
        url: Feng.ctxPath + '/purchase/addDetailList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: PurchaseDetail.initColumn(),
        where: {pid:$("#pid").val()}
    });

    // 添加
    $('#btnAdd').click(function () {
        PurchaseDetail.openAdd();
    });

    // 清除
    $('#btnClean').click(function () {
        PurchaseDetail.resetSearch();
    });

    // 结束
    $('#btnFinish').click(function () {
        PurchaseDetail.Finish();
    });

    //锁定
    form.on('checkbox(lock)', function(data){
        if($("#qty").val()==''){
        }else{
            if(data.elem.checked){
                PurchaseDetail.ischeck=true;
                $("#qty").attr("readonly","readonly");
            }else{
                PurchaseDetail.ischeck=false;
                $("#qty").removeAttr("readonly");
            }
        }
    });
});