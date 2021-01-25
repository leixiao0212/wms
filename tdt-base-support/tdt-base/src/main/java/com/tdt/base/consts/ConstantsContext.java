package com.tdt.base.consts;

import com.tdt.base.enums.CommonStatus;
import com.tdt.base.sms.AliyunSmsProperties;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.tdt.base.enums.CommonStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tdt.base.consts.ConfigConstant.SYSTEM_CONSTANT_PREFIX;

/**
 * 系统常量的容器
 *
 * @author thedreamtree
 * @Date 2019-06-20 13:37
 */
@Slf4j
public class ConstantsContext {

    /**
     * 所有的常量，可以增删改查
     */
    private static Map<String, Object> CONSTNTS_HOLDER = new ConcurrentHashMap<>();

    /**
     * 添加系统常量
     */
    public static void putConstant(String key, Object value) {
        if (ToolUtil.isOneEmpty(key, value)) {
            return;
        }
        CONSTNTS_HOLDER.put(key, value);
    }

    /**
     * 删除常量
     */
    public static void deleteConstant(String key) {
        if (ToolUtil.isOneEmpty(key)) {
            return;
        }

        //如果是系统常量
        if (!key.startsWith(SYSTEM_CONSTANT_PREFIX)) {
            CONSTNTS_HOLDER.remove(key);
        }
    }

    /**
     * 获取系统常量
     */
    public static Map<String, Object> getConstntsMap() {
        return CONSTNTS_HOLDER;
    }

    /**
     * 获取验证码开关
     */
    public static Boolean getKaptchaOpen() {
        String tdtKaptchaOpen = (String) CONSTNTS_HOLDER.get("TDT_KAPTCHA_OPEN");
        if (CommonStatus.ENABLE.getCode().equalsIgnoreCase(tdtKaptchaOpen)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取短信的配置
     */
    public static AliyunSmsProperties getAliyunSmsProperties() {
        String tdtSmsAccesskeyId = (String) CONSTNTS_HOLDER.get("TDT_SMS_ACCESSKEY_ID");
        String tdtSmsAccesskeySecret = (String) CONSTNTS_HOLDER.get("TDT_SMS_ACCESSKEY_SECRET");
        String tdtSmsSignName = (String) CONSTNTS_HOLDER.get("TDT_SMS_SIGN_NAME");
        String tdtSmsLoginTemplateCode = (String) CONSTNTS_HOLDER.get("TDT_SMS_LOGIN_TEMPLATE_CODE");
        String tdtSmsInvalidateMinutes = (String) CONSTNTS_HOLDER.get("TDT_SMS_INVALIDATE_MINUTES");

        AliyunSmsProperties aliyunSmsProperties = new AliyunSmsProperties();
        aliyunSmsProperties.setAccessKeyId(tdtSmsAccesskeyId);
        aliyunSmsProperties.setAccessKeySecret(tdtSmsAccesskeySecret);
        aliyunSmsProperties.setSignName(tdtSmsSignName);
        aliyunSmsProperties.setLoginTemplateCode(tdtSmsLoginTemplateCode);
        aliyunSmsProperties.setInvalidateMinutes(Integer.valueOf(tdtSmsInvalidateMinutes));
        return aliyunSmsProperties;
    }

    /**
     * 获取管理系统名称
     */
    public static String getSystemName() {
        String systemName = (String) CONSTNTS_HOLDER.get("TDT_SYSTEM_NAME");
        if (ToolUtil.isEmpty(systemName)) {
            log.error("系统常量存在空值！常量名称：TDT_SYSTEM_NAME，采用默认名称：Guns快速开发平台");
            return "Guns快速开发平台";
        } else {
            return systemName;
        }
    }

    /**
     * 获取管理系统名称
     */
    public static String getDefaultPassword() {
        String defaultPassword = (String) CONSTNTS_HOLDER.get("TDT_DEFAULT_PASSWORD");
        if (ToolUtil.isEmpty(defaultPassword)) {
            log.error("系统常量存在空值！常量名称：TDT_DEFAULT_PASSWORD，采用默认密码：111111");
            return "111111";
        } else {
            return defaultPassword;
        }
    }

    /**
     * 获取管理系统名称
     */
    public static String getOAuth2UserPrefix() {
        String oauth2Prefix = (String) CONSTNTS_HOLDER.get("TDT_OAUTH2_PREFIX");
        if (ToolUtil.isEmpty(oauth2Prefix)) {
            log.error("系统常量存在空值！常量名称：TDT_OAUTH2_PREFIX，采用默认值：oauth2");
            return "oauth2";
        } else {
            return oauth2Prefix;
        }
    }

    /**
     * 获取顶部导航条是否开启
     */
    public static Boolean getDefaultAdvert() {
        String tdtDefaultAdvert = (String) CONSTNTS_HOLDER.get("TDT_DEFAULT_ADVERT");
        if (ToolUtil.isEmpty(tdtDefaultAdvert)) {
            log.error("系统常量存在空值！常量名称：TDT_DEFAULT_ADVERT，采用默认值：true");
            return true;
        } else {
            if (CommonStatus.ENABLE.getCode().equalsIgnoreCase(tdtDefaultAdvert)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 获取系统发布的版本号（防止css和js的缓存）
     */
    public static String getReleaseVersion() {
        String systemReleaseVersion = (String) CONSTNTS_HOLDER.get("TDT_SYSTEM_RELEASE_VERSION");
        if (ToolUtil.isEmpty(systemReleaseVersion)) {
            log.error("系统常量存在空值！常量名称：systemReleaseVersion，采用默认值：tdt");
            return ToolUtil.getRandomString(8);
        } else {
            return systemReleaseVersion;
        }
    }
}
