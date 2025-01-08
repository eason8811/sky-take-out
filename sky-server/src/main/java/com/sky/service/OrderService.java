package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 用户提交订单
     *
     * @param orders 用于传输订单数据的实体类对象
     * @return 返回 OrderSubmitVO 视图对象
     */
    OrderSubmitVO submit(Orders orders);

    /**
     * 用户进行订单支付
     *
     * @param ordersPaymentDTO 用于传输用户支付的订单信息的数据传输对象
     * @return 返回 OrderPaymentVO 的视图对象
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO);

    /**
     * 分页查询订单信息
     *
     * @param ordersPageQueryDTO 用于传输分页查询参数的数据传输对象
     * @return 返回封装了 OrderVO 对象的 Page 集合的 PageResult 对象
     */
    PageResult list(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据 ID 查询订单详细信息
     * @param id 需要查询的订单 ID
     * @return 返回 OrderVO 的视图对象
     */
    OrderVO listById(Long id);

    /**
     * 根据订单 ID 取消订单
     *
     * @param id 需要取消的订单 ID
     */
    void cancel(Long id);

    /**
     * 根据订单 ID 进行再来一单
     *
     * @param id 需要再来一单的订单 ID
     */
    void repetition(Long id);
}
