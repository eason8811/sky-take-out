package com.sky.service;

import com.sky.dto.OrdersDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderSubmitVO;

public interface OrderService {

    /**
     * 用户提交订单
     *
     * @param orders 用于传输订单数据的实体类对象
     * @return 返回 OrderSubmitVO 视图对象
     */
    OrderSubmitVO submit(Orders orders);
}
