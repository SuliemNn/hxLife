package com.hmdp.utils.bloomFilter;

import java.util.ArrayList;

/**
 * 自适应布隆过滤器，当满了的时候实现自动扩容
 */
public class AdaptBloomFilter implements BloomFilter{
    private int n;
    private double p;
    public AdaptBloomFilter(int n, double p){
        this.n=n;
        this.p=p;
        this.bloomList.add(BloomFilterFactory.createBloomFilter(n,p));
    }

    // 布隆过滤器列表
    private ArrayList<BloomFilter> bloomList = new ArrayList<>();;

    //此布隆过滤器实现
    private int count=0;
    @Override
    public void add(Object value) {
        // 向最后的一个布隆过滤器添加
        bloomList.get(bloomList.size()-1).add(value);
        if (count++>n){
            expand();
        }
    }

    @Override
    public boolean contains(Object value) {
        // 遍历每个布隆过滤器判断是否存在
        for (BloomFilter i : bloomList){
            if (bloomList.contains(value)){
                return true;
            }
        }
        return false;
    }

    private void expand(){
        this.bloomList.add(BloomFilterFactory.createBloomFilter((int) (n*0.5),p));
    }
}
