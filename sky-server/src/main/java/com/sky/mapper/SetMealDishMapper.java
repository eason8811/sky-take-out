package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
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

    /**
     * 批量删除套餐与菜品的关联信息
     *
     * @param setMealIds 需要删除的套餐的 ID 信息集合
     */
    void delete(List<Long> setMealIds);

    /**
     * 根据 setMealId 查询套餐与菜品的关联信息
     *
     * @param setMealId 需要查询的套餐 ID
     * @return 返回封装了 SetmealDish 对象的 List 集合
     */
    List<SetmealDish> listBySetMealId(Long setMealId);

    /**
     * 根据 setMealId 查询套餐与菜品的关联信息，以及具体关联的菜品信息
     * @param setMealId 需要查询的套餐 ID
     * @return 返回封装了 DishItemVO 对象的 List 集合
     */
    List<DishItemVO> listSetMealDishInfoBySetMealId(Long setMealId);
}
