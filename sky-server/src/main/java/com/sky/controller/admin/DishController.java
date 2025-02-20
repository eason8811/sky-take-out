package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminDishController")
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
    @CacheEvict(cacheNames = "dish", key = "#dishDTO.categoryId")
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
    @CacheEvict(cacheNames = "dish", allEntries = true)
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
    @CacheEvict(cacheNames = "dish", allEntries = true)
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
    @CacheEvict(cacheNames = "dish", allEntries = true)
    public Result<Object> updateStatus(@PathVariable Integer status,
                                       @RequestParam("id") Long id) {
        log.info("修改菜品的起售、停售状态, 菜品 ID 为: {}, 目标状态为: {}（{}）", id,
                status, status.equals(StatusConstant.ENABLE) ? "启用" : "禁用");
        dishService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 根据ID查询菜品
     *
     * @param id 需要查询的菜品 ID
     * @return 返回Result格式的对象
     */
    @GetMapping("/{id}")
    public Result<DishVO> listById(@PathVariable Long id) {
        log.info("根据 ID 查询菜品信息, ID 为: {}", id);
        DishVO dishVO = dishService.listById(id);
        return Result.success(dishVO);
    }

    /**
     * 根据分类ID查询菜品
     *
     * @param categoryId 需要查询的菜品的分类的ID
     * @return 返回Result格式的对象
     */
    @GetMapping("/list")
    public Result<List<Dish>> listByCategoryId(@RequestParam("categoryId") Long categoryId){
        log.info("根据分类的 ID 查询菜品, ID 为: {}", categoryId);
        List<Dish> dishes = dishService.listByCategoryId(categoryId);
        return Result.success(dishes);
    }

}
