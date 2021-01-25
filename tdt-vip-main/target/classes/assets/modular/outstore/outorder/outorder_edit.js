layui.use(['form', 'admin', 'ax', 'layarea'], function () {
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layarea = layui.layarea;

    //让当前iframe弹层高度适应
    admin.iframeAuto();

    //获取详情信息，填充表单
    var ajax = new $ax(Feng.ctxPath + "/outorder/detail?id=" + Feng.getUrlParam("id"));
    var result = ajax.start();
    form.val('outorderForm', result.data);
    //省市区三级联动
    layarea.render({
        elem: '#area-picker',
        data: {
            province: result.data.province,
            city: result.data.city,
            county: result.data.county
        }
    });
    //表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/outorder/editItem", function (data) {
            Feng.success("更新成功！");

            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);

            //关掉对话框
            admin.closeThisDialog();

        }, function (data) {
            Feng.error("更新失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();

        return false;
    });

});