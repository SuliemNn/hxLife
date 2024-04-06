package com.hmdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.entity.BlogContent;
import com.hmdp.mapper.BlogContentMapper;
import com.hmdp.service.IBlogContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BlogContentService extends ServiceImpl<BlogContentMapper, BlogContent> implements IBlogContent {
    @Resource
    private BlogContentMapper blogContentMapper;
    public BlogContent getByTbId(Long tbId){
        QueryWrapper<BlogContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tb_id",tbId);
        BlogContent blogContent = blogContentMapper.selectOne(queryWrapper);
        return blogContent;
    }

}
