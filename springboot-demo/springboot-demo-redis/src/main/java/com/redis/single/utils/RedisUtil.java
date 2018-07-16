package com.redis.single.utils;

import com.redis.single.RedisSingle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName RedisUtil
 * @Description 默认JedisPool工具类
 * @author LBQ
 * @Date 2018年6月12日 上午9:06:04
 * @version 1.0.0
 */
public class RedisUtil {

    private static JedisPool jedisPool = RedisSingle.getDefaultJedisPool();

    /**
     * @Description 执行redis get 方法后，自动释放资源
     * @param key
     * @return
     * @throws Exception
     */
    public static String get(String key) throws Exception {
        Jedis jedis = null;
        String rs = null;

        try {
            jedis = jedisPool.getResource();
            rs = jedis.get(key);
        } catch (Exception e) {
            throw new Exception("Redis get 操作异常!");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return rs;
    }

    /**
     * @Description 执行redis get 方法后，自动释放资源
     * @param key
     * @return
     * @throws Exception
     */
    public static String set(String key, String value) throws Exception {
        Jedis jedis = null;
        String rs = null;

        try {
            jedis = jedisPool.getResource();
            rs = jedis.set(key, value);
        } catch (Exception e) {
            throw new Exception("Redis get 操作异常!");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return rs;
    }
}
