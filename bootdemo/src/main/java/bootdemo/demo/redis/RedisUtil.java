package bootdemo.demo.redis;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisUtil extends Jedis{
    
    private static Log logger = LogFactory.getLog(RedisUtil.class);

    private static JedisPool jedisPool;;

    private static Jedis jedis;
    
    private static <T> T execute(RedisFunction<T, Jedis> function) {
        Jedis jedis = null;
        try {
            jedis = getJedis();// 获取jedis对象
            jedis.select(AdminRedis.getDb()); // 设置index
            return function.callBack(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 检查连接是否有效，有效则放回连接池，无效则关闭。
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    
    private static Jedis getJedis() {
        try {
            jedisPool = AdminRedis.getJedisPool();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        jedis = jedisPool.getResource();
        return jedis;
    }
    
}
