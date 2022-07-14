package com.lxk.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxk.reggie.dto.DishDto;
import com.lxk.reggie.entity.Dish;
import com.lxk.reggie.entity.DishFlavor;
import com.lxk.reggie.mapper.DishMapper;
import com.lxk.reggie.service.DishFlavorService;
import com.lxk.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    DishFlavorService dishFlavorService;

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        for (int i = 0; i < dishDto.getFlavors().size(); i++) {
            dishDto.getFlavors().get(i).setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch((dishDto.getFlavors()));
    }

    @Override
    public DishDto getByIdWithFlavor(long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavorList = dishFlavorService.list(wrapper);
        dishDto.setFlavors(flavorList);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        Long dishId = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishId);
        dishLambdaQueryWrapper.eq(Dish::getId, dishId);
        this.remove(dishLambdaQueryWrapper);
        dishFlavorService.remove(wrapper);
        this.saveWithFlavor(dishDto);
    }
}
