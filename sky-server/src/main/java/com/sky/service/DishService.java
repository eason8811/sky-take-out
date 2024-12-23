package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {

    /**
     * 新增菜品
     *
     * @param dishDTO 新增菜品的数据传输对象
     */
    void insert(DishDTO dishDTO);
}
