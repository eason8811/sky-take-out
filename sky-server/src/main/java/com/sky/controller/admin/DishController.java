package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDTO 新增菜品的数据传输对象
     * @return 返回Result格式的对象
     */
    @PostMapping
    public Result<Object> insert(@RequestBody DishDTO dishDTO){
        log.info("新增菜品, 参数为: {}", dishDTO);
        dishService.insert(dishDTO);
        return Result.success();
    }

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO 分页查询菜品信息的数据传输对象
     * @return 返回Result格式的对象
     */
    @GetMapping("/page")
    public Result<PageResult> list(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询菜品, 参数为: {}", dishPageQueryDTO);
        PageResult result = dishService.list(dishPageQueryDTO);
        return Result.success(result);
    }
}
