package com.redis.single.config;

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
@ConfigurationProperties(prefix = "redis.single")
public class RedisSingleCommonConfig {

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
        RedisSingleCommonConfig.connectionTimeout = connectionTimeout;
    }

    public static int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        RedisSingleCommonConfig.socketTimeout = socketTimeout;
    }

    public void setMinIdle(int minIdle) {
        RedisSingleCommonConfig.minIdle = minIdle;
    }

    public void setMaxIdle(int maxIdle) {
        RedisSingleCommonConfig.maxIdle = maxIdle;
    }

    public void setMaxTotal(int maxTotal) {
        RedisSingleCommonConfig.maxTotal = maxTotal;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        RedisSingleCommonConfig.maxWaitMillis = maxWaitMillis;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        RedisSingleCommonConfig.testOnBorrow = testOnBorrow;
    }
}
