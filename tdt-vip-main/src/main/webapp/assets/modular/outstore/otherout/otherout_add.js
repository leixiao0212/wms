layui.use(['ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;

    var OtheroutInfoDlg = {

    };

    $("#locatorcode").focus();

    /**
     * 入库
     */
    OtheroutInfoDlg.openAdd = function () {
        if ($("#locatorcode").val()==""){
            Feng.info("入库库位不能为空");
            $("#locatorcode").focus();
        } else if ($("#commoditybar").val()==""){
            Feng.info("商品编码不能为空");
            $("#commoditybar").focus();
        } else if ($("#qty").val()==""){
            Feng.info("数量不能为空");
            $("#qty").focus();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/otherout/addHead", function (data) {
                if(data.code == '200'){
                    Feng.success(data.message)
                    OtheroutInfoDlg.resetSearch();
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
    OtheroutInfoDlg.resetSearch = function () {
        $("#locatorcode").val("");
        $("#commoditybar").val("");
        $("#qty").val("");
        $("#locatorcode").focus();
    };

    // 入库
    $('#btnAdd').click(function () {
        OtheroutInfoDlg.openAdd();
    });

    // 清除
    $('#btnClean').click(function () {
        OtheroutInfoDlg.resetSearch();
    });

});