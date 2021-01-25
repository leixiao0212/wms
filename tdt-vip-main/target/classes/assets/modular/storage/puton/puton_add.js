layui.use(['form', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;

    var PutonInfoDlg = {
        ischeck:false
    };

    $("#commoditybar").focus();

    /**
     * 上架
     */
    PutonInfoDlg.openAdd = function () {
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
            var ajax = new $ax(Feng.ctxPath + "/puton/addHead", function (data) {
                if(data.code == 200){
                    Feng.success(data.message);
                    PutonInfoDlg.resetSearch();
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

    /**
     * 清空
     */
    PutonInfoDlg.resetSearch = function () {
        $("#commoditybar").val("");
        $("#locatorcode").val("");
        $("#oldlocatorcode").val("");
        if(!PutonInfoDlg.ischeck){
            $("#qty").val("1");
        }
        $("#commoditybar").focus();
    };

    // 上架
    $('#btnAdd').click(function () {
        PutonInfoDlg.openAdd();
    });

    // 重置
    $('#btnClean').click(function () {
        PutonInfoDlg.resetSearch();
    });
+
    //锁定
    form.on('checkbox(lock)', function(data){
        if($("#qty").val()==''){
        }else{
            if(data.elem.checked){
                PutonInfoDlg.ischeck=true;
                $("#qty").attr("readonly","readonly");
            }else{
                PutonInfoDlg.ischeck=false;
                $("#qty").removeAttr("readonly");
            }
        }
    });

});