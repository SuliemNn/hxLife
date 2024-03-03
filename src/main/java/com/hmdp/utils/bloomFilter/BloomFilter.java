package com.hmdp.utils.bloomFilter;

//布隆过滤器的接口，定义了布隆过滤器该有哪些功能
public interface BloomFilter {
    //添加元素
    public void add(Object value);

    //判断某元素是否存在
    public boolean contains(Object value);
}
