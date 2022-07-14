package com.lxk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxk.reggie.common.R;
import com.lxk.reggie.dto.DishDto;
import com.lxk.reggie.entity.Dish;
import com.lxk.reggie.entity.DishFlavor;
import com.lxk.reggie.service.CategoryService;
import com.lxk.reggie.service.DishFlavorService;
import com.lxk.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Dish::getName, name);
        wrapper.orderByAsc(Dish::getSort);
        dishService.page(dishPage, wrapper);
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        List<Dish> records = dishPage.getRecords();
        List<DishDto> list = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(records.get(i), dishDto);
            Long categoryId = records.get(i).getCategoryId();
            String categoryName = categoryService.getById(categoryId).getName();
            dishDto.setCategoryName(categoryName);
            list.add(dishDto);
        }
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    @PostMapping("/status/{status}")
    public R<String> disabled(@PathVariable int status, @RequestParam List<Long> ids) {
        List<Dish> dishes = dishService.listByIds(ids);
        for (int i = 0; i < dishes.size(); i++) {
            dishes.get(i).setStatus(status);
        }
        dishService.updateBatchById(dishes);
        return R.success("更改状态成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        dishService.removeByIds(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(long categoryId,int status) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, categoryId);
        wrapper.eq(Dish::getStatus, status);
        wrapper.orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(wrapper);
        List<DishDto> dtoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(list.get(i), dishDto);
            Long dishId = list.get(i).getId();
            LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(flavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            dtoList.add(dishDto);
        }
        return R.success(dtoList);
    }
}
