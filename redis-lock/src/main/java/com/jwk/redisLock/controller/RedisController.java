package com.jwk.redisLock.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @program: parent
 * @author: jwk
 * @create: {MONTH}-{HOUR}:2021/4/2--下午9:55
 **/
@RestController
public class RedisController {

    final String LOCK_KEY="justLock";

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    Redisson redisson;

    @GetMapping("/setRedis")
    public String setRedis(@RequestParam("value") String value,@RequestParam("key") String key){
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(key, value, 60L, TimeUnit.SECONDS);
        return aBoolean.toString();
    }

    @GetMapping("/getRedis")
    public String getRedis(@RequestParam("key") String key){
        Object o = stringRedisTemplate.opsForValue().get(key);
        return o.toString();
    }

    // 分布式锁
    @GetMapping("/redisLock")
    public String redisLock(@RequestParam("key") String key){

//          redisson分布式锁
//        RLock lock = redisson.getLock(LOCK_KEY);
        Object o2 = stringRedisTemplate.opsForValue().get(LOCK_KEY);
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(LOCK_KEY, value, 3L, TimeUnit.SECONDS);
        if (!aBoolean) {
            return "获取锁失败";
        }
        try {

//          redisson分布式锁加锁
//            lock.lock();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Object o = stringRedisTemplate.opsForValue().get(key);
            Object o1 = stringRedisTemplate.opsForValue().get(LOCK_KEY);
            return o.toString()+"------"+o1+"》》》》》》》》"+o2;
        } finally {
            stringRedisTemplate.watch(LOCK_KEY);
            if (value.equalsIgnoreCase(stringRedisTemplate.opsForValue().get(LOCK_KEY))) {
                stringRedisTemplate.setEnableTransactionSupport(true);
                stringRedisTemplate.multi();
                stringRedisTemplate.delete(LOCK_KEY);
                stringRedisTemplate.exec();
            }
            stringRedisTemplate.unwatch();
//            redisson分布式锁解锁
//            if (lock.isLocked()&&lock.isHeldByCurrentThread()){
//                lock.unlock();
//            }
        }
    }
}
