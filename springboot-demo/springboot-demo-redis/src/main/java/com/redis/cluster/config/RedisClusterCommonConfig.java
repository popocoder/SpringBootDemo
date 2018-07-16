package com.redis.cluster.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis.cluster")
public class RedisClusterCommonConfig {

    private static GenericObjectPoolConfig gopc;

    private static int minIdle;

    private static int maxIdle;

    private static int maxTotal;

    private static int maxWaitMillis;

    private static boolean testOnBorrow;

    private static int connectionTimeout;

    private static int socketTimeout;

    public static synchronized GenericObjectPoolConfig getGenericObjectPoolConfig() {
        if (gopc == null) {
            gopc = new GenericObjectPoolConfig();
            gopc.setMinIdle(minIdle);
            gopc.setMaxIdle(maxIdle);
            gopc.setMaxTotal(maxTotal);
            gopc.setMaxWaitMillis(maxWaitMillis);
            gopc.setTestOnBorrow(testOnBorrow);
        }
        return gopc;
    }

    public static int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        RedisClusterCommonConfig.connectionTimeout = connectionTimeout;
    }

    public static int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        RedisClusterCommonConfig.socketTimeout = socketTimeout;
    }

    public void setMinIdle(int minIdle) {
        RedisClusterCommonConfig.minIdle = minIdle;
    }

    public void setMaxIdle(int maxIdle) {
        RedisClusterCommonConfig.maxIdle = maxIdle;
    }

    public void setMaxTotal(int maxTotal) {
        RedisClusterCommonConfig.maxTotal = maxTotal;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        RedisClusterCommonConfig.maxWaitMillis = maxWaitMillis;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        RedisClusterCommonConfig.testOnBorrow = testOnBorrow;
    }

}
