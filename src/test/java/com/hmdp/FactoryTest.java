package com.hmdp;

import com.hmdp.utils.bloomFilter.BloomFilter;
import com.hmdp.utils.bloomFilter.BloomFilterFactory;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FactoryTest {
    @Test
    public void testBloomFilerFactory() {
        int dataSize = 1000;
        BloomFilter bloomFilter = BloomFilterFactory.createBloomFilter(dataSize, 0.001);

        // 添加一千个随机字符串
        Set<String> dataSet = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < dataSize; i++) {
            String randomString = generateRandomString(10, random);
            dataSet.add(randomString);
            bloomFilter.add(randomString);
        }

        // 测试误判率
        int falsePositives = 0;
        int testSize = 10000;
        for (int i = 0; i < testSize; i++) {
            String testString = generateRandomString(10, random);
            if (!dataSet.contains(testString) && bloomFilter.contains(testString)) {
                falsePositives++;
            }
        }

        double falsePositiveRate = falsePositives / (double) testSize;
        System.out.println("False positive rate: " + falsePositiveRate);

        // 断言误判率是否符合预期
        assertTrue(falsePositiveRate <= 0.001);
    }

    private String generateRandomString(int length, Random random) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
