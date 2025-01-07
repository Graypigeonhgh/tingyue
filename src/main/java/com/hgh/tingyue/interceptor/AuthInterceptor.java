/**
 * 认证拦截器
 * 
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.interceptor;

import com.hgh.tingyue.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 请求预处理
     * 验证请求中的JWT令牌，并将用户信息存入请求属性
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器
     * @return 是否通过验证
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }

        token = token.substring(7);
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(401);
            return false;
        }

        // 将用户信息存入request
        request.setAttribute("userId", jwtUtil.extractUserId(token));
        request.setAttribute("username", jwtUtil.extractUsername(token));
        return true;
    }
}