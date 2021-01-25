package com.tdt.sys.modular.third.service.impl;

import com.tdt.base.shiro.ShiroUser;
import com.tdt.sys.core.exception.oauth.OAuthExceptionEnum;
import com.tdt.sys.core.exception.oauth.OAuthLoginException;
import com.tdt.sys.core.shiro.ShiroKit;
import com.tdt.sys.core.shiro.oauth.OAuthToken;
import com.tdt.sys.modular.system.entity.User;
import com.tdt.sys.modular.system.service.UserService;
import com.tdt.sys.modular.third.entity.OauthUserInfo;
import com.tdt.sys.modular.third.factory.OAuthUserInfoFactory;
import com.tdt.sys.modular.third.service.LoginService;
import com.tdt.sys.modular.third.service.OauthUserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tdt.sys.modular.third.factory.OAuthUserInfoFactory;
import com.tdt.sys.modular.third.service.LoginService;
import com.tdt.sys.modular.third.service.OauthUserInfoService;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 默认第三方登录逻辑
 *
 * @author thedreamtree
 * @Date 2019/6/9 18:16
 */
@Service
public class DefaultLoginService implements LoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private OauthUserInfoService oauthUserInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String oauthLogin(AuthUser oauthUser) {

        if (oauthUser == null) {
            throw new OAuthLoginException(OAuthExceptionEnum.OAUTH_RESPONSE_ERROR);
        }

        //当前有登录用户
        if (ShiroKit.isUser()) {

            //当前登录用户
            ShiroUser user = ShiroKit.getUserNotNull();

            //绑定用户相关的openId
            bindOAuthUser(user.getId(), oauthUser);

            return "redirect:/system/user_info";

        } else {

            //当前无登录用户，创建用户或根据已有绑定用户的账号登录
            String account = getOauthUserAccount(oauthUser);

            //执行shiro的登录逻辑
            OAuthToken token = new OAuthToken(account);
            ShiroKit.getSubject().login(token);

            return "redirect:/";
        }

    }


    /**
     * 绑定当前用户的source和openId
     *
     * @author thedreamtree
     * @Date 2019/6/9 18:51
     */
    private void bindOAuthUser(Long userId, AuthUser oauthUser) {

        //先判断当前系统这个openId有没有人用
        QueryWrapper<OauthUserInfo> queryWrapper = new QueryWrapper<OauthUserInfo>()
                .eq("source", oauthUser.getSource())
                .and(i -> i.eq("uuid", oauthUser.getUuid()))
                .and(i -> i.ne("user_id", userId));
        List<OauthUserInfo> oauthUserInfos = this.oauthUserInfoService.list(queryWrapper);

        //已有人绑定，抛出异常
        if (oauthUserInfos != null && oauthUserInfos.size() > 0) {
            throw new OAuthLoginException(OAuthExceptionEnum.OPEN_ID_ALREADY_BIND);
        } else {
            //新建一条绑定记录
            OauthUserInfo oAuthUserInfo = OAuthUserInfoFactory.createOAuthUserInfo(userId, oauthUser);
            this.oauthUserInfoService.save(oAuthUserInfo);
        }

    }

    /**
     * 通过第三方登录的信息创建本系统用户
     *
     * @author thedreamtree
     * @Date 2019/6/9 19:07
     */
    private String getOauthUserAccount(AuthUser oauthUser) {

        //先判断当前系统这个openId有没有人用
        QueryWrapper<OauthUserInfo> queryWrapper = new QueryWrapper<OauthUserInfo>()
                .eq("source", oauthUser.getSource())
                .and(i -> i.eq("uuid", oauthUser.getUuid()));
        OauthUserInfo oauthUserInfos = this.oauthUserInfoService.getOne(queryWrapper);

        //已有人绑定,直接返回这个人的账号，进行登录
        if (oauthUserInfos != null) {
            Long userId = oauthUserInfos.getUserId();
            return this.userService.getById(userId).getAccount();
        } else {

            //没有人绑定的创建这个人的本系统用户redirect_uri
            User user = OAuthUserInfoFactory.createOAuthUser(oauthUser);
            this.userService.save(user);

            //新建一条oauth2绑定记录
            OauthUserInfo oAuthUserInfo = OAuthUserInfoFactory.createOAuthUserInfo(user.getUserId(), oauthUser);
            this.oauthUserInfoService.save(oAuthUserInfo);

            return user.getAccount();
        }
    }

}
