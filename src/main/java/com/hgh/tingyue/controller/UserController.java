/**
 * 用户控制器
 * 
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.controller;

import com.hgh.tingyue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import com.hgh.tingyue.dto.request.RegisterRequest;
import com.hgh.tingyue.dto.request.LoginRequest;
import com.hgh.tingyue.dto.response.LoginResponse;
import com.hgh.tingyue.entity.User;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 用户注册接口
     *
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     * @return 注册结果
     */
    @Operation(summary = "用户注册", description = "注册新用户")
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(userService.register(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()));
    }

    /**
     * 用户登录接口
     *
     * @param username 用户名
     * @param password 密码
     * @return 包含JWT令牌的响应
     */
    @Operation(summary = "用户登录", description = "用户登录并返回JWT令牌")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }
}