package com.lxk.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxk.reggie.entity.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService extends IService<Category> {
    void removeById(long id);
}
