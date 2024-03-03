package com.hmdp.utils.bloomFilter;

/**
 * 布隆过滤器的工厂，用于创建布隆过滤器
 * 给定 预期数据量 n 和误差率 p 计算得到所需布隆过滤器的 size 和哈希函数个数 k
 */
public class BloomFilterFactory {
    public static BloomFilter createBloomFilter(int n, double p){
        int size = getSizeOfBloomFilter(n,p);
        int k =getNumberOfHashFuc(size,n);
        return new YugBloomFilter(size,k);
    }

    /**
     * 根据传入的数据量 n 和 误差率 p返回size
     * @return
     */
    private static int getSizeOfBloomFilter(int n, double p) {
        int size = (int) Math.ceil(-(n * Math.log(p)) / Math.pow(Math.log(2), 2));
        return size;
    }

    /**
     * 根据传入的 size 和 数据量 n 返回所需的哈希函数值
     * @return
     */
    private static int getNumberOfHashFuc(int size, int n) {
        int k = (int) Math.ceil((size / (double) n) * Math.log(2));
        return k;
    }
}
