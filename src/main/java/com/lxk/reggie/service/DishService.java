package com.lxk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxk.reggie.dto.DishDto;
import com.lxk.reggie.entity.Dish;
import org.springframework.stereotype.Service;

@Service
public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(long id);

    void updateWithFlavor(DishDto dishDto);
}
