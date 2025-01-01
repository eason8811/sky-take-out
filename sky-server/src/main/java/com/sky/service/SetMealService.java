package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

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

    /**
     * 修改套餐起售、停售状态
     *
     * @param status 需要修改的套餐的目标状态
     * @param id     需要修改的套餐 ID
     */
    void updateStatus(Integer status, Long id);

    /**
     * 批量删除套餐
     *
     * @param ids 需要删除的套餐的 ID 信息集合
     */
    void delete(List<Long> ids);

    /**
     * 根据 ID 查询套餐信息
     *
     * @param id 需要查询的套餐 ID
     * @return 返回 SetmealVO 视图对象
     */
    SetmealVO listById(Long id);

    /**
     * 修改套餐信息
     *
     * @param setmealDTO 需要修改的菜品的信息
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 根据提供的分类 ID 查询套餐信息
     *
     * @param categoryId 提供的分类 ID
     * @return 返回封装了 Setmeal 对象的 List 集合
     */
    List<Setmeal> listByCategoryIdUser(Long categoryId);

    /**
     * 根据套餐的 ID 查询其中包含的菜品信息
     *
     * @param id 提供的套餐 ID
     * @return 返回封装了 DishItemVO 视图对象的 List 集合
     */
    List<DishItemVO> listDishBySetMealId(Long id);
}
