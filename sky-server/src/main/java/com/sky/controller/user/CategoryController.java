package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据分类类型查询分类
     *
     * @param type 需要查找的分类类型
     * @return 返回Result格式的对象
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type){
        log.info("用户正在根据分类类型查询分类, 类型为: {}", type);
        List<Category> categories = categoryService.listByType(type);
        return Result.success(categories);
    }
}
