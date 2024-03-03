package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.MyBloomFilter;
import com.hmdp.utils.MyBloomFilterSingleton;
import com.hmdp.utils.bloomFilter.BloomFilter;
import com.hmdp.utils.bloomFilter.BloomFilterFactory;
import io.lettuce.core.dynamic.annotation.Param;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.RANDOM_EXPIRE_TIME;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {




    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Resource
    private ShopMapper shopMapper;

    BloomFilter myBloomFilter = BloomFilterFactory.createAdaptBloomFilter(1000,0.001);

    /**
     * 初始化布隆过滤器，将数据库中的数据初始化进入布隆过滤器中
     */
    @PostConstruct
    public void init(){
        List<Shop> shops = shopMapper.selectList(null);

        for (Shop shop : shops){
            myBloomFilter.add(shop.getId());
        }
    }



    @Override
    public Result queryById(Long id) {
        //先从布隆过滤中判断是否有这个数据
        if(!myBloomFilter.contains(id)){
            return Result.fail("不包含这个id");
        }


        String key = "cache:shop:" + id;
        // 1. 从redis查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        // 2. 判断是否存在
        if (StrUtil.isNotBlank(shopJson)){
            // 3.存在，直接返回
            Shop shop = JSONUtil.toBean(shopJson,Shop.class);
            myBloomFilter.add(shop.getId());
            return Result.ok(shop);
        }
        // 4.不存在，根据id查询数据库
        Shop shop = getById(id);
        myBloomFilter.add(shop.getId());
        // 5.不存在，返回错误
        if (shop == null){
            return  Result.fail("商铺不存在！");
        }
        // 6.存在，写入redis,并设置过期时间
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(shop));
        //设置30+1~5 mins的过期时间，以解决缓存雪崩问题
        stringRedisTemplate.expire(key,30+RANDOM_EXPIRE_TIME, TimeUnit.MINUTES);
        return Result.ok(shop);
    }
}
