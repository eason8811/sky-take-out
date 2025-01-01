package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     *
     * @param setmealDTO 用于新增套餐的数据传输对象
     */
    @Transactional
    @Override
    public void insert(SetmealDTO setmealDTO) {
        System.out.println(setmealDTO);
        // 复制信息到setMeal实体类对象
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.insert(setmeal);

        // 获取该套餐与菜品的关联信息
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 通过主键返回获取套餐的 ID
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setMealDishMapper.insert(setmealDishes);
    }

    /**
     * 分页查询套餐信息
     *
     * @param setmealPageQueryDTO 用于查询套餐信息的数据传输对象
     * @return 返回 PageResult 格式的对象
     */
    @Override
    public PageResult list(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setMealMapper.list(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改套餐起售、停售状态
     *
     * @param status 需要修改的套餐的目标状态
     * @param id     需要修改的套餐 ID
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        if (Objects.equals(status, StatusConstant.ENABLE)){
            // 检查套餐中的菜品是否存在未起售菜品
            // 先获取需要修改状态的套餐 与菜品之间的关系数据
            List<SetmealDish> setmealDishes = setMealDishMapper.listBySetMealId(id);
            List<Long> dishIds = setmealDishes.stream()
                    .map(SetmealDish::getDishId)
                    .toList();

            // 判断是否状态为禁用的菜品数目为0
            Integer disableCount = dishMapper.getDisableCount(dishIds);
            if (disableCount != null){
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }

        // 全部为启用状态则启用套餐
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setMealMapper.update(setmeal);
    }

    /**
     * 批量删除套餐
     *
     * @param ids 需要删除的套餐的 ID 信息集合
     */
    @Transactional
    @Override
    public void delete(List<Long> ids) {
        Integer disableCount = setMealMapper.getDisableCount(ids);
        if (disableCount == null || disableCount < ids.size()) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        // 根据需要删除的套餐 ID 删除套餐
        setMealMapper.delete(ids);
        // 根据需要删除的套餐 ID 删除套餐与菜品的关联信息
        setMealDishMapper.delete(ids);
    }

    /**
     * 根据 ID 查询套餐信息
     *
     * @param id 需要查询的套餐 ID
     * @return 返回 SetmealVO 视图对象
     */
    @Override
    public SetmealVO listById(Long id) {
        // 先查询 setMeal 套餐信息
        SetmealVO setmealVO = setMealMapper.listById(id);
        // 再查询套餐与菜品关联的信息 (setMealDish)
        List<SetmealDish> setmealDishList = setMealDishMapper.listBySetMealId(id);
        setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;
    }

    /**
     * 修改套餐信息
     *
     * @param setmealDTO 需要修改的菜品的信息
     */
    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {
        // 先更新套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.update(setmeal);
        // 先删除套餐与菜品的关联信息, 再重新添加
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        Long setMealId = setmeal.getId();
        setMealDishMapper.delete(List.of(setMealId));
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setMealId));
        setMealDishMapper.insert(setmealDishes);
    }

    /**
     * 根据提供的分类 ID 查询套餐信息
     *
     * @param categoryId 提供的分类 ID
     * @return 返回封装了 Setmeal 对象的 List 集合
     */
    @Override
    public List<Setmeal> listByCategoryIdUser(Long categoryId) {
        return setMealMapper.listByCategoryId(categoryId);
    }
}
