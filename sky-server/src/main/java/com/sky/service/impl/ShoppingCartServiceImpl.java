package com.sky.service.impl;

import com.sky.context.UserContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * 向购物车中添加套餐或菜品
     *
     * @param shoppingCartDTO 用于向购物车中添加套餐或菜品的数据传输对象
     */
    @Override
    public void insert(ShoppingCartDTO shoppingCartDTO) {
        Long userId = UserContext.getCurrentId();
        Long dishId = shoppingCartDTO.getDishId();
        Long setMealId = shoppingCartDTO.getSetmealId();
        String dishFlavor = shoppingCartDTO.getDishFlavor();
        String name;
        String image;
        BigDecimal amount;
        // 根据菜品 ID 获取该菜品的基本信息
        if (dishId != null) {
            log.info("正在向购物车中添加 菜品...");
            DishVO dishVO = dishMapper.listById(dishId);
            name = dishVO.getName();
            image = dishVO.getImage();
            amount = dishVO.getPrice();
        }
        // 根据套餐 ID 获取该套餐的基本信息
        else {
            log.info("正在向购物车中添加 套餐...");
            SetmealVO setmealVO = setMealMapper.listById(setMealId);
            name = setmealVO.getName();
            image = setmealVO.getImage();
            amount = setmealVO.getPrice();
        }
        // 若购物车中没有该菜品或套餐，则构建新对象插入
        ShoppingCart queryShoppingCart = ShoppingCart.builder()
                .userId(userId)
                .dishId(dishId)
                .dishFlavor(dishFlavor)
                .setmealId(setMealId)
                .build();
        List<ShoppingCart> shoppingCartItem = shoppingCartMapper.list(queryShoppingCart);
        if (shoppingCartItem == null || shoppingCartItem.isEmpty()) {
            log.info("购物车中没有该 菜品/套餐 直接新增数据");
            ShoppingCart shoppingCart = ShoppingCart.builder()
                    .name(name)
                    .userId(userId)
                    .dishId(dishId)
                    .setmealId(setMealId)
                    .dishFlavor(dishFlavor)
                    .image(image)
                    .number(1)
                    .amount(amount)
                    .createTime(LocalDateTime.now())
                    .build();
            shoppingCartMapper.insert(shoppingCart);
        }
        // 若有，则修改菜品或套餐的数量和价格
        else {
            log.info("购物车中已有该 菜品/套餐 修改其数量和价格");
            ShoppingCart shoppingCart = ShoppingCart.builder()
                    .userId(userId)
                    .dishId(dishId)
                    .setmealId(setMealId)
                    .dishFlavor(dishFlavor)
                    .number(shoppingCartItem.get(0).getNumber() + 1)
                    .build();
            shoppingCartMapper.update(shoppingCart);
        }
    }
}
