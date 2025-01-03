package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 根据用户 ID 和 菜品 ID 或套餐 ID 查询购物车信息
     *
     * @param shoppingCart 需要查询的购物车商品对象
     * @return 返回 ShoppingCart 对象
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 新增购物车项目
     * @param shoppingCart 购物车项目的实体类对象
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 修改购物车项目信息
     * @param shoppingCart 需要修改的目标信息对象
     */
    void update(ShoppingCart shoppingCart);
}
