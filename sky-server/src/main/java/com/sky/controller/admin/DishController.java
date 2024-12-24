package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Result<Object> insert(@RequestBody DishDTO dishDTO) {
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
    public Result<PageResult> list(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品, 参数为: {}", dishPageQueryDTO);
        PageResult result = dishService.list(dishPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 删除菜品
     *
     * @param ids 通过Query参数输入的需要删除的id数组
     * @return 返回Result格式的对象
     */
    @DeleteMapping
    public Result<Object> delete(@RequestParam("ids") List<Long> ids) {
        log.info("删除菜品信息, 需要删除的ID为: {}", ids);
        dishService.delete(ids);
        return Result.success();
    }

    /**
     * 修改菜品
     *
     * @param dishDTO 修改菜品的数据传输对象
     * @return 返回Result格式的对象
     */
    @PutMapping
    public Result<Object> update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品, 参数为: {}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * 修改菜品起售、停售状态
     *
     * @param status 需要修改的菜品的目标状态
     * @param id     需要修改的菜品的 ID
     * @return 返回Result格式的对象
     */
    @PostMapping("/status/{status}")
    public Result<Object> updateStatus(@PathVariable Integer status,
                                       @RequestParam("id") Long id) {
        log.info("修改菜品的起售、停售状态, 菜品 ID 为: {}, 目标状态为: {}", id, status);
        dishService.updateStatus(status, id);
        return Result.success();
    }

}
