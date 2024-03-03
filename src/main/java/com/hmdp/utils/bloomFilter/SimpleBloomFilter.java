package com.hmdp.utils.bloomFilter;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


/**
 * 接受预期size和hash函数个数，并以此创建布隆过滤器
 */
public class SimpleBloomFilter implements BloomFilter{
    /**
     * 位数组的大小
     */
    private int size;

    /**
     * SimpleHash Function 的个数
     */
    private int k;

    /**
     * 通过这个数组可以创建 多 个不同的哈希函数
     */
    private List<Integer> SEEDS ;

    /**
     * 位数组。数组中的元素只能是 0 或者 1
     */
    private BitSet bits ;

    /**
     * 存放包含 hash 函数的类的数组
     */
    private ArrayList<SimpleHash> func = new ArrayList<>();

    /**
     * 初始化多个包含 hash 函数的类的数组，每个类中的 hash 函数都不一样
     */
    public SimpleBloomFilter(int size, int k) {
        this.size=size;
        this.k=k;
        this.bits = new BitSet(size);
        // 根据k的值获得k个种子数组
        SEEDS = PrimeNumbers.getKDistinctPrimes(k);
        // 根据k个种子创建k个哈希函数并添加到数组中
        for(int i : SEEDS){
            func.add(new SimpleHash(size,i));
        }
    }

    /**
     * 添加元素到位数组
     */
    public void add(Object value) {
        for (SimpleHash f : func) {
            bits.set(f.hash(value), true);
        }
    }

    /**
     * 判断指定元素是否存在于位数组
     */
    public boolean contains(Object value) {
        boolean ret = true;
        for (SimpleHash f : func) {
            ret = ret && bits.get(f.hash(value));
        }
        return ret;
    }

    /**
     * 静态内部类。用于 hash 操作！
     */
    public static class SimpleHash {

        private int cap;
        private int seed;

        public SimpleHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        /**
         * 计算 hash 值
         */
        public int hash(Object value) {
            int h;
            return (value == null) ? 0 : Math.abs(seed * (cap - 1) & ((h = value.hashCode()) ^ (h >>> 16)));
        }

    }
}
