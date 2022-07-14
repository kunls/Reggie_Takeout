package com.lxk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxk.reggie.common.R;
import com.lxk.reggie.entity.Employee;
import com.lxk.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //查询用户
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(wrapper);
        //用户不存在
        if (emp == null) {
            return R.error("登录失败");
        }
        //密码比对
        if (!password.equals(emp.getPassword())) {
            return R.error("登录失败");
        }
        //查询是否被禁用
        if (emp.getStatus() == 0) {
            return R.error("该员工被禁用");
        }
        //返回成功结果,存入session
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        String password = "123456";
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(password);
        employeeService.save(employee);
        return R.success("添加员工成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //添加分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //添加条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Employee::getName, name);
        wrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable long id) {
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
