package com.npci.integration.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

        @Autowired
        private RedisTemplate<String, Object> redisTemplate;

        public <T> T get(String key, Class<T> entityClass) {
            try {
                Object object = redisTemplate.opsForValue().get(key);
                return entityClass.cast(object);  // Direct casting instead of JSON deserialization
            } catch (Exception e) {
                System.err.println("Exception in get: " + e);
                return null;
            }
        }

        public void set(String key, Object value, Long ttl) {
            try {
                redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
            } catch (Exception e) {
                System.err.println("Exception in set: " + e);
            }
        }

        public void delete(String key) {
            try {
                redisTemplate.delete(key);
            } catch (Exception e) {
                System.err.println("Exception in delete: " + e);
            }
        }


}
