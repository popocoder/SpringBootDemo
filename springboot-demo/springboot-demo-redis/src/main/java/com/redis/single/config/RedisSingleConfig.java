package com.redis.single.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName RedisSingleConfig
 * @Description redis单机版 主机地址 端口配置 1.进行默认配置 2.个性化配置
 * @author LBQ
 * @Date 2018年6月12日 下午5:49:55
 * @version 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "redis.single")
public class RedisSingleConfig {

    private String host;

    private Integer port;

    private Integer db;

    public RedisSingleConfig(String host, Integer port, Integer db) {
        this.host = host;
        this.port = port;
        this.db = db;
    }

    public RedisSingleConfig(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public RedisSingleConfig() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getDb() {
        return db == null ? 0 : db;
    }

    public void setDb(Integer db) {
        this.db = db == null ? 0 : db;
    }

    @Override
    public boolean equals(Object obj) {
        RedisSingleConfig config = (RedisSingleConfig) obj;
        return (this.host + ":" + this.port).equals(config.getHost() + ":" + config.getPort());
    }

    @Override
    public int hashCode() {
        return (this.host + ":" + this.port).hashCode();
    }
}
