layui.use(['form', 'admin', 'ax','table'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var table = layui.table;

    /**
     * 管理
     */
    var InventoryLocatorDetailInfoDlg = {
        tableId: "inventoryDetailTable",
        ischeck1:false,
        ischeck2:false
    };

    $("#locatorcode").focus();
    $("#qty").val("1");

    /**
     * 初始化表格的列
     */
    InventoryLocatorDetailInfoDlg.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: 'inventoryId'},
            {field: 'locatorcode', sort: true, title: '盘点货位编码'},
            {field: 'commodityname', sort: true, title: '商品名称'},
            {field: 'commoditybar', sort: true, title: '商品编码'},
            {field: 'qty', sort: true, title: '数量'},
            {field: 'creator', sort: true, title: '盘点人'},
            {field: 'createtime', sort: true, title: '盘点时间'},
        ]];
    };

    /**
     * 盘点
     */
    InventoryLocatorDetailInfoDlg.openAdd = function () {
        if($("#locatorcode").val()==""){
            Feng.info("货位编码不能为空");
            $("#locatorcode").focus();
        } else if($("#commoditybar").val()=="") {
            Feng.info("商品编码不能为空");
            $("#commoditybar").focus();
        } else if($("#qty").val()==""){
            Feng.info("数量不能为空");
            $("#qty").focus();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/inventory/addDetail", function (data) {
                if (data.code=='500'){
                    Feng.error('添加失败:' + data.message + "!");
                    InventoryLocatorDetailInfoDlg.resetSearch();
                } else {
                    Feng.success(data.message);
                    table.reload(InventoryLocatorDetailInfoDlg.tableId);
                    InventoryLocatorDetailInfoDlg.resetSearch();
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
    InventoryLocatorDetailInfoDlg.resetSearch = function () {
        if(!InventoryLocatorDetailInfoDlg.ischeck1){
            $("#locatorcode").val("");
        }
        $("#commoditybar").val("");
        if(!InventoryLocatorDetailInfoDlg.ischeck2){
            $("#qty").val("1");
        }
        $("#commoditybar").focus();
    };

    /**
     * 结束
     */
    InventoryLocatorDetailInfoDlg.Finish = function () {
        var ajax = new $ax(Feng.ctxPath + "/inventory/endInventory", function (data) {
            if(data.code == '200'){
                Feng.success(data.message);
                //传给上个页面，刷新table用
                admin.putTempData('formOk', true);
                //关掉对话框
                admin.closeThisDialog();
            }else{
                Feng.error('盘点结束失败:' + data.message + "!");
            }
        }, function (data) {
            Feng.error('盘点结束失败' + data.responseJSON.message + "!");
        });
        ajax.setData(getFormData());
        ajax.start();
    };

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + InventoryLocatorDetailInfoDlg.tableId,
        url: Feng.ctxPath + '/inventory/inventoryDetailList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: InventoryLocatorDetailInfoDlg.initColumn(),
        where:{pid:$("#inventoryId").val()}
    });

    // 盘点
    $('#btnAdd').click(function () {
        InventoryLocatorDetailInfoDlg.openAdd();
    });

    // 清除
    $('#btnClean').click(function () {
        InventoryLocatorDetailInfoDlg.resetSearch();
    });

    // 结束
    $('#btnFinish').click(function () {
        InventoryLocatorDetailInfoDlg.Finish();
    });

    //货位编码锁定
    form.on('checkbox(lock1)', function(data){
        if($("#locatorcode").val()==''){
        }else{
            if(data.elem.checked){
                InventoryLocatorDetailInfoDlg.ischeck1=true;
                $("#locatorcode").attr("readonly","readonly");
            }else{
                InventoryLocatorDetailInfoDlg.ischeck1=false;
                $("#locatorcode").removeAttr("readonly");
            }
        }
    });

    //数量锁定
    form.on('checkbox(lock2)', function(data){
        if($("#qty").val()==''){
        }else{
            if(data.elem.checked){
                InventoryLocatorDetailInfoDlg.ischeck2=true;
                $("#qty").attr("readonly","readonly");
            }else{
                InventoryLocatorDetailInfoDlg.ischeck2=false;
                $("#qty").removeAttr("readonly");
            }
        }
    });
});