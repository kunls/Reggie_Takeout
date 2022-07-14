package com.lxk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxk.reggie.dto.SetmealDto;
import com.lxk.reggie.entity.Setmeal;
import org.springframework.stereotype.Service;
@Service
public interface SetmealService extends IService<Setmeal> {
    SetmealDto getSetmealWithDishes(long id);

    void updateSetmealWithDishes(SetmealDto setmealDto);
}
