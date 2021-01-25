/**
 * Copyright 2018-2020 thedreamtree (https://gitee.com/thedreamtree)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tdt.sys.modular.system.controller;

import com.tdt.base.shiro.ShiroUser;
import com.tdt.sys.core.exception.InvalidKaptchaException;
import com.tdt.sys.core.log.LogManager;
import com.tdt.sys.core.log.factory.LogTaskFactory;
import com.tdt.sys.core.shiro.ShiroKit;
import com.tdt.sys.core.util.KaptchaUtil;
import com.tdt.sys.modular.system.service.UserService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.google.code.kaptcha.Constants;
import com.tdt.sys.core.exception.InvalidKaptchaException;
import com.tdt.sys.core.log.LogManager;
import com.tdt.sys.core.log.factory.LogTaskFactory;
import com.tdt.sys.core.shiro.ShiroKit;
import com.tdt.sys.core.util.KaptchaUtil;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

import static cn.stylefeng.roses.core.util.HttpContext.getIp;

/**
 * 登录控制器
 *
 * @author thedreamtree
 * @Date 2017年1月10日 下午8:25:24
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到主页
     *
     * @author thedreamtree
     * @Date 2018/12/23 5:41 PM
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {

        //获取当前用户角色列表
        ShiroUser user = ShiroKit.getUserNotNull();
        List<Long> roleList = user.getRoleList();

        if (roleList == null || roleList.size() == 0) {
            ShiroKit.getSubject().logout();
            model.addAttribute("tips", "该用户没有角色，无法登陆");
            return "/login.html";
        }

        List<Long> warehouseList=user.getWarehouses();
        if(warehouseList==null || warehouseList.size() == 0 ){
            ShiroKit.getSubject().logout();
            model.addAttribute("tips", "该用户没有仓库信息权限，无法登陆,请联系管理员");
            return "/login.html";
        }else if(warehouseList.size()==1){
            ShiroKit.getUser().setWarehouseId(warehouseList.get(0));
            List<Map<String, Object>> menus = userService.getUserMenuNodes(roleList);
            model.addAttribute("menus", menus);
            return "/index.html";
        }else{
            List<Map<String, Object>> warehouses = userService.getWarehouses(warehouseList);
            model.addAttribute("warehouses", warehouses);
            return "/choose.html";
        }
    }
    /**
     * 跳转到登录页面
     *
     * @author thedreamtree
     * @Date 2018/12/23 5:41 PM
     */
    @RequestMapping(value = "/choose")
    public String choose(Model model) {
        String warehouseid = super.getPara("warehouseid").trim();
        ShiroKit.getUser().setWarehouseId(Long.valueOf(warehouseid));
        List<Map<String, Object>> menus = userService.getUserMenuNodes(ShiroKit.getUser().getRoleList());
        model.addAttribute("menus", menus);
        return "/index.html";
    }
    /**
     * 跳转到登录页面
     *
     * @author thedreamtree
     * @Date 2018/12/23 5:41 PM
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (ShiroKit.isAuthenticated() || ShiroKit.getUser() != null) {
            return REDIRECT + "/";
        } else {
            return "/login.html";
        }
    }

    /**
     * 点击登录执行的动作
     *
     * @author thedreamtree
     * @Date 2018/12/23 5:42 PM
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginVali() {

        String username = super.getPara("username").trim();
        String password = super.getPara("password").trim();
        String remember = super.getPara("remember");

        //验证验证码是否正确
        if (KaptchaUtil.getKaptchaOnOff()) {
            String kaptcha = super.getPara("kaptcha").trim();
            String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (ToolUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
                throw new InvalidKaptchaException();
            }
        }

        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        //如果开启了记住我功能
        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }

        //执行shiro登录操作
        currentUser.login(token);

        //登录成功，记录登录日志
        ShiroUser shiroUser = ShiroKit.getUserNotNull();
        LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), getIp()));

        ShiroKit.getSession().setAttribute("sessionFlag", true);

        return REDIRECT + "/";
    }

    /**
     * 退出登录
     *
     * @author thedreamtree
     * @Date 2018/12/23 5:42 PM
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut() {
        LogManager.me().executeLog(LogTaskFactory.exitLog(ShiroKit.getUserNotNull().getId(), getIp()));
        ShiroKit.getSubject().logout();
        deleteAllCookie();
        return REDIRECT + "/login";
    }
}
