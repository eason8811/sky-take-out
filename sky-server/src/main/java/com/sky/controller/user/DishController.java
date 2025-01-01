package com.sky.controller.user;

import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 根据给定的分类 ID 查询菜品
     *
     * @param categoryId 给定的分类 ID
     * @return 返回Result格式的对象
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId){
        log.info("根据提供的分类 ID 查询菜品, 分类 ID 为: {}", categoryId);
        List<DishVO> dishVOList = dishService.listByCategoryIdUser(categoryId);
        return Result.success(dishVOList);
    }
}
