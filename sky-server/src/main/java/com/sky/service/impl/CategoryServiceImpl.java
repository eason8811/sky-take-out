package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 新增分类
     *
     * @param categoryDTO 添加分类的数据传输对象
     */
    @Override
    @AutoFill(OperationType.INSERT)
    public void insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(StatusConstant.DISABLE);     // 默认新增的分类状态为禁用
        /*category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());*/
        categoryMapper.insert(category);
    }

    /**
     * 分类的分页查询
     *
     * @param categoryPageQueryDTO 分页查询分类的数据传输对象
     * @return 返回PageResult格式的对象
     */
    @Override
    public PageResult list(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.list(categoryPageQueryDTO);
        long total = page.getTotal();
        return new PageResult(total, page);
    }

    /**
     * 启用、禁用分类
     *
     * @param id     分类的 ID
     * @param status 需要修改的分类目标状态
     */
    @Override
    @AutoFill(OperationType.UPDATE)
    public void updateStatus(Long id, Integer status) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                /*.updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())*/
                .build();
        categoryMapper.update(category);
    }

    /**
     * 修改分类信息
     *
     * @param categoryDTO 修改分类的数据传输对象
     */
    @Override
    @AutoFill(OperationType.UPDATE)
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        /*category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());*/
        categoryMapper.update(category);
    }

    /**
     * 根据 ID 删除分类信息
     *
     * @param id 需要删除的分类 ID
     */
    @Override
    public void delete(Long id) {
        // 检查菜品分类中有无关联菜品
        Integer dishCount = dishMapper.getCount(id);
        if (dishCount > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        // 检查套餐分类中有无关联套餐
        Integer setMealCount = setMealMapper.getCount(id);
        if (setMealCount > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        // 都没有关联则执行删除
        categoryMapper.delete(id);
    }

    /**
     * 根据type查询分类信息
     *
     * @param type 输入的分类
     * @return 返回包含 Category 对象的List集合
     */
    @Override
    public List<Category> listByType(Integer type) {
        return categoryMapper.listByType(type);
    }
}
