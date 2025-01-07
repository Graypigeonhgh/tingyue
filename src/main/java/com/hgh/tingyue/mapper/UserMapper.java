package com.hgh.tingyue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgh.tingyue.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}