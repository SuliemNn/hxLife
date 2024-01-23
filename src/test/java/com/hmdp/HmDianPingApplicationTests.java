package com.hmdp;

import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.MyBloomFilter;
import com.hmdp.utils.MyBloomFilterSingleton;
import com.hmdp.utils.RedisIDWorker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class HmDianPingApplicationTests {

    @Resource
    private ShopServiceImpl shopService;

    @Resource
    private RedisIDWorker redisIDWorker;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private ExecutorService es = Executors.newFixedThreadPool(500);


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

    //测试布隆过滤器
    @Test
    public void test01(){
        ArrayList<String> arrayList=new ArrayList<>();

        MyBloomFilter instance = MyBloomFilterSingleton.getInstance();



        //布隆过滤器初始化
        int initLen = 50000;
        for (int i= 0;i<initLen;i++){
            String s = generateRandomString();
            instance.add(s);
        }

        testFalsePositiveRate(instance);
        System.out.println("Success");
    }

    private static void testFalsePositiveRate(MyBloomFilter filter) {

        // 随机生成一些字符串，并测试是否误判为存在
        int falsePositiveCount = 0;
        int totalTests = 100000;

        Random random = new Random();

        for (int i = 0; i < totalTests; i++) {
            String randomString = generateRandomString();
            boolean contains = filter.contains(randomString);

            // 如果误判为存在，增加计数
            if (contains) {
                falsePositiveCount++;
            }
        }

        double falsePositiveRate = (double) falsePositiveCount / totalTests;
        System.out.println("False Positive Rate: " + falsePositiveRate);
        System.out.println(falsePositiveCount);
    }

    private static String generateRandomString() {
        // 生成一个随机字符串
        int length = 25;
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

}
