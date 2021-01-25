package com.tdt.config.datasource;

import com.tdt.sys.core.shiro.ShiroKit;
import cn.stylefeng.roses.core.metadata.CustomMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mp的插件拓展和资源扫描
 *
 * @author thedreamtree
 * @Date 2019/5/10 21:33
 */
@Configuration
@MapperScan(basePackages = {
        "com.tdt.sys.modular.*.mapper",
        "com.tdt.modular.*.mapper",
        "com.tdt.sms.modular.mapper"})
public class PluginsConfig {

    /**
     * 拓展核心包中的字段包装器
     *
     * @author thedreamtree
     * @Date 2019/5/10 21:35
     */
    @Bean
    public CustomMetaObjectHandler tdtMpFieldHandler() {
        return new CustomMetaObjectHandler() {

            @Override
            protected Long getUserUniqueId() {
                try {
                    return ShiroKit.getUserNotNull().getId();
                } catch (Exception e) {

                    //如果获取不到当前用户就存空id
                    return -100L;
                }
            }
        };
    }

}
