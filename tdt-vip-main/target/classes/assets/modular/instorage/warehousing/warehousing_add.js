layui.use(['ax','func'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var func = layui.func;

    var WarehousingInfoDlg = {
    };

    $("#receiveno").focus();

    /**
     * 入库
     */
    WarehousingInfoDlg.openAdd = function () {
        if ($("#receiveno").val() == ""){
            Feng.info("单号不能为空");
            $("#receiveno").focus();
        } else if ($("#locatorcode").val() == ""){
            Feng.info("货位不能为空");
            $("#locatorcode").focus();
        } else{
            var ajax = new $ax(Feng.ctxPath + "/warehousing/addHead", function (data) {
                if(data.code == '200'){
                    Feng.success(data.message);
                    WarehousingInfoDlg.resetSearch();
                    WarehousingInfoDlg.add(data.warehousing.id,data.warehousing.warehousingno);
                }else{
                    Feng.error('入库失败:' + data.message + "!");
                }
            }, function (data) {
                Feng.error('入库失败' + data.responseJSON.message + "!");
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
    WarehousingInfoDlg.resetSearch = function () {
        $("#receiveno").val("");
        $("#locatorcode").val("");
        $("#remark").val("");
        $("#receiveno").focus();
    };

    /**
     * 直接入库
     */
    WarehousingInfoDlg.Ensure = function () {
        if ($("#receiveno").val() == ""){
            Feng.info("单号不能为空");
            $("#receiveno").focus();
        } else if ($("#locatorcode").val() == ""){
            Feng.info("货位不能为空");
            $("#locatorcode").focus();
        } else{
            var ajax = new $ax(Feng.ctxPath + "/warehousing/addEnd", function (data) {
                if(data.code == '200'){
                    Feng.success(data.message);
                    WarehousingInfoDlg.resetSearch();
                }else{
                    Feng.error('接货失败:' + data.message + "!");
                }
            }, function (data) {
                Feng.error('接货失败' + data.responseJSON.message + "!");
            });
            ajax.setData(getFormData());
            ajax.start();
        }
    };

    //跳转到收货界面
    WarehousingInfoDlg.add = function (id,warehousingno) {
        func.open({
            title: '修改',
            content: Feng.ctxPath + '/warehousing/warehousing_detail_add?id=' + id+'&warehousingno='+warehousingno,
        });
    }

    // 入库
    $('#btnAdd').click(function () {
        WarehousingInfoDlg.openAdd();
    });

    // 清除
    $('#btnClean').click(function () {
        WarehousingInfoDlg.resetSearch();
    });

    // 直接入库
    $('#btnEnsure').click(function () {
        WarehousingInfoDlg.Ensure();
    });

});