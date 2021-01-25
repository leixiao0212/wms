layui.use(['ax','func'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var func = layui.func;

    var ReviewInfoDlg = {

    };

    $("#outorderno").focus();

    /**
     * 点击开始出库复核按钮
     */
    ReviewInfoDlg.openAdd = function () {
        if ($("#outorderno").val()==""){
            Feng.info("出库订单编号不能为空不能为空");
            $("#outorderno").focus();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/review/addHead", function (data) {
                if(data.code == '200'){
                    Feng.success(data.message);
                    ReviewInfoDlg.resetSearch();
                    ReviewInfoDlg.add(data.review.id);
                }else{
                    Feng.error('开始出库复核失败:' + data.message + "!");
                }
            }, function (data) {
                Feng.error('开始出库复核失败' + data.responseJSON.message + "!");
            });
            ajax.setData(getFormData());
            ajax.start();
        }
    };

    function getFormData(){
        return $("input,select,textarea").serializeArray();
    };

    //跳转到添加拣货明细界面
    ReviewInfoDlg.add = function (id) {
        func.open({
            title: '修改',
            content: Feng.ctxPath + '/review/add_detail?id=' + id,
        });
    }

    /**
     * 清空
     */
    ReviewInfoDlg.resetSearch = function () {
        $("#outorderno").val("");
    };

    // 拣货
    $('#btnAdd').click(function () {
        ReviewInfoDlg.openAdd();
    });

    // 重置
    $('#btnClean').click(function () {
        ReviewInfoDlg.resetSearch();
    });

});