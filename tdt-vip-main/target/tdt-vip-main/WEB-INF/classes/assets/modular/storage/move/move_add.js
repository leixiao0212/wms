layui.use(['form', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;

    var MoveInfoDlg = {
        ischeck:false
    };

    $("#commoditybar").focus();

    /**
     * 移库
     */
    MoveInfoDlg.openAdd = function () {
        if ($("#commoditybar").val()==""){
            Feng.info("商品编码不能为空");
            $("#commoditybar").focus();
        } else if ($("#locatorcode").val()==""){
            Feng.info("货位编码不能为空");
            $("#locatorcode").focus();
        } else if ($("#oldlocatorcode").val()==""){
            Feng.info("原货位编码不能为空");
            $("#oldlocatorcode").focus();
        } else if($("#qty").val()==""){
            Feng.info("数量不能为空");
            $("#qty").focus();
        } else {
            var ajax = new $ax(Feng.ctxPath + "/move/addHead", function (data) {
                if(data.code == '200'){
                    Feng.success(data.message);
                    MoveInfoDlg.resetSearch();
                }else{
                    Feng.error('移库失败:' + data.message + "!");
                }
            }, function (data) {
                Feng.error('移库失败' + data.responseJSON.message + "!");
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
    MoveInfoDlg.resetSearch = function () {
        $("#commoditybar").val("");
        $("#locatorcode").val("");
        $("#oldlocatorcode").val("");
        if(!MoveInfoDlg.ischeck){
            $("#qty").val("1");
        }
        $("#commoditybar").focus();
    };

    // 移库
    $('#btnAdd').click(function () {
        MoveInfoDlg.openAdd();
    });

    // 重置
    $('#btnClean').click(function () {
        MoveInfoDlg.resetSearch();
    });

    //锁定
    form.on('checkbox(lock)', function(data){
        if($("#qty").val()==''){
        }else{
            if(data.elem.checked){
                MoveInfoDlg.ischeck=true;
                $("#qty").attr("readonly","readonly");
            }else{
                MoveInfoDlg.ischeck=false;
                $("#qty").removeAttr("readonly");
            }
        }
    });

});