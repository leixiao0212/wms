layui.use(['ax','func'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var func = layui.func;

    $("#purchaseno").focus();

    var ReceiveInfoDlg = {
    };

    /**
     * 开始接货
     */
    ReceiveInfoDlg.openAdd = function () {
        if($("#purchaseno").val() != ''){
            var ajax = new $ax(Feng.ctxPath + "/receive/addHead", function (data) {
                if(data.code == '200'){
                    Feng.success(data.message);
                    ReceiveInfoDlg.add(data.receive.id);
                    ReceiveInfoDlg.resetSearch();
                }else{
                    Feng.error('接货失败:' + data.message + "!");
                }
            }, function (data) {
                Feng.error('接货失败' + data.responseJSON.message + "!");
            });
            ajax.setData(getFormData());
            ajax.start();
        }else{
            if($("#purchaseno").val()==''){
                $("#purchaseno").focus();
            }
        }
    };

    function getFormData(){
        return $("input,select,textarea").serializeArray();
    };

    /**
     * 清空
     */
    ReceiveInfoDlg.resetSearch = function () {
        $("#purchaseno").val("");
        $("#purchaseno").focus();
    };

    /**
     * 无单接货
     */
    ReceiveInfoDlg.Receive = function () {
            var ajax = new $ax(Feng.ctxPath + "/receive/addHead", function (data) {
                if(data.code == '200'){
                    ReceiveInfoDlg.resetSearch();
                    ReceiveInfoDlg.add(data.receive.id);
                }else{
                    Feng.error('接货失败:' + data.message + "!");
                }
            }, function (data) {
                Feng.error('接货失败' + data.responseJSON.message + "!");
            });
            ajax.setData(getFormData());
            ajax.start();
    };

    //跳转到收货界面
    ReceiveInfoDlg.add = function (id) {
        func.open({
            title: '修改',
            content: Feng.ctxPath + '/receive/add_detail?id=' + id,
        });
    }

    // 开始接货
    $('#btnAdd').click(function () {
        ReceiveInfoDlg.openAdd();
    });

    // 清除
    $('#btnClean').click(function () {
        ReceiveInfoDlg.resetSearch();
    });

    // 无单接货
    $('#btnReceive').click(function () {
        ReceiveInfoDlg.Receive();
    });

});