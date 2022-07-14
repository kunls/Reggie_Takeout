package com.lxk.reggie.dto;

import com.lxk.reggie.entity.Setmeal;
import com.lxk.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
