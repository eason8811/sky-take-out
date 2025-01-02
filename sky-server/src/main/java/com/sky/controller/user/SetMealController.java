package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetMealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    /**
     * 根据提供的分类 ID 查询套餐信息
     *
     * @param categoryId 提供的分类 ID
     * @return 返回Result格式的对象
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "setMealCategory", key = "#categoryId")
    public Result<List<Setmeal>> list(Long categoryId){
        log.info("根据提供的分类 ID 查询套餐信息, 分类的 ID 为: {}", categoryId);
        List<Setmeal> setmealList = setMealService.listByCategoryIdUser(categoryId);
        return Result.success(setmealList);
    }

    /**
     * 根据套餐的 ID 查询其中包含的菜品信息
     *
     * @param id 提供的套餐 ID
     * @return 返回Result格式的对象
     */
    @GetMapping("/dish/{id}")
//    @Cacheable(cacheNames = "setMeal", key = "#id")
    public Result<List<DishItemVO>> listDishBySetMealId(@PathVariable Long id){
        log.info("根据套餐的 ID 查询其中包含的菜品信息, 套餐 ID 为: {}", id);
        List<DishItemVO> dishItemVOList = setMealService.listDishBySetMealId(id);
        return Result.success(dishItemVOList);
    }
}
