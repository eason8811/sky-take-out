package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
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

@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

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
}
