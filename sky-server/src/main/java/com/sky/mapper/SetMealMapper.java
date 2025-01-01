package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealMapper {

    /**
     * 根据分类的 ID 获取其关联的套餐的数量
     *
     * @param categoryId 需要查询的分类的 ID
     * @return 与提供的 ID 的分类相关联的套餐数量
     */
    Integer getCount(Long categoryId);

    /**
     * 新增套餐
     *
     * @param setmeal 用于新增套餐的实体类对象
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 分页查询套餐信息
     *
     * @param setmealPageQueryDTO 用于查询套餐信息的数据传输对象
     * @return 返回封装了 SetmealVO 的 Page 集合对象
     */
    Page<SetmealVO> list(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 修改套餐起售、停售状态
     *
     * @param setmeal 需要修改的套餐的实体类对象
     */
    void update(Setmeal setmeal);

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
     * 获取状态为禁用的套餐的数量
     *
     * @param ids 需要获取的状态为禁用的套餐的数量
     * @return 状态为禁用的套餐的数量
     */
    Integer getDisableCount(List<Long> ids);

    /**
     * 根据提供的分类 ID 查询套餐信息
     *
     * @param categoryId 提供的分类 ID
     * @return 返回封装了 Setmeal 对象的 List 集合
     */
    List<Setmeal> listByCategoryId(Long categoryId);
}
