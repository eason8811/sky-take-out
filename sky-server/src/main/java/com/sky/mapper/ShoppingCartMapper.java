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
     *
     * @param shoppingCart 购物车项目的实体类对象
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 修改购物车项目信息
     *
     * @param shoppingCart 需要修改的目标信息对象
     */
    void update(ShoppingCart shoppingCart);

    /**
     * 批量删除购物车中的商品
     *
     * @param shoppingCartList 封装了需要删除的商品信息的实体类对象 shoppingCart 的 List 集合
     */
    void delete(List<ShoppingCart> shoppingCartList);

    /**
     * 批量新增购物车项目
     *
     * @param shoppingCartList 封装了 ShoppingCart 购物车项目实体类对象的 List 集合
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
