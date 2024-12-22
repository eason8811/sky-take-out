package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @return 返回Result格式的对象
     */
    @PostMapping
    public Result<Object> insert(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类, 参数为: {}", categoryDTO);
        categoryService.insert(categoryDTO);
        return Result.success();
    }

    /**
     * 分类的分页查询
     *
     * @param categoryPageQueryDTO 分页查询分类的数据传输对象
     * @return 返回Result格式的对象
     */
    @GetMapping("/page")
    public Result<PageResult> list(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类的分页查询, 参数为: {}", categoryPageQueryDTO);
        PageResult result = categoryService.list(categoryPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 启用、禁用分类
     *
     * @param id     分类的 ID
     * @param status 需要修改的分类目标状态
     * @return 返回Result格式的对象
     */
    @PostMapping("/status/{status}")
    public Result<Object> updateStatus(Long id, @PathVariable Integer status) {
        log.info("{} 分类, ID 为: {}", status.equals(1) ? "启用" : "禁用", id);
        categoryService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 修改分类信息
     *
     * @param categoryDTO 修改分类的数据传输对象
     * @return 返回Result格式的对象
     */
    @PutMapping
    public Result<Object> update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类信息, 参数为: {}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

}
