package com.lxk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxk.reggie.common.R;
import com.lxk.reggie.dto.OrdersDto;
import com.lxk.reggie.entity.*;
import com.lxk.reggie.service.*;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    OrdersService ordersService;
    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    AddressBookService addressBookService;
    @Autowired
    UserService userService;
    @Autowired
    OrderDetailService orderDetailService;
    @PostMapping("/submit")
    @Transactional
    public R<String> submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("提交订单成功");
    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> userPage(HttpSession session, int page, int pageSize) {
        Page<Orders> ordersPage = new Page<Orders>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        LambdaQueryWrapper<Orders> ordersWrapper = new LambdaQueryWrapper<>();
        ordersWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(ordersPage,ordersWrapper);
        BeanUtils.copyProperties(ordersPage, ordersDtoPage, "records");
        List<Orders> records = ordersPage.getRecords();
        List<OrdersDto> ordersDtoList = new ArrayList<>();
        for (Orders record : records) {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(record, ordersDto);
            /*Long orderId = record.getId();
            Orders orders = ordersService.getById(orderId);
            */
            String number = record.getNumber();
            LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getNumber, number);
            List<OrderDetail> detailList = orderDetailService.list(wrapper);
            ordersDto.setOrderDetails(detailList);
            int num=0;
            for (OrderDetail detail : detailList) {
                num += detail.getNumber();
            }
            ordersDto.setSumNum(num);
            ordersDtoList.add(ordersDto);
        }
        ordersDtoPage.setRecords(ordersDtoList);
        return R.success(ordersDtoPage);
    }
}
