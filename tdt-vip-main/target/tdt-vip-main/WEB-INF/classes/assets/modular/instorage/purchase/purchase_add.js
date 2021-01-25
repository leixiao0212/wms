layui.use(['form', 'admin', 'ax', 'laydate'], function () {
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var laydate = layui.laydate;

    //让当前iframe弹层高度适应
    admin.iframeAuto();

    laydate.render({
        elem: '#estimatearrivaltime',
        type: 'datetime',
        trigger: 'click'
    });

    var PurchaseInfoDlg={};

    //表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/purchase/addItem", function (data) {
            Feng.success("添加成功！");

            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);

            //关掉对话框
            admin.closeThisDialog();
            PurchaseInfoDlg.addDetail(data.data.purchase.id,data.data.purchase.purchaseno);
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
        return false;
    });

    //弹出添加采购订单详情页面
    PurchaseInfoDlg.addDetail = function (id,purchaseno) {
        //传给上个页面，刷新table用
        admin.putTempData('formOk', true);
        admin.putTempData('id', id);
        admin.putTempData('purchaseno', purchaseno);

        //关掉对话框
        admin.closeThisDialog();
    };
});