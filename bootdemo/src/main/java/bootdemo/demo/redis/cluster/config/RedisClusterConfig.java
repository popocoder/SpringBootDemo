package bootdemo.demo.redis.cluster.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis.cluster")
public class RedisClusterConfig {

    private static GenericObjectPoolConfig gopc;

    private static int minIdle;

    private static int maxIdle;

    private static int maxTotal;

    private static int maxWaitMillis;

    private static boolean testOnBorrow;

    private static int connectionTimeout;

    private static int socketTimeout;

    private static String hostAndPort;

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
        RedisClusterConfig.connectionTimeout = connectionTimeout;
    }

    public static int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        RedisClusterConfig.socketTimeout = socketTimeout;
    }

    public void setMinIdle(int minIdle) {
        RedisClusterConfig.minIdle = minIdle;
    }

    public void setMaxIdle(int maxIdle) {
        RedisClusterConfig.maxIdle = maxIdle;
    }

    public void setMaxTotal(int maxTotal) {
        RedisClusterConfig.maxTotal = maxTotal;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        RedisClusterConfig.maxWaitMillis = maxWaitMillis;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        RedisClusterConfig.testOnBorrow = testOnBorrow;
    }

    public static Map<Integer, String> getHostAndPort() {
        Map<Integer, String> map = new HashMap<>();
        String[] hostAndPorts = hostAndPort.split(",");
        for (String hostAndPort : hostAndPorts) {
            String[] str = hostAndPort.split(":");
            map.put(Integer.parseInt(str[1]), str[0]);
        }
        return map;
    }

    public void setHostAndPort(String hostAndPort) {
        RedisClusterConfig.hostAndPort = hostAndPort;
    }

}
