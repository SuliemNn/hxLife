package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.entity.BlogHomePage;

public interface IBlogHomePage extends IService<BlogHomePage> {
    public BlogHomePage getByTbId(Long tbId);
}
