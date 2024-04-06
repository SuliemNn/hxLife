package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.entity.BlogContent;

public interface IBlogContent extends IService<BlogContent> {
    public BlogContent getByTbId(Long tbId);
}
