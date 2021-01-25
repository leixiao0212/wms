layui.use(['admin', 'upload', 'table'], function () {
    var $ = layui.jquery;
    var admin = layui.admin;
    var upload = layui.upload;

    //让当前iframe弹层高度适应
    admin.iframeAuto();

    //下载模板按钮点击事件
    $('#download').click(function () {
        window.location.href="http://localhost/commodity/download";
    });

    //执行实例
    var uploadInst = upload.render({
        elem: '#btnExp'
        , url: '/commodity/uploadExcel'
        ,accept: 'file'
        , done: function (res) {
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }
        , error: function () {
            //请求异常回调
        }
    });
});