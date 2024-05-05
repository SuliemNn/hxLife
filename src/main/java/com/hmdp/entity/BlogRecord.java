package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_blog_record")
public class BlogRecord {
    private int id;
    private long userId;
    private long blogId;
    private long timestamp;
}
