package com.lxk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxk.reggie.common.R;
import com.lxk.reggie.entity.ShoppingCart;
import com.lxk.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(HttpSession session, @RequestBody ShoppingCart shoppingCart) {
        long userId = (long) session.getAttribute("user");
        shoppingCart.setUserId(userId);
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        if (dishId != null) {
            wrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(wrapper);
        if (cartServiceOne != null) {
            cartServiceOne.setNumber(cartServiceOne.getNumber() + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            cartServiceOne = shoppingCart;
            shoppingCartService.save(cartServiceOne);
        }

        return R.success(cartServiceOne);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(HttpSession session, @RequestBody ShoppingCart shoppingCart) {
        Long userId = (Long) session.getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId()).eq(ShoppingCart::getUserId, userId);
        ShoppingCart one = shoppingCartService.getOne(wrapper);
        one.setNumber(one.getNumber() - 1);
        shoppingCartService.updateById(one);
        return R.success(one);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpSession session) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, session.getAttribute("user"));
        List<ShoppingCart> list = shoppingCartService.list(wrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(HttpSession session) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, session.getAttribute("user"));
        shoppingCartService.remove(wrapper);
        return R.success("清空购物车成功");
    }
}
