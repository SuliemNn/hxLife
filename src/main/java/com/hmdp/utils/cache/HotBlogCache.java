package com.hmdp.utils.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hmdp.entity.Blog;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class HotBlogCache {
    private Cache<Long, Blog> blogCache;
    public HotBlogCache(){
        blogCache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).maximumSize(10).build();
    }
    public void put(Long id,Blog blog){
        blogCache.put(id,blog);
    }
    public Blog get(Long id){
        return blogCache.get(id,key->{
            return null;
        });
    }
    public void clear(){
        blogCache.cleanUp();
    }

    @Override
    public String toString() {
        return blogCache.toString();
    }
}
