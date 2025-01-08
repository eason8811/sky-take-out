package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    /**
     * 用户提交订单
     *
     * @param orders 用于新增订单的 Orders 实体类对象
     */
    void insert(Orders orders);

    /**
     * 分页查询订单信息
     *
     * @param ordersPageQueryDTO 用于传输分页查询参数的数据传输对象
     * @return 返回封装了 OrderVO 对象的 Page 集合
     */
    Page<OrderVO> list(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 修改订单信息
     *
     * @param orders 用于修改订单信息的实体类对象
     */
    void update(Orders orders);

    /**
     * 根据 ID 查询订单详细信息
     * @param id 需要查询的订单 ID
     * @return 返回 OrderVO 的视图对象
     */
    OrderVO listById(Long id);
}
