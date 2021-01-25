layui.use(['form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;

   $("#bar").focus();

    //表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/commodity/addItem", function (data) {
            Feng.success("添加成功！");

            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);

            //关掉对话框
            admin.closeThisDialog();

        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();

        return false;
    });

    //自动生成按钮点击事件
    $('#generateBar').click(function () {
        var bar = '';
        var now = new Date();
        bar = bar + now.getFullYear().toString().substr(now.getFullYear().toString().length-2);
        if(now.getMonth() < 10){
            bar = bar + '0'+ (now.getMonth()+1);
        }else{
            bar = bar + (now.getMonth()+1);
        }
        if(now.getDate() < 10){
            bar = bar + '0'+ now.getDate();
        }else{
            bar = bar + now.getDate();
        }
        if(now.getHours() < 10){
            bar = bar + '0'+ now.getHours();
        }else{
            bar = bar + now.getHours();
        }
        if(now.getMinutes() < 10){
            bar = bar + '0'+ now.getMinutes();
        }else{
            bar = bar + now.getMinutes();
        }
        if(now.getSeconds() < 10){
            bar = bar + '0'+ now.getSeconds();
        }else{
            bar = bar + now.getSeconds();
        }
        bar = bar + now.getMilliseconds();
        $("#bar").val(bar);
    });

});