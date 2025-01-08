package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户提交订单
     *
     * @param orders 用于传输订单数据的实体类对象
     * @return 返回Result格式的对象
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody Orders orders){
        log.info("用户提交订单, 参数为: {}", orders);
        OrderSubmitVO orderSubmitVO = orderService.submit(orders);
        return Result.success(orderSubmitVO);
    }

    /**
     * 用户进行订单支付
     *
     * @param ordersPaymentDTO 用于传输用户支付的订单信息的数据传输对象
     * @return 返回Result格式的对象
     */
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO){
        log.info("用户正在进行订单支付, 参数为: {}", ordersPaymentDTO);
        OrderPaymentVO payment = orderService.payment(ordersPaymentDTO);
        return Result.success(payment);
    }

    /**
     * 分页查询订单信息
     *
     * @param ordersPageQueryDTO 用于传输分页查询参数的数据传输对象
     * @return 返回Result格式的对象
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> list(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("正在分页查询订单信息, 参数为: {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.list(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据 ID 查询订单详细信息
     * @param id 需要查询的订单 ID
     * @return 返回Result格式的对象
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> listById(@PathVariable Long id){
        log.info("根据 ID 查询订单详细信息, ID 为: {}", id);
        OrderVO orderVO = orderService.listById(id);
        return Result.success(orderVO);
    }
}
