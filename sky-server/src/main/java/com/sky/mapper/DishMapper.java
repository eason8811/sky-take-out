package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO 分页查询菜品信息的数据传输对象
     * @return 返回Page格式的集合
     */
    Page<DishVO> list(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除菜品
     *
     * @param ids 通过Query参数输入的需要删除的id数组
     */
    void delete(List<Long> ids);

    /**
     * 修改菜品
     *
     * @param dish 修改菜品的实体类对象
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

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
     * @param dishParam 需要查询的菜品的查询对象
     * @return 返回封装了 Dish 的集合对象
     */
    List<Dish> listByCategoryId(Dish dishParam);

    /**
     * 获取提供的 ID 集合中status为禁用的对象
     *
     * @param ids 需要获取的 ID 集合
     * @return 禁用的菜品的数量
     */
    Integer getDisableCount(List<Long> ids);
}
