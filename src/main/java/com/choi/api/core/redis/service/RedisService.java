package com.choi.api.core.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void setValue(String key, String value, long timeout){
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
    }

    public String getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void removeValue(String key){
        redisTemplate.delete(key);
    }
}
