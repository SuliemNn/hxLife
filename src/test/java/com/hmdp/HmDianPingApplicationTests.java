package com.hmdp;

import com.hmdp.utils.MyBloomFilter;
import com.hmdp.utils.MyBloomFilterSingleton;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Random;

@SpringBootTest
public
class HmDianPingApplicationTests {

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
    }

    private static String generateRandomString() {
        // 生成一个随机字符串
        int length = 20;
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
