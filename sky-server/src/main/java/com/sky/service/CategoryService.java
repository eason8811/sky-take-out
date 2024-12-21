package com.sky.service;

import com.sky.dto.CategoryDTO;

public interface CategoryService {

    /**
     * 新增分类
     *
     * @param categoryDTO 添加分类的数据传输对象
     */
    void insert(CategoryDTO categoryDTO);
}
