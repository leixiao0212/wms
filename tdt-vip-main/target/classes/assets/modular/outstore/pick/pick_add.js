layui.use(['ax','func'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var func = layui.func;

    var PickInfoDlg = {

    };

    $("#picktaskno").focus();

    /**
     * 拣货
     */
    PickInfoDlg.openAdd = function () {
        if ($("#picktaskno").val()==""){
            Feng.info("拣货任务单号不能为空");
            $("#picktaskno").focus();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/pick/addHead", function (data) {
                if(data.code == '200'){
                    Feng.success(data.message);
                    PickInfoDlg.resetSearch();
                    PickInfoDlg.add(data.pick.id,data.pick.pickno);
                }else{
                    Feng.error('上架失败:' + data.message + "!");
                }
            }, function (data) {
                Feng.error('上架失败' + data.responseJSON.message + "!");
            });
            ajax.setData(getFormData());
            ajax.start();
        }
    };

    function getFormData(){
        return $("input,select,textarea").serializeArray();
    };

    //跳转到添加拣货明细界面
    PickInfoDlg.add = function (id,pickno) {
        func.open({
            title: '修改',
            content: Feng.ctxPath + '/pick/add_detail?id=' + id + "&pickno="+pickno,
        });
    }

    /**
     * 清空
     */
    PickInfoDlg.resetSearch = function () {
        $("#picktaskno").val("");
        $("#remarks").val("");
    };

    // 拣货
    $('#btnAdd').click(function () {
        PickInfoDlg.openAdd();
    });

    // 重置
    $('#btnClean').click(function () {
        PickInfoDlg.resetSearch();
    });

});