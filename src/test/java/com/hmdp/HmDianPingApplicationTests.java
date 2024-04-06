package com.hmdp;

import com.hmdp.dto.Result;
import com.hmdp.entity.BlogContent;
import com.hmdp.service.IBlogContent;
import com.hmdp.service.IBlogHomePage;
import com.hmdp.service.IBlogService;
import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.MyBloomFilter;
import com.hmdp.utils.MyBloomFilterSingleton;
import com.hmdp.utils.RedisIDWorker;
import com.hmdp.utils.bloomFilter.BloomFilter;
import com.hmdp.utils.bloomFilter.BloomFilterFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class HmDianPingApplicationTests {

    @Resource
    private ShopServiceImpl shopService;

    @Resource
    private RedisIDWorker redisIDWorker;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private ExecutorService es = Executors.newFixedThreadPool(500);

    @Resource
    private IBlogService blogService;

    @Resource
    private IBlogHomePage blogHomePage;
    @Resource
    private IBlogContent blogContent;
    @Test
    void testBlog(){
        int times=5000;
        //单表查询
        long begin = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            Result result = blogService.queryBlogById(5L);
        }
        long end = System.currentTimeMillis();


        //双表顺序查询
        long begin1 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            Result result1 = blogService.queryBlogByIdQuick0(5L);
        }
        long end1 = System.currentTimeMillis();


        //双表线程池查询
        long begin2 = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            Result result2 = blogService.queryBlogByIdQuick1(5L);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("0:"+(end-begin));
        System.out.println("1:"+(end1-begin1));
        System.out.println("2:"+(end2-begin2));

    }
    @Test
    void testIdWorker() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);

        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                long id = redisIDWorker.nextId("order");
                System.out.println("id = " + id);
            }
            latch.countDown();
        };
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            es.submit(task);
        }
        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - begin));
    }

    @Test
    void testInjection() throws InterruptedException {
        System.out.println("seasd");

        // 检查Bean是否成功注入
        checkBean("shopService", shopService);
        checkBean("redisIDWorker", redisIDWorker);
        checkBean("stringRedisTemplate", stringRedisTemplate);
    }

    private void checkBean(String beanName, Object bean) {
        if (bean != null) {
            System.out.println(beanName + " bean successfully injected.");
        } else {
            System.err.println(beanName + " bean injection failed!");
        }
    }





}
