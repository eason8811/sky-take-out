package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public Result<List<Setmeal>> list(Long categoryId){
        log.info("根据提供的分类 ID 查询套餐信息, 分类的 ID 为: {}", categoryId);
        List<Setmeal> setmealList = setMealService.listByCategoryIdUser(categoryId);
        return Result.success(setmealList);
    }
}
