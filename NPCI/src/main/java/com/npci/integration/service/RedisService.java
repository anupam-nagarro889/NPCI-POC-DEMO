package com.npci.integration.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class RedisService {
	
	@Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(Long key, Class<T> entityClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key.toString());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), entityClass);
        } catch (Exception e) {
            System.out.println("Exception " + e);
            return null;
        }
    }

    public void set(Long key, Object o, Long ttl) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key.toString(), jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
        	System.out.println("Exception " + e);
        }
    }

}
