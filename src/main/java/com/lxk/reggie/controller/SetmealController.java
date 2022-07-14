package com.lxk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxk.reggie.common.CustomException;
import com.lxk.reggie.common.R;
import com.lxk.reggie.dto.SetmealDto;
import com.lxk.reggie.entity.Category;
import com.lxk.reggie.entity.Dish;
import com.lxk.reggie.entity.Setmeal;
import com.lxk.reggie.entity.SetmealDish;
import com.lxk.reggie.service.CategoryService;
import com.lxk.reggie.service.DishService;
import com.lxk.reggie.service.SetmealDishService;
import com.lxk.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    SetmealService setmealService;
    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            wrapper.like(Setmeal::getName, name);
        }
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, wrapper);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> dtoList = new ArrayList<>();
        for (Setmeal record : records) {
            Long categoryId = record.getCategoryId();
            String categoryName = categoryService.getById(categoryId).getName();
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(record, setmealDto);
            setmealDto.setCategoryName(categoryName);
            dtoList.add(setmealDto);
        }
        setmealDtoPage.setRecords(dtoList);
        return R.success(setmealDtoPage);
    }

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long id = setmealDto.getId();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(id);
        }
        setmealDishService.saveBatch(setmealDishes);
        return R.success("新增套餐成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getSetmeal(@PathVariable long id) {
        SetmealDto setmealWithDishes = setmealService.getSetmealWithDishes(id);
        return R.success(setmealWithDishes);
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        setmealService.updateSetmealWithDishes(setmealDto);
        return R.success("修改成功");
    }

    @PostMapping("/status/{status}")
    public R<String> disabled(@PathVariable int status, @RequestParam List<Long> ids) {
        List<Setmeal> setmeals = setmealService.listByIds(ids);
        for (Setmeal setmeal : setmeals) {
            setmeal.setStatus(status);
        }
        setmealService.updateBatchById(setmeals);
        return R.success("修改状态成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getStatus, 1);
        queryWrapper.in(Setmeal::getId, ids);
        int count = setmealService.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("商品正在售卖中,不能删除");
        }
        setmealService.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(wrapper);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(long categoryId, int status) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId, categoryId).eq(Setmeal::getStatus,1);
        List<Setmeal> list = setmealService.list(wrapper);
        return R.success(list);
    }
}
