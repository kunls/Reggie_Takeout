package com.lxk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxk.reggie.entity.Orders;
import org.springframework.stereotype.Service;

@Service
public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
