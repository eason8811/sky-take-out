package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 新增菜品
     *
     * @param dishDTO 新增菜品的数据传输对象
     */
    @Transactional
    @Override
    public void insert(DishDTO dishDTO) {
        // 添加菜品本体
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);

        // 添加菜品的口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(flavor -> flavor.setDishId(dish.getId()));
        dishFlavorMapper.insert(flavors);
    }

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO 分页查询菜品信息的数据传输对象
     * @return 返回PageResult格式的对象
     */
    @Override
    public PageResult list(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.list(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 删除菜品
     *
     * @param ids 通过Query参数输入的需要删除的id数组
     */
    @Transactional
    @Override
    public void delete(List<Long> ids) {
        // 判断菜品是否启用, 启用的菜品无法被删除
        Integer disableCount = dishMapper.getDisableCount(ids);
        if (disableCount == null || disableCount < ids.size()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        // 判断菜品是否被套餐关联, 如果被关联则无法被删除
        List<Long> setMealIds = setMealDishMapper.listSetMealIdByDishId(ids);
        if (!setMealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除菜品以及口味
        dishMapper.delete(ids);
        dishFlavorMapper.deleteByDishId(ids);
    }

    /**
     * 修改菜品
     *
     * @param dishDTO 修改菜品的数据传输对象
     */
    @Transactional
    @Override
    public void update(DishDTO dishDTO) {
        // 更改菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        // 先删除菜品的口味信息，再重新添加
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(flavor -> flavor.setDishId(dish.getId()));
        dishFlavorMapper.deleteByDishId(List.of(dish.getId()));
        dishFlavorMapper.insert(flavors);
    }

    /**
     * 修改菜品起售、停售状态
     *
     * @param status 需要修改的菜品的目标状态
     * @param id     需要修改的菜品的 ID
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.update(dish);
    }

    /**
     * 根据ID查询菜品
     *
     * @param id 需要查询的菜品 ID
     * @return 返回 DishVO 格式的对象
     */
    @Override
    public DishVO listById(Long id) {
        // 根据ID查询菜品信息
        DishVO dishVO = dishMapper.listById(id);

        // 根据菜品的ID查询菜品的口味信息
        List<DishFlavor> flavors = dishFlavorMapper.listByDishId(id);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * 根据分类ID查询菜品
     *
     * @param categoryId 需要查询的菜品的分类的ID
     * @return 返回封装了 Dish 的集合对象
     */
    @Override
    public List<Dish> listByCategoryId(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        return dishMapper.listByCategoryId(dish);
    }

    /**
     * 根据分类ID查询菜品
     *
     * @param categoryId 需要查询的菜品的分类的ID
     * @return 返回封装了 DishVO 视图对象的集合对象
     */
    @Override
    public List<DishVO> listByCategoryIdUser(Long categoryId) {
        // 根据ID查询菜品信息
        Dish dishParam = new Dish();
        dishParam.setCategoryId(categoryId);
        dishParam.setStatus(StatusConstant.ENABLE);
        List<Dish> dishes = dishMapper.listByCategoryId(dishParam);

        // 根据每个菜品的ID查询菜品的口味信息
        return dishes.stream()
                .map(dish -> {
                    DishVO dishVO = new DishVO();
                    BeanUtils.copyProperties(dish, dishVO);
                    List<DishFlavor> flavors = dishFlavorMapper.listByDishId(dish.getId());
                    dishVO.setFlavors(flavors);
                    return dishVO;
                })
                .toList();
    }
}
