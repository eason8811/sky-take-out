package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    /**
     * 根据 DishId 获取 setMeal 的 ID
     *
     * @param ids 需要查询的 DishId
     * @return 返回封装了SetMealId（Long类型）的 List 集合
     */
    List<Long> listSetMealIdByDishId(List<Long> ids);

    /**
     * 新增套餐与菜品的关联信息
     *
     * @param setmealDishes 用于新增套餐的 套餐与菜品关联信息的对象
     */
    void insert(List<SetmealDish> setmealDishes);
}
