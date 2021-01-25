layui.use(['form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;

    //让当前iframe弹层高度适应
    admin.iframeAuto();

    //获取详情信息，填充表单
    var ajax = new $ax(Feng.ctxPath + "/locator/detail?id=" + Feng.getUrlParam("id"));
    var result = ajax.start();
    form.val('locatorForm', result.data);

    //表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/locator/editItem", function (data) {
            if (data.code == 500){
                Feng.error('修改失败:' + data.message + "!");
            } else {
                Feng.success("更新成功！");
                //传给上个页面，刷新table用
                admin.putTempData('formOk', true);
                //关掉对话框
                admin.closeThisDialog();
            }
        }, function (data) {
            Feng.error("更新失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();

        return false;
    });

    //将货位类型和所属仓库下拉列表锁定
    $('select').attr("disabled","disabled");
    form.render('select', 'locatorForm');
    $('.layui-input.layui-unselect').attr("style","background-color:#DBDBDB");
});