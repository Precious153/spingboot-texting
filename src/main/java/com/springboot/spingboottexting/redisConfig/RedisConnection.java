package com.springboot.spingboottexting.redisConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection {
    private static JedisPool jedisPool;

    public static Jedis getConnection(RedisConfig config) {
        if (jedisPool == null) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            jedisPool = new JedisPool(poolConfig, config.getHost(), config.getPort());
        }
        return jedisPool.getResource();
    }
}
