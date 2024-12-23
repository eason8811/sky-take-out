package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

    /**
     * 新增分类
     *
     * @param categoryDTO 添加分类的数据传输对象
     */
    void insert(CategoryDTO categoryDTO);

    /**
     * 分类的分页查询
     *
     * @param categoryPageQueryDTO 分页查询分类的数据传输对象
     * @return 返回PageResult格式的对象
     */
    PageResult list(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用、禁用分类
     *
     * @param id     分类的 ID
     * @param status 需要修改的分类目标状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 修改分类信息
     *
     * @param categoryDTO 修改分类的数据传输对象
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 根据 ID 删除分类信息
     *
     * @param id 需要删除的分类 ID
     */
    void delete(Long id);

    /**
     * 根据type查询分类信息
     *
     * @param type 输入的分类
     * @return 返回包含 Category 对象的List集合
     */
    List<Category> listByType(Integer type);
}
