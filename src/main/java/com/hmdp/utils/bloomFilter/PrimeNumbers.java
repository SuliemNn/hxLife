package com.hmdp.utils.bloomFilter;

import java.util.ArrayList;
import java.util.List;

public class PrimeNumbers {

    public static List<Integer> getKDistinctPrimes(int k) {
        List<Integer> primes = new ArrayList<>();
        int num = 2;

        while (primes.size() < k) {
            if (isPrime(num) && isWellDistributed(num, primes)) {
                primes.add(num);
            }
            num++;
        }

        return primes;
    }

    private static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        if (num <= 3) {
            return true;
        }
        if (num % 2 == 0 || num % 3 == 0) {
            return false;
        }
        int sqrtNum = (int) Math.sqrt(num);
        for (int i = 5; i <= sqrtNum; i += 6) {
            if (num % i == 0 || num % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean isWellDistributed(int num, List<Integer> primes) {
        for (int prime : primes) {
            if (Math.abs(prime - num) < 20) { // 设置一个距离阈值，确保质数之间距离较远
                return false;
            }
        }
        return true;
    }
}