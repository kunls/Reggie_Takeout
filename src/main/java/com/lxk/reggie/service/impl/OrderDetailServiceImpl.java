package com.lxk.reggie.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxk.reggie.entity.OrderDetail;
import com.lxk.reggie.entity.Orders;
import com.lxk.reggie.entity.ShoppingCart;
import com.lxk.reggie.mapper.OrderDetailMapper;
import com.lxk.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    @Override
    public void save(ShoppingCart shoppingCart, Orders orders) {
        Long dishId = shoppingCart.getDishId();
        BigDecimal amount = shoppingCart.getAmount();
        Long setmealId = shoppingCart.getSetmealId();
        String dishFlavor = shoppingCart.getDishFlavor();
        String image = shoppingCart.getImage();
        String name = shoppingCart.getName();
        Integer number = shoppingCart.getNumber();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(Long.valueOf(orders.getNumber()));
        orderDetail.setNumber(number);
        orderDetail.setSetmealId(setmealId);
        orderDetail.setDishFlavor(dishFlavor);
        orderDetail.setDishId(dishId);
        orderDetail.setId(IdWorker.getId());
        orderDetail.setImage(image);
        orderDetail.setName(name);
        orderDetail.setAmount(amount);
        this.save(orderDetail);
    }
}
