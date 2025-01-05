package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    /**
     * 用户提交订单
     *
     * @param orders 用于新增订单的 Orders 实体类对象
     */
    void insert(Orders orders);
}
