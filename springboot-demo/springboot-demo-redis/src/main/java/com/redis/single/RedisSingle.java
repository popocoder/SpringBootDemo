package com.redis.single;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.redis.single.config.RedisSingleCommonConfig;
import com.redis.single.config.RedisSingleConfig;
import com.springboot.demo.utils.BeanTool;

import redis.clients.jedis.JedisPool;

public class RedisSingle {

    /**
     * @Field @jedisPool : 存储默认配置Jedis连接池
     */
    private static JedisPool jedisPool;

    /**
     * @Field @redisSingleMap : 存储个性化配置Jedis连接池
     */
    private static ConcurrentHashMap<RedisSingleConfig, JedisPool> redisSingleMap = new ConcurrentHashMap<>();

    private static Integer db;

    /**
     * @Description 采用 redis.single 配置,获取默认JedisPool
     * @return
     */
    public static synchronized JedisPool getDefaultJedisPool() {
        if (jedisPool == null) {
            RedisSingleConfig singleConfig = BeanTool.getBean(RedisSingleConfig.class);
            db = singleConfig.getDb();
            URI redisUri = URI.create("redis://" + singleConfig.getHost() + ":" + singleConfig.getPort());
            jedisPool = new JedisPool(RedisSingleCommonConfig.getJedisPoolConfig(), redisUri,
                    RedisSingleCommonConfig.getConnectionTimeout(), RedisSingleCommonConfig.getSocketTimeout());
        }
        return jedisPool;
    }

    /**
     * @Description 获取个性化配置Jedis连接池
     * @param singleConfig RedisSingleConfig(实例化一个redis单机配置传入就行)
     * @return
     * @throws Exception
     */
    public static synchronized JedisPool getJedisPool(RedisSingleConfig singleConfig) throws Exception {
        JedisPool pool = redisSingleMap.get(singleConfig);
        if (pool == null) {
            URI redisUri = URI.create("redis://" + singleConfig.getHost() + ":" + singleConfig.getPort());
            pool = new JedisPool(RedisSingleCommonConfig.getJedisPoolConfig(), redisUri,
                    RedisSingleCommonConfig.getConnectionTimeout(), RedisSingleCommonConfig.getSocketTimeout());
            redisSingleMap.put(singleConfig, pool);
        }
        return pool;
    }

    public static Integer getDefaultDb() {
        return db;
    }
}
