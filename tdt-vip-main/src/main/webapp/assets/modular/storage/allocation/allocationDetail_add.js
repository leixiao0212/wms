layui.use(['form', 'admin', 'ax','table'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var table = layui.table;

    /**
     * 管理
     */
    var AllocationDetailInfoDlg = {
        tableId: "allocationDetailTable",
        ischeck:false,
    };

    $("#locatorcode").focus();
    $("#qty").val("1");

    /**
     * 初始化表格的列
     */
    AllocationDetailInfoDlg.initColumn = function () {
        return [[
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'qty', sort: true, title: '数量'},
        ]];
    };

    /**
     * 添加
     */
    AllocationDetailInfoDlg.openAdd = function () {
        if($("#commoditybar").val()==""){
            Feng.info("商品编码不能为空");
            $("#commoditybar").focus();
        } else if($("#qty").val()==""){
            Feng.info("数量不能为空");
            $("#qty").focus();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/allocation/addDetail", function (data) {
                if (data.code=='500'){
                    Feng.error('添加失败:' + data.message + "!");
                } else {
                    Feng.success(data.message);
                    table.reload(AllocationDetailInfoDlg.tableId);
                    AllocationDetailInfoDlg.resetSearch();
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
    AllocationDetailInfoDlg.resetSearch = function () {
        $("#locatorcode").val("");
        $("#commoditybar").val("");
        if(!AllocationDetailInfoDlg.ischeck){
            $("#qty").val("1");
        }
        $("#commoditybar").focus();
    };

    /**
     * 结束
     */
    AllocationDetailInfoDlg.Finish = function () {
        var ajax = new $ax(Feng.ctxPath + "/allocation/finish", function (data) {
            if(data.code == '200'){
                Feng.success(data.message);
                //关掉对话框
                admin.closeThisDialog();
            }else{
                Feng.error('调拨结束失败:' + data.message + "!");
            }
        }, function (data) {
            Feng.error('调拨结束失败' + data.responseJSON.message + "!");
        });
        ajax.setData(getFormData());
        ajax.start();
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + AllocationDetailInfoDlg.tableId,
        url: Feng.ctxPath + '/allocation/allocationDetailList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: AllocationDetailInfoDlg.initColumn(),
        where:{pid:$("#pid").val()}
    });

    // 添加
    $('#btnAdd').click(function () {
        AllocationDetailInfoDlg.openAdd();
    });

    // 清除
    $('#btnClean').click(function () {
        AllocationDetailInfoDlg.resetSearch();
    });

    // 结束
    $('#btnFinish').click(function () {
        AllocationDetailInfoDlg.Finish();
    });

    //锁定
    form.on('checkbox(lock)', function(data){
        if($("#qty").val()==''){
        }else{
            if(data.elem.checked){
                AllocationDetailInfoDlg.ischeck=true;
                $("#qty").attr("readonly","readonly");
            }else{
                AllocationDetailInfoDlg.ischeck=false;
                $("#qty").removeAttr("readonly");
            }
        }
    });
});