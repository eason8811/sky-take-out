package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单详细信息
     * @param orderDetailList 封装了订单详细信息 OrderDetail 对象的 List 集合
     */
    void insertBatch(List<OrderDetail> orderDetailList);
}
