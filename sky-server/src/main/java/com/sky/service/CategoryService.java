package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

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
}
