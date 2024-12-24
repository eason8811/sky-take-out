package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

public interface DishService {

    /**
     * 新增菜品
     *
     * @param dishDTO 新增菜品的数据传输对象
     */
    void insert(DishDTO dishDTO);

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO 分页查询菜品信息的数据传输对象
     * @return 返回PageResult格式的对象
     */
    PageResult list(DishPageQueryDTO dishPageQueryDTO);
}
