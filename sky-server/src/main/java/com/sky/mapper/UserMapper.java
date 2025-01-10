package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    /**
     * 根据给定的开始日期和结束日期查询用户
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 返回封装了 User 用户对象的 List 集合
     */
    List<User> list(LocalDate begin, LocalDate end);

    /**
     * 根据给定的开始日期和结束日期查询用户每日的用户数量
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 在日期区间内的用户个数
     */
    Integer getTotal(LocalDateTime begin, LocalDateTime end);
}
