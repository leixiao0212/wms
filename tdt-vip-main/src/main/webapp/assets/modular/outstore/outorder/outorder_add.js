layui.use(['form', 'admin', 'ax', 'layarea'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layarea = layui.layarea;

    var OutorderInfoDlg = {

    };

    $("#receiver").focus();

    //省市区三级联动
    layarea.render({
        elem: '#area-picker',
    });

    //让当前iframe弹层高度适应
    admin.iframeAuto();

    //表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/outorder/addItem", function (data) {
            Feng.success("添加成功！");

            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);

            //关掉对话框
            admin.closeThisDialog();
            OutorderInfoDlg.addDetail(data.data.outorder.id,data.data.outorder.outorderno);
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();

        return false;
    });

    //弹出添加出货订单详情页面
    OutorderInfoDlg.addDetail = function (id,outorderno) {
        //传给上个页面，刷新table用
        admin.putTempData('formOk', true);
        admin.putTempData('id', id);
        admin.putTempData('outorderno', outorderno);
        //关掉对话框
        admin.closeThisDialog();
    };

});