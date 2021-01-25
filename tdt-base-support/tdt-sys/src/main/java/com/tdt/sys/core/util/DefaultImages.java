/**
 * Copyright 2018-2020 thedreamtree (sn93@qq.com)
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
package com.tdt.sys.core.util;

import com.tdt.sys.core.listener.ConfigListener;
import com.tdt.sys.core.shiro.ShiroKit;
import com.tdt.sys.core.listener.ConfigListener;
import com.tdt.sys.core.shiro.ShiroKit;

/**
 * 获取默认图片地址
 *
 * @author thedreamtree
 * @date 2018-10-30-5:50 PM
 */
public class DefaultImages {

    /**
     * 默认的登录页面背景
     *
     * @author thedreamtree
     * @Date 2018/10/30 5:51 PM
     */
    public static String loginBg() {
        return ConfigListener.getConf().get("contextPath") + "/assets/common/images/login-register.jpg";
    }

    /**
     * 默认的用户图片的base64编码
     *
     * @author thedreamtree
     * @Date 2018/10/30 5:51 PM
     */
    public static String defaultAvatarUrl() {
        if (ShiroKit.oauth2Flag()) {
            return ConfigListener.getConf().get("contextPath") + "/oauth/avatar";
        } else {
            return ConfigListener.getConf().get("contextPath") + "/system/previewAvatar";
        }
    }

    /**
     * 默认的404错误页面背景
     *
     * @author thedreamtree
     * @Date 2018/10/30 5:51 PM
     */
    public static String error404() {
        return ConfigListener.getConf().get("contextPath") + "/assets/common/images/error-bg.jpg";
    }
}
