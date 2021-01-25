layui.use(['form', 'ax', 'func'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var func = layui.func;

    var AllocationInfoDlg = {

    };

    /**
     * 调拨
     */
    AllocationInfoDlg.openAdd = function () {
        if ($("#allocationno").val()==""){
            Feng.info("调拨单号不能为空");
            $("#allocationno").focus();
        } else if ($("#bwarehouseid").val()==""){
            Feng.info("原仓库不能为空");
            $("#bwarehouseid").focus();
        } else if ($("#awarehouseid").val()==""){
            Feng.info("目标仓库不能为空");
            $("#awarehouseid").focus();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/allocation/addHead", function (data) {
                if(data.code == '200'){
                    Feng.success(data.message);
                    AllocationInfoDlg.addDetail(data.allocation.id,data.allocation.allocationno);
                    AllocationInfoDlg.resetSearch();
                }else{
                    Feng.error('调拨失败:' + data.message + "!");
                }
            }, function (data) {
                Feng.error('调拨失败' + data.responseJSON.message + "!");
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
    AllocationInfoDlg.resetSearch = function () {
        $("#remark").val("");
        $("#bwarehouseid").val("");
        $("#awarehouseid").val("");
        form.render('select', 'test');
    };

    /**
     * 进入调拨明细界面
     */
    AllocationInfoDlg.addDetail = function (id,allocationno) {
        func.open({
            title: '调拨明细',
            content: Feng.ctxPath + '/allocation/allocationDetail_add?id='+id+'&allocationno='+allocationno,
        });
    };

    // 调拨
    $('#btnAdd').click(function () {
        AllocationInfoDlg.openAdd();
    });

    // 清空
    $('#btnClean').click(function () {
        AllocationInfoDlg.resetSearch();
    });

});