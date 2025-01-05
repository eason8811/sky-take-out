package com.sky.controller.user;

import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
