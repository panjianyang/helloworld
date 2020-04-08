package com.itheima.config;/*
 * @description: 获取配置文件config.properties文件的属性
 * @date: $date$ $time$
 * @auther: pjy
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("config.properties")
@ConfigurationProperties(prefix = "springboot")
public class PropConfig {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
