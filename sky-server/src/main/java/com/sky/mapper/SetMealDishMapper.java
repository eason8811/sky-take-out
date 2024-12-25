package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    /**
     * 根据 DishId 获取 setMeal 的 ID
     *
     * @param ids 需要查询的 DishId
     * @return 返回封装了SetMealId（Long类型）的 List 集合
     * */
    List<Long> listSetMealIdByDishId(List<Long> ids);
}
