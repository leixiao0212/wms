layui.use(['table', 'admin', 'ax', 'ztree', 'func'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var $ZTree = layui.ztree;
    var func = layui.func;

    /**
     * 系统管理--部门管理
     */
    var Dept = {
        tableId: "deptTable",
        condition: {
            deptId: ""
        }
    };

    /**
     * 初始化表格的列
     */
    Dept.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'deptId', hide: true, sort: true, title: 'id'},
            {field: 'simpleName', align: "center", sort: true, title: '部门简称'},
            {field: 'fullName', align: "center", sort: true, title: '部门全称'},
            {field: 'sort', align: "center", sort: true, title: '排序'},
            {field: 'description', align: "center", sort: true, title: '备注'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    /**
     * 点击查询按钮
     */
    Dept.search = function () {
        var queryData = {};
        queryData['condition'] = $("#name").val();
        queryData['deptId'] = Dept.condition.deptId;
        table.reload(Dept.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 选择部门时
     */
    Dept.onClickDept = function (e, treeId, treeNode) {
        Dept.condition.deptId = treeNode.id;
        Dept.search();
    };

    /**
     * 弹出添加
     */
    Dept.openAddDept = function () {
        func.open({
            height: 530,
            title: '添加部门',
            content: Feng.ctxPath + '/dept/dept_add',
            tableId: Dept.tableId
        });
    };

    /**
     * 点击编辑部门
     *
     * @param data 点击按钮时候的行数据
     */
    Dept.onEditDept = function (data) {
        func.open({
            height: 530,
            title: '编辑部门',
            content: Feng.ctxPath + "/dept/dept_update?deptId=" + data.deptId,
            tableId: Dept.tableId
        });
    };

    /**
     * 导出excel按钮
     */
    Dept.exportExcel = function () {
        var checkRows = table.checkStatus(Dept.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击删除部门
     *
     * @param data 点击按钮时候的行数据
     */
    Dept.onDeleteDept = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/dept/delete", function () {
                Feng.success("删除成功!");
                table.reload(Dept.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("deptId", data.deptId);
            ajax.start();
        };
        Feng.confirm("是否删除部门 " + data.simpleName + "?", operation);
    };
    /**
     * 分配菜单
     *
     * @param data 点击按钮时候的行数据
     */
    Dept.deptAssign = function (data) {
        parent.layer.open({
            type: 2,
            title: '权限配置',
            area: ['300px', '450px'], //宽高
            fix: false,
            maxmin: true,
            content: Feng.ctxPath + '/dept/dept_assign/' + data.deptId,
            end: function () {
                table.reload(Dept.tableId);
            }
        });
    };
    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Dept.tableId,
        url: Feng.ctxPath + '/dept/list',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: Dept.initColumn()
    });

    //初始化左侧部门树
    var ztree = new $ZTree("deptTree", "/dept/tree");
    ztree.bindOnClick(Dept.onClickDept);
    ztree.init();

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Dept.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        Dept.openAddDept();
    });

    // 导出excel
    $('#btnExp').click(function () {
        Dept.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + Dept.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Dept.onEditDept(data);
        } else if (layEvent === 'delete') {
            Dept.onDeleteDept(data);
        }else if (layEvent === 'deptAssign') {
            Dept.deptAssign(data);
        }
    });
});
