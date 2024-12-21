package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param categoryDTO 添加分类的数据传输对象
     */
    @PostMapping
    public Result<Object> insert(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类, 参数为: {}", categoryDTO);
        categoryService.insert(categoryDTO);
        return Result.success();
    }
}
