package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetMealMapper {

    Integer getCount(Long id);
}
