package bootdemo.demo.redis;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import bootdemo.demo.redis.config.RedisCommonConfig;
import redis.clients.jedis.JedisPool;

@Component
@ConfigurationProperties(prefix = "admin.redis")
public class AdminRedis {

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
        AdminRedis.db = db == null ? 0 : db;
    }

    public void setHost(String host) {
        AdminRedis.host = host;
    }

    public void setPort(String port) {
        AdminRedis.port = port;
    }

    public static synchronized JedisPool getJedisPool() {
        if (jedisPool == null) {
            URI redisUri = URI.create("redis://" + host + ":" + port);
            jedisPool = new JedisPool(RedisCommonConfig.getJedisPoolConfig(), redisUri,
                    RedisCommonConfig.getConnectionTimeout(), RedisCommonConfig.getSocketTimeout());
        }
        return jedisPool;
    }
}
