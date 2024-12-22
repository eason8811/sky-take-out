package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    /**
     * 新增分类
     *
     * @param category 添加的分类的实体类对象
     */
    @AutoFill(OperationType.INSERT)
    void insert(Category category);

    /**
     * 分类的分页查询
     *
     * @param categoryPageQueryDTO 分页查询分类的数据传输对象
     * @return 返回Page格式的对象
     */
    Page<Category> list(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据 category 实体类对象修改分类信息
     *
     * @param category 分类的实体类对象
     */
    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    /**
     * 根据 ID 删除分类信息
     *
     * @param id 需要删除的分类 ID
     */
    void delete(Long id);
}
