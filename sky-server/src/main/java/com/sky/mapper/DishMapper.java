package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper {

    /**
     * 根据分类 ID 获取菜品数量
     *
     * @param id 分类的 ID
     */
    Integer getCount(Long id);

    /**
     * 新增菜品
     *
     * @param dish 新增菜品的实体类对象
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

}
