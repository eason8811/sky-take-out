package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 新增菜品中附属的口味
     *
     * @param flavors 新增菜品的数据传输对象
     */
    void insert(List<DishFlavor> flavors);

    /**
     * 根据菜品ID删除口味信息
     * @param ids 需要删除的口味所属的菜品的ID
     * */
    void deleteByDishId(List<Long> ids);

    /**
     * 根据菜品ID查询菜品的口味信息
     *
     * @param id 菜品的 ID
     * @return 返回封装了 DishFlavor 的集合对象
     */
    List<DishFlavor> listByDishId(Long id);
}
