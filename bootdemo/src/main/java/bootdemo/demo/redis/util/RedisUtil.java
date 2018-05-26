package bootdemo.demo.redis.util;

import bootdemo.demo.redis.BizRedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisUtil {

    private static JedisPool jedisPool = BizRedis.getJedisPool();

    public static String get(String key) throws Exception {
        Jedis jedis = null;
        String value = null;
        
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            throw new Exception("Redis get 操作异常!");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return value;
    }
}
