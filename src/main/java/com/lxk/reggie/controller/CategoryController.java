package com.lxk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxk.reggie.common.R;
import com.lxk.reggie.entity.Category;
import com.lxk.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        Page<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        categoryService.page(categoryPage, wrapper);
        return R.success(categoryPage);
    }

    @DeleteMapping
    public R<String> delete(long ids) {
        categoryService.removeById(ids);
        return R.success("删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        Integer type = category.getType();
        List<Category> list=null;
        if (type != null) {
            LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Category::getType, type);
            wrapper.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);
            list = categoryService.list(wrapper);
        } else {
            list = categoryService.list();
        }
        return R.success(list);
    }
}
