package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

public interface ShoppingCartService {

    /**
     * 向购物车中添加套餐或菜品
     *
     * @param shoppingCartDTO 用于向购物车中添加套餐或菜品的数据传输对象
     */
    void insert(ShoppingCartDTO shoppingCartDTO);
}
