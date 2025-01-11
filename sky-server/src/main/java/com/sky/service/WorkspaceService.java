package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import java.time.LocalDateTime;

public interface WorkspaceService {

    /**
     * 根据时间段统计营业数据
     *
     * @param begin 开始时间
     * @param end 结束时间
     * @return 返回 BusinessDataVO 的视图对象
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 查询订单管理数据
     *
     * @return 返回 OrderOverViewVO 的视图对象
     */
    OrderOverViewVO getOrderOverView();

    /**
     * 查询菜品总览
     *
     * @return 返回 DishOverViewVO 的视图对象
     */
    DishOverViewVO getDishOverView();

    /**
     * 查询套餐总览
     *
     * @return 返回 SetmealOverViewVO 的视图对象
     */
    SetmealOverViewVO getSetmealOverView();

}
