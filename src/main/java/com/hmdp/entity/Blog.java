package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户id
     */
    private Long shopId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户图标
     */
    @TableField(exist = false)
    private String icon;
    /**
     * 用户姓名
     */
    @TableField(exist = false)
    private String name;
    /**
     * 是否点赞过了
     */
    @TableField(exist = false)
    private Boolean isLike;

    /**
     * 标题
     */
    private String title;

    /**
     * 探店的照片，最多9张，多张以","隔开
     */
    private String images;

    /**
     * 探店的文字描述
     */
    private String content;

    /**
     * 点赞数量
     */
    private Integer liked;

    /**
     * 评论数量
     */
    private Integer comments;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    public Blog(BlogContent Content,BlogHomePage homePage){
//        private Long tbId;
//        private String images;
//        private Integer liked;
//        private Integer comments;
//        private LocalDateTime createTime;
//        private LocalDateTime updateTime;
//        private Long shopId;
//        private Long userId;
        //Blog blog = new Blog();
        id = homePage.getTbId();
        images = homePage.getImages();
        liked = homePage.getLiked();
        comments=homePage.getComments();
        createTime=homePage.getCreateTime();
        updateTime=homePage.getUpdateTime();
        shopId=homePage.getShopId();
        userId=homePage.getUserId();
        content = Content.getContent();
    }
    public void addContent(BlogContent Content){
        content = Content.getContent();
    }
    public void addHomePage(BlogHomePage homePage){
        id = homePage.getTbId();
        images = homePage.getImages();
        liked = homePage.getLiked();
        comments=homePage.getComments();
        createTime=homePage.getCreateTime();
        updateTime=homePage.getUpdateTime();
        shopId=homePage.getShopId();
        userId=homePage.getUserId();
    }

    public Blog() {

    }
}
