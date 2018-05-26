package bootdemo.demo.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName RedisCommonConfig
 * @Description redis公共配置文件
 * @author LBQ
 * @Date 2018年5月17日 下午5:13:44
 * @version 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisCommonConfig {

    private static JedisPoolConfig jpc;

    private static int minIdle;

    private static int maxIdle;

    private static int maxTotal;

    private static int maxWaitMillis;

    private static boolean testOnBorrow;

    private static int connectionTimeout;

    private static int socketTimeout;

    public static synchronized JedisPoolConfig getJedisPoolConfig() {
        if (jpc == null) {
            jpc = new JedisPoolConfig();
            jpc.setMinIdle(minIdle);
            jpc.setMaxIdle(maxIdle);
            jpc.setMaxTotal(maxTotal);
            jpc.setMaxWaitMillis(maxWaitMillis);
            jpc.setTestOnBorrow(testOnBorrow);
        }
        return jpc;
    }

    public static int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        RedisCommonConfig.connectionTimeout = connectionTimeout;
    }

    public static int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        RedisCommonConfig.socketTimeout = socketTimeout;
    }

    public void setMinIdle(int minIdle) {
        RedisCommonConfig.minIdle = minIdle;
    }

    public void setMaxIdle(int maxIdle) {
        RedisCommonConfig.maxIdle = maxIdle;
    }

    public void setMaxTotal(int maxTotal) {
        RedisCommonConfig.maxTotal = maxTotal;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        RedisCommonConfig.maxWaitMillis = maxWaitMillis;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        RedisCommonConfig.testOnBorrow = testOnBorrow;
    }
}
