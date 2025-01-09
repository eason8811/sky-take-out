package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 每分钟处理状态为 待付款 的超时的订单，将其状态修改为 已取消
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("每分钟处理超时订单信息");
        LocalDateTime checkTime = LocalDateTime.now().minusMinutes(15);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, checkTime);
        ordersList.forEach(orders -> {
            orders.setStatus(Orders.CANCELLED);
            LocalDateTime cancelTime = LocalDateTime.now();
            orders.setCancelTime(cancelTime);
            log.info("订单超时, 自动取消, 取消时间为: {}, 订单信息为: {}", cancelTime, orders);
            orderMapper.update(orders);
        });
    }

    /**
     * 每天凌晨 01:00 定时将仍在 派送中 的订单状态修改为 已完成
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("每天凌晨 01:00 定时完成订单");
        LocalDateTime checkTime = LocalDateTime.now().minusMinutes(60);
        // 查询到所有上一个营业日中状态为 派送中 的订单
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, checkTime);
        ordersList.forEach(orders -> {
            orders.setStatus(Orders.COMPLETED);
            orderMapper.update(orders);
        });
    }
}
