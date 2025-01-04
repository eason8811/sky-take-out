package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 向购物车中添加套餐或菜品
     *
     * @param shoppingCartDTO 用于向购物车中添加套餐或菜品的数据传输对象
     */
    void insert(ShoppingCartDTO shoppingCartDTO);

    /**
     * 获取购物车中的商品信息
     *
     * @return 返回封装了 ShoppingCart 对象的 List 集合
     */
    List<ShoppingCart> list();

    /**
     * 删除购物车中的一个商品
     *
     * @param shoppingCartDTO 需要删除的商品信息
     */
    void sub(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     */
    void clean();

}
