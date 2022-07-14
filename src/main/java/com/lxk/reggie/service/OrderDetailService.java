package com.lxk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxk.reggie.entity.OrderDetail;
import com.lxk.reggie.entity.Orders;
import com.lxk.reggie.entity.ShoppingCart;
import org.springframework.stereotype.Service;

@Service
public interface OrderDetailService extends IService<OrderDetail> {
    void save(ShoppingCart shoppingCart, Orders orders);
}
