package com.hmdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.entity.BlogHomePage;
import com.hmdp.mapper.BlogHomePageMapper;
import com.hmdp.service.IBlogHomePage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BlogHomePageService extends ServiceImpl<BlogHomePageMapper, BlogHomePage> implements IBlogHomePage {

    @Resource
    private  BlogHomePageMapper blogHomePageMapper;
    public BlogHomePage getByTbId(Long tbId){
        QueryWrapper<BlogHomePage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tb_id",tbId);
        BlogHomePage blogHomePage = blogHomePageMapper.selectOne(queryWrapper);
        return blogHomePage;
    }
}
