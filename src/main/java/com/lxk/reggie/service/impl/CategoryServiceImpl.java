package com.lxk.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxk.reggie.common.CustomException;
import com.lxk.reggie.entity.Category;
import com.lxk.reggie.entity.Dish;
import com.lxk.reggie.entity.Setmeal;
import com.lxk.reggie.mapper.CategoryMapper;
import com.lxk.reggie.service.CategoryService;
import com.lxk.reggie.service.DishService;
import com.lxk.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void removeById(long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(dishLambdaQueryWrapper);
        if (count > 0) {
            //抛异常
            throw new CustomException("与其他菜品关联,不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if (count1 > 0) {
            //抛异常
            throw new CustomException("与其他套餐关联,不能删除");
        }
        super.removeById(id);
    }
}
