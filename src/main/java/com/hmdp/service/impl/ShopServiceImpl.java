package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.junit.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Random;
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

    @Test
    public void mytest00231(){
        // Redis服务器地址和端口号
        String host = "localhost";
        int port = 6379;

        // 创建Jedis实例
        try (Jedis jedis = new Jedis("127.0.0.1", 6379)) {
            // 如果Redis服务器需要密码认证
            // 设置Key
            String key = "exampleKey";
            String value = "exampleValue";
            jedis.set(key, value);

            System.out.println("Key set successfully!");

            // 获取Key的值
            String retrievedValue = jedis.get(key);
            System.out.println("Retrieved value for key " + key + ": " + retrievedValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result queryById(Long id) {

        String key = "cache:shop:" + id;
        // 1. 从redis查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        // 2. 判断是否存在
        if (StrUtil.isNotBlank(shopJson)){
            // 3.存在，直接返回
            Shop shop = JSONUtil.toBean(shopJson,Shop.class);
            return Result.ok(shop);
        }
        // 4.不存在，根据id查询数据库
        Shop shop = getById(id);
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
