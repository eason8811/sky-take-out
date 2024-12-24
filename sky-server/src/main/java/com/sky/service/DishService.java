package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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

    /**
     * 修改菜品起售、停售状态
     *
     * @param status 需要修改的菜品的目标状态
     * @param id     需要修改的菜品的 ID
     */
    void updateStatus(Integer status, Long id);

    /**
     * 根据ID查询菜品
     *
     * @param id 需要查询的菜品 ID
     * @return 返回 DishVO 格式的对象
     */
    DishVO listById(Long id);

    /**
     * 根据分类ID查询菜品
     *
     * @param categoryId 需要查询的菜品的分类的ID
     * @return 返回封装了 Dish 的集合对象
     */
    List<Dish> listByCategoryId(Long categoryId);
}
