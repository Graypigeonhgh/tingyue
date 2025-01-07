/**
 * Web配置类
 * 配置拦截器等Web相关功能
 * 
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.config;

import com.hgh.tingyue.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;

    /**
     * 添加拦截器配置
     * 配置需要进行身份认证的路径和排除的路径
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                // 拦截所有/api/**路径的请求
                .addPathPatterns("/api/**")
                // 排除登录和注册接口
                .excludePathPatterns("/api/user/login", "/api/user/register");
    }
}