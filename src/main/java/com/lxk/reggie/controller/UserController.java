package com.lxk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lxk.reggie.common.R;
import com.lxk.reggie.entity.User;
import com.lxk.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();
        return null;
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        String phone = map.get("phone").toString();
        if (phone != null) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user = userService.getOne(wrapper);
            //如果未注册
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }

}
