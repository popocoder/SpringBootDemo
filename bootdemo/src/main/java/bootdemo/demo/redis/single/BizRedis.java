package bootdemo.demo.redis.single;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import bootdemo.demo.redis.single.config.RedisSingleConfig;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName BizRedis
 * @Description Biz平台使用redis
 * @author LBQ
 * @Date 2018年5月17日 下午5:14:06
 * @version 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "biz.redis")
public class BizRedis {

    private static JedisPool jedisPool;

    private static String host;

    private static String port;

    private static Integer db;

    /**
     * @Description 默认初始化0库
     * @return
     */
    public static int getDb() {
        return db == null ? 0 : db;
    }

    public void setDb(Integer db) {
        BizRedis.db = db;
    }

    public void setHost(String host) {
        BizRedis.host = host;
    }

    public void setPort(String port) {
        BizRedis.port = port;
    }

    public static synchronized JedisPool getJedisPool() {
        if (jedisPool == null) {
            URI redisUri = URI.create("redis://" + host + ":" + port);
            jedisPool = new JedisPool(RedisSingleConfig.getJedisPoolConfig(), redisUri,
                    RedisSingleConfig.getConnectionTimeout(), RedisSingleConfig.getSocketTimeout());
        }
        return jedisPool;
    }

}
