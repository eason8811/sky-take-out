package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 获取购物车中的商品信息
     *
     * @return 返回Result格式的对象
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        log.info("用户正在查看购物车中的商品信息");
        List<ShoppingCart> shoppingCartList = shoppingCartService.list();
        return Result.success(shoppingCartList);
    }

    /**
     * 删除购物车中的一个商品
     *
     * @param shoppingCartDTO 需要删除的商品信息
     * @return 返回Result格式的对象
     */
    @PostMapping("/sub")
    public Result<Object> sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("用户正在删除购物车中的一个商品, 参数为: {}", shoppingCartDTO);
        shoppingCartService.sub(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 清空购物车
     *
     * @return 返回Result格式的对象
     */
    @DeleteMapping("/clean")
    public Result<Object> clean(){
        log.info("用户正在清空购物车");
        shoppingCartService.clean();
        return Result.success();
    }
}
