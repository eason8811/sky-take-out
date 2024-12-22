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
}
