package com.hmdp.utils;

public class MyBloomFilterSingleton {
    private static MyBloomFilter instance = new MyBloomFilter();
    public static MyBloomFilter getInstance(){
        return instance;
    }
}
