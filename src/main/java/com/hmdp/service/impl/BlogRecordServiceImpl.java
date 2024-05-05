package com.hmdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hmdp.entity.BlogRecord;
import com.hmdp.mapper.BlogRecordMapper;
import com.hmdp.service.IBlogRecordService;
import org.springframework.stereotype.Service;

@Service
public class BlogRecordServiceImpl extends ServiceImpl<BlogRecordMapper, BlogRecord> implements IBlogRecordService {
}
