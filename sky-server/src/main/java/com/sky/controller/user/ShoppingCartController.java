package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 向购物车中添加套餐或菜品
     *
     * @param shoppingCartDTO 用于向购物车中添加套餐或菜品的数据传输对象
     * @return 返回Result格式的对象
     */
    @PostMapping("/add")
    public Result<Object> insert(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("用户正在向购物车中添加菜品或套餐, 参数为: {}", shoppingCartDTO);
        shoppingCartService.insert(shoppingCartDTO);
        return Result.success();
    }
}
