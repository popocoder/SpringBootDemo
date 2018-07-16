package com.redis.cluster.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis.cluster")
public class RedisClusterConfig {

    public String hostAndPort;

    public RedisClusterConfig() {
    }

    public RedisClusterConfig(String hostAndPort) {
        this.hostAndPort = hostAndPort;
    }

    public Map<Integer, String> getHostAndPort() {
        Map<Integer, String> map = new HashMap<>();
        String[] hostAndPorts = this.hostAndPort.split(",");
        for (String hostAndPort : hostAndPorts) {
            String[] str = hostAndPort.split(":");
            map.put(Integer.parseInt(str[1]), str[0]);
        }
        return map;
    }

    public void setHostAndPort(String hostAndPort) {
        this.hostAndPort = hostAndPort;
    }

    @Override
    public boolean equals(Object obj) {
        RedisClusterConfig config = (RedisClusterConfig) obj;
        return this.hostAndPort.equals(config.hostAndPort);
    }

    @Override
    public int hashCode() {
        return this.hostAndPort.hashCode();
    }
}
