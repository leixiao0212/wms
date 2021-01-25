layui.use(['ax','table','func'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var table = layui.table;
    var func = layui.func;

    /**
     * 管理
     */
    var InventoryLocatorDetail = {
        tableId: "inventoryLocatorDetailTable",
        locatorcodes: [],
    };

    /**
     * 初始化表格的列
     */
    InventoryLocatorDetail.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: '货位id'},
            {field: 'code', sort: true, title: '货位编码'},
            {field: 'name', sort: true, title: '货位名称'},
            {field: 'typeName', sort: true, title: '货位类型'},
            {field: 'stateName', sort: true, title: '货位状态'},
            {field: 'warehouseName', sort: true, title: '所属仓库'},
        ]];
    };

    /**
     * 点击查询按钮
     */
    InventoryLocatorDetail.search = function () {
        var queryData = {};
        queryData['code'] = $("#code").val();
        queryData['name'] = $("#name").val();
        table.reload(InventoryLocatorDetail.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 点击重置按钮
     */
    InventoryLocatorDetail.reset = function () {
        $("#code").val("");
        $("#name").val("");
        $("#selected").val("");
        InventoryLocatorDetail.locatorcodes=[];
        InventoryLocatorDetail.search();
    };

    table.on('checkbox(' + InventoryLocatorDetail.tableId + ')', function (obj) {
        var datas=[];
        if(obj.type=='one'){
            datas.push(obj.data);
        }else{
            var table_data=table.checkStatus('' + InventoryLocatorDetail.tableId + '').data;
            for(var i=0;i<table_data.length;i++){
                datas.push(table_data[i]);
            }
        }
        examine(obj.checked,datas);
    });

    /**
     * 选中盘点的记录，显示盘点编码
     */
    function examine(checked,datas){
        if(checked){
            for(var i=0;i<datas.length;i++){
                var data=datas[i];
                if($.inArray(data.code, InventoryLocatorDetail.locatorcodes) == -1){
                    InventoryLocatorDetail.locatorcodes.push(data.code);
                }
                $("#selected").val(InventoryLocatorDetail.locatorcodes.toString());
            }
        }else{
            for(var i=0;i<datas.length;i++){
                var data=datas[i];
                if($.inArray(data.code, InventoryLocatorDetail.locatorcodes) > -1){
                    var index = InventoryLocatorDetail.locatorcodes.indexOf(data.code);
                    InventoryLocatorDetail.locatorcodes.splice(index,1);
                }
                $("#selected").val(InventoryLocatorDetail.locatorcodes.toString());
            }
        }
    }

    /**
     * 点击盘点按钮
     */
    InventoryLocatorDetail.add = function () {
        if (InventoryLocatorDetail.locatorcodes.length > 0){
            var ajax = new $ax(Feng.ctxPath + "/inventory/addInventory", function (data) {
                if (data.code == "200"){
                    Feng.success(data.message);
                    InventoryLocatorDetail.reset();
                    InventoryLocatorDetail.addDetail(data.inventory.id,data.inventory.inventoryno);
                } else {
                    Feng.error('盘点失败' + data.message + "!");
                }
            }, function (data) {
                Feng.error("盘点失败!" + data.responseJSON.message + "!");
            });
            ajax.set("locatorCodes", InventoryLocatorDetail.locatorcodes.toString());
            ajax.start();
        } else {
            Feng.info("盘点货位不能为空!");
        }
    };

    /**
     * 点击盘点全部货位按钮
     */
    InventoryLocatorDetail.addAll = function () {
        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/inventory/addAll", function (data) {
                if (data.code == "200"){
                    Feng.success(data.message);
                    InventoryLocatorDetail.reset();
                    InventoryLocatorDetail.addDetail(data.inventory.id,data.inventory.inventoryno);
                } else {
                    Feng.error('盘点失败' + data.message + "!");
                }
            }, function (data) {
                Feng.error("盘点失败!" + data.responseJSON.message + "!");
            });
            var data = [];
            ajax.setData(data);
            ajax.start();
        }
        Feng.confirm('是否确认盘点全部货位',operation);
    };

    /**
     * 跳转到盘点货位明细界面
     */
    InventoryLocatorDetail.addDetail = function (id,inventoryno) {
        func.open({
            title: '盘点明细',
            content: Feng.ctxPath + '/inventory/inventoryDetail_add?id=' + id +'&inventoryno='+inventoryno,
            tableId: InventoryLocatorDetail.tableId
        });
    };


    // 渲染表格
    var tableResult = table.render({
        elem: '#' + InventoryLocatorDetail.tableId,
        url: Feng.ctxPath + '/inventory/locatorList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: InventoryLocatorDetail.initColumn(),
        done:function(res,curr,count){
            for(var i=0;i<res.data.length;i++){
                var mydata=res.data[i];
                if($.inArray(mydata.code, InventoryLocatorDetail.locatorcodes) > -1){
                    var index = res.data[i]['LAY_TABLE_INDEX'];
                    $('tr[data-index=' + index + '] input[type="checkbox"]').prop('checked', true);
                    $('tr[data-index=' + index + '] input[type="checkbox"]').next().addClass('layui-form-checked');
                }
            }
        }
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        InventoryLocatorDetail.search();
    });

    // 重置按钮点击事件
    $('#btnReset').click(function () {
        InventoryLocatorDetail.reset();
    });

    // 盘点按钮点击事件
    $('#btnAdd').click(function () {
        InventoryLocatorDetail.add();
    });

    // 盘点全部货位按钮点击事件
    $('#btnAddAll').click(function () {
        InventoryLocatorDetail.addAll();
    });

});