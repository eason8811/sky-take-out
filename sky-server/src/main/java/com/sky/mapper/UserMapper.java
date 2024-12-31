package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.User;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    /**
     * 根据给定的 openId 在数据库中查询用户是否存在
     *
     * @param openid 给定的 openId
     * @return 返回用户的实体类对象
     */
    User listByOpenId(String openid);

    /**
     * 插入新用户
     *
     * @param user 用户的实体类对象
     */
    void insert(User user);
}
