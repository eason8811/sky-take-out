package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    /**
     * 新增分类
     *
     * @param category 添加的分类的实体类对象
     */
    void insert(Category category);

    /**
     * 分类的分页查询
     *
     * @param categoryPageQueryDTO 分页查询分类的数据传输对象
     * @return 返回Page格式的对象
     */
    Page<Category> list(CategoryPageQueryDTO categoryPageQueryDTO);
}
