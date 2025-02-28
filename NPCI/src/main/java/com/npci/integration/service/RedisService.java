package com.npci.integration.service;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisService {

        @Autowired
        private RedisTemplate<String, Object> redisTemplate;

        public <T> T get(String key, Class<T> entityClass) {
            try {
                log.info("Getting redis object");
                Object object = redisTemplate.opsForValue().get(key);
                return entityClass.cast(object);  // Direct casting instead of JSON deserialization
            } catch (Exception e) {
                log.info("Failed to get Redis object");
                return null;
            }
        }

        public void set(String key, Object value, Long ttl) {
            try {
                log.info("Setting redis object");
                redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.info("Failed to set Redis object");
            }
        }

        public void delete(String key) {
            try {
                log.info("Deleting redis object");
                redisTemplate.delete(key);
            } catch (Exception e) {
                log.info("Failed to delete Redis object");
            }
        }


}
