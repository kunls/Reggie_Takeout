package com.lxk.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxk.reggie.dto.SetmealDto;
import com.lxk.reggie.entity.Setmeal;
import com.lxk.reggie.entity.SetmealDish;
import com.lxk.reggie.mapper.SetmealMapper;
import com.lxk.reggie.service.CategoryService;
import com.lxk.reggie.service.SetmealDishService;
import com.lxk.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>  implements SetmealService {
    @Autowired
    SetmealDishService setmealDishService;

    @Override
    public SetmealDto getSetmealWithDishes(long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(wrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    public void updateSetmealWithDishes(SetmealDto setmealDto) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getId, setmealDto.getId());
        this.update(setmealDto, wrapper);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(wrapper1);
        setmealDishService.saveBatch(setmealDishes);
    }
}
