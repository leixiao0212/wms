layui.use(['ax', 'form'], function () {
    var $ = layui.$;
    var $ax = layui.ax;
    var form = layui.form;

    /**
     * 管理
     */
    var Wellen = {};

    form.on('submit(*)', function(data){
        var qty = $("#qty").val();
        //将页面全部复选框选中的值拼接到一个数组中
        var arr_box = [];
        $('input[type=checkbox]:checked').each(function() {
            arr_box.push($(this).val());
        });
        if (arr_box.length == 0){
            Feng.info("请至少选择一种波次种类");
        } else if (qty==""){
            Feng.info("请输入生成波次的数量");
        } else {
            var ajax = new $ax(Feng.ctxPath + "/wellen/add", function (data) {
                if (data.code == "200"){
                    Feng.success(data.message);
                } else {
                    Feng.error(data.message);
                }
            }, function (data) {
                Feng.error("添加失败！" + data.responseJSON.message)
            });
            ajax.set("checkValue",arr_box.toString());
            ajax.set("qty",qty);
            ajax.start();
        }
        return false;
    });

    /**
     * 点击生成出库订单标签。按钮
     */
    Wellen.increase = function () {
        var ajax = new $ax(Feng.ctxPath + "/wellen/increase", function (data) {
            if (data.code == "200"){
                Feng.success(data.message);
            } else {
                Feng.error(data.message);
            }
            Wellen.clear();
        }, function (data) {
            Feng.error("生成出库订单标签失败！" + data.responseJSON.message)
        });
        ajax.start();
        return false;
    };
    Wellen.clear = function () {
        $("input:checkbox").prop("checked", false);
        form.render('checkbox');
    }
    // 生成出库订单标签按钮点击事件
    $('#btnIncrease').click(function () {
        Wellen.increase();
    });
    // 生成出库订单标签按钮点击事件
    $('#btnClean').click(function () {
        Wellen.clear();
    });
});
