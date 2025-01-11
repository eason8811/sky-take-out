package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setmealMapper;

    /**
     * 根据时间段统计营业数据
     *
     * @param begin 开始时间
     * @param end 结束时间
     * @return 返回 BusinessDataVO 的视图对象
     */
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        /*
          营业额：当日已完成订单的总金额
          有效订单：当日已完成订单的数量
          订单完成率：有效订单数 / 总订单数
          平均客单价：营业额 / 有效订单数
          新增用户：当日新增用户的数量
         */


        //查询总订单数
        Integer totalOrderCount = orderMapper.getCount(begin, end);

        //营业额
        Double turnover = orderMapper.getTurnover(begin, end, Orders.COMPLETED);
        turnover = turnover == null? 0.0 : turnover;

        //有效订单数
        Integer validOrderCount = orderMapper.getValidCount(begin, end, Orders.COMPLETED);

        double unitPrice = 0.0;

        double orderCompletionRate = 0.0;
        if(totalOrderCount != 0 && validOrderCount != 0){
            //订单完成率
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            //平均客单价
            unitPrice = turnover / validOrderCount;
        }

        //新增用户数
        Integer totalToday = userMapper.getTotal(begin, end);
        Integer totalYesterday = userMapper.getTotal(begin.minusDays(1), end.minusDays(1));
        Integer newUsers = totalToday - totalYesterday;

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }


    /**
     * 查询订单管理数据
     *
     * @return 返回 OrderOverViewVO 的视图对象
     */
    public OrderOverViewVO getOrderOverView() {
        //待接单
        Integer waitingOrders = orderMapper.getValidCount(LocalDateTime.now().with(LocalTime.MIN), null, Orders.TO_BE_CONFIRMED);

        //待派送
        Integer deliveredOrders = orderMapper.getValidCount(LocalDateTime.now().with(LocalTime.MIN), null, Orders.CONFIRMED);

        //已完成
        Integer completedOrders = orderMapper.getValidCount(LocalDateTime.now().with(LocalTime.MIN), null, Orders.COMPLETED);

        //已取消
        Integer cancelledOrders = orderMapper.getValidCount(LocalDateTime.now().with(LocalTime.MIN), null, Orders.CANCELLED);

        //全部订单
        Integer allOrders = orderMapper.getValidCount(LocalDateTime.now().with(LocalTime.MIN), null, null);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 查询菜品总览
     *
     * @return 返回 DishOverViewVO 的视图对象
     */
    public DishOverViewVO getDishOverView() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 查询套餐总览
     *
     * @return 返回 SetmealOverViewVO 的视图对象
     */
    public SetmealOverViewVO getSetmealOverView() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
