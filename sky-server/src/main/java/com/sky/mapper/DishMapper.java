package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper {

    Integer getCount(Long id);
}
