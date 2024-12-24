package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

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

    /**
     * 删除菜品
     *
     * @param ids 通过Query参数输入的需要删除的id数组
     */
    void delete(List<Long> ids);

    /**
     * 修改菜品
     *
     * @param dishDTO 修改菜品的数据传输对象
     */
    void update(DishDTO dishDTO);
}
