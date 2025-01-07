/**
 * 用户服务实现类
 * 
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hgh.tingyue.entity.User;
import com.hgh.tingyue.mapper.UserMapper;
import com.hgh.tingyue.service.UserService;
import com.hgh.tingyue.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     * @return 注册成功的用户信息
     * @throws RuntimeException 当用户名或邮箱已存在时抛出
     */
    @Override
    public User register(String username, String password, String email) {
        // 检查用户名是否已存在
        if (userMapper.selectOne(new QueryWrapper<User>().eq("username", username)) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userMapper.selectOne(new QueryWrapper<User>().eq("email", email)) != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setEmail(email);

        userMapper.insert(user);
        return user;
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return JWT令牌
     * @throws RuntimeException 当用户名或密码错误时抛出
     */
    @Override
    public String login(String username, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("用户名或密码错误");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getId());
    }

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }
}