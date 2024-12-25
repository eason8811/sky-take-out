package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

public interface SetMealService {

    /**
     * 新增套餐
     *
     * @param setmealDTO 用于新增套餐的数据传输对象
     */
    void insert(SetmealDTO setmealDTO);

    /**
     * 分页查询套餐信息
     *
     * @param setmealPageQueryDTO 用于查询套餐信息的数据传输对象
     * @return 返回 PageResult 格式的对象
     */
    PageResult list(SetmealPageQueryDTO setmealPageQueryDTO);
}
