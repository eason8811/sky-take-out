package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminSetMealController")
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    /**
     * 新增套餐
     *
     * @param setmealDTO 用于新增套餐的数据传输对象
     * @return 返回Result格式的对象
     */
    @PostMapping
    @CacheEvict(cacheNames = "setMealCategory", key = "#setmealDTO.categoryId")
    public Result<Object> insert(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐, 参数为: {}", setmealDTO);
        setMealService.insert(setmealDTO);
        return Result.success();
    }

    /**
     * 分页查询套餐信息
     *
     * @param setmealPageQueryDTO 用于查询套餐信息的数据传输对象
     * @return 返回Result格式的对象
     */
    @GetMapping("/page")
    public Result<PageResult> list(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询套餐信息, 参数为: {}", setmealPageQueryDTO);
        PageResult result = setMealService.list(setmealPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 修改套餐起售、停售状态
     *
     * @param status 需要修改的套餐的目标状态
     * @param id     需要修改的套餐 ID
     * @return 返回Result格式的对象
     */
    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setMealCategory", allEntries = true)
    public Result<Object> updateStatus(@PathVariable Integer status,
                                       @RequestParam("id") Long id){
        log.info("修改套餐起售、停售状态, 修改的菜品 ID: {}, 目标状态: {}（{}）", id,
                status, status.equals(StatusConstant.ENABLE) ? "启用" : "禁用");
        setMealService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 批量删除套餐
     *
     * @param ids 需要删除的套餐的 ID 信息集合
     * @return 返回Result格式的对象
     */
    @DeleteMapping
    @CacheEvict(cacheNames = "setMealCategory", allEntries = true)
    public Result<Object> delete(@RequestParam("ids")List<Long> ids){
        log.info("批量删除套餐, 参数为: {}", ids);
        setMealService.delete(ids);
        return Result.success();
    }

    /**
     * 根据 ID 查询套餐信息
     *
     * @param id 需要查询的套餐 ID
     * @return 返回封装了 SetmealVO 视图对象的 Result 格式的对象
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> listById(@PathVariable Long id){
        log.info("根据 ID 查询套餐信息, ID 为: {}", id);
        SetmealVO setmealVO = setMealService.listById(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐信息
     *
     * @param setmealDTO 需要修改的菜品的信息
     * @return 返回Result格式的对象
     */
    @PutMapping
    @CacheEvict(cacheNames = "setMealCategory", allEntries = true)
    public Result<Object> update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改菜品信息, 参数为: {}", setmealDTO);
        setMealService.update(setmealDTO);
        return Result.success();
    }


}
