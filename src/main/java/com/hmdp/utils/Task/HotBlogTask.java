package com.hmdp.utils.Task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.entity.Blog;
import com.hmdp.entity.BlogRecord;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.service.IBlogRecordService;
import com.hmdp.service.IBlogService;
import com.hmdp.utils.TimeStamps;
import com.hmdp.utils.cache.HotBlogCache;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.*;

@Component
@Slf4j
public class HotBlogTask {
    @Resource
    private BlogMapper blogMapper;
    @Resource
    private HotBlogCache hotBlogCache;
    @Resource
    private IBlogRecordService blogRecordService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //TODO 对于大数据日志处理，还有优化空间
    //每到整点就执行一次任务
    @Scheduled(cron = "0 0 * * * *")
    public void refreshHotBlogCache(){
        //1.读取数据库中的数据，找出此时访问次数最多的10条帖子

        //1.1获得数据库中满足条件的全部数据数据
        QueryWrapper<BlogRecord> queryWrapper = new QueryWrapper<>();
        long curr = TimeStamps.now();
        queryWrapper.between("timestamp",curr-86400000,curr);
        List<BlogRecord> ls = blogRecordService.list(queryWrapper);

        //1.2遍历数据，找到出现频次最高的blog_id
        Map<Long,Integer> map = new HashMap<>();
        ls.forEach(blogRecord -> {
            Integer id = map.get(blogRecord.getBlogId());
            if (id==null){
                map.put(blogRecord.getBlogId(), 1);
            }else {
                map.put(blogRecord.getBlogId(), id+1);
            }
        });

        PriorityQueue<Count> queue = new PriorityQueue<>(new Comparator<Count>() {
            @Override
            public int compare(Count o1, Count o2) {
                return (o1.count-o2.count);
            }
        });
        Set<Long> ketSet = map.keySet();
        for (Long i:ketSet){
            Integer count = map.get(i);
            if (queue.size()<10){
                queue.add(new Count(i,count));
            }else {
                Count peek = queue.peek();;
                if (peek.count<count){
                    queue.poll();
                    queue.offer(new Count(i,count));
                }
            }
        }

        //2.将这十条帖子的排行数据缓存到redis中
        for (Count i : queue){
            stringRedisTemplate.opsForZSet().add("hotBlog",i.id.toString(),i.count);
        }

        //3.将这十条帖子缓存到本地caffeine中
        hotBlogCache.clear();
        for (Count i : queue){
            Blog blog = blogMapper.selectById(i.id);
            hotBlogCache.put(i.id,blog);
        }
        log.info("存入本地Caffeine中!!!"+hotBlogCache.toString());
    }


}
class Count{
    public Long id;
    public int count;
    public Count(long id,int count){
        this.id=id;
        this.count=count;
    }

}
