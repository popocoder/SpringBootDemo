package com.redis.cluster;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.redis.cluster.config.RedisClusterCommonConfig;
import com.redis.cluster.config.RedisClusterConfig;
import com.springboot.demo.utils.BeanTool;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * @ClassName RedisCluster
 * @Description 获取JedisCluster
 * @author LBQ
 * @Date 2018年6月8日 下午9:34:01
 * @version 1.0.0
 */
public class RedisCluster {

    private static JedisCluster jedisCluster;

    /**
     * @Field @redisClusterMap : 存储个性化配置Jedis连接池
     */
    private static ConcurrentHashMap<RedisClusterConfig, JedisCluster> redisClusterMap = new ConcurrentHashMap<>();

    public static synchronized JedisCluster getDefaultJedisCluster() {
        if (jedisCluster == null) {
            Set<HostAndPort> nodes = new HashSet<>();
            for (Entry<Integer, String> entry : BeanTool.getBean(RedisClusterConfig.class).getHostAndPort()
                    .entrySet()) {
                nodes.add(new HostAndPort(entry.getValue(), entry.getKey()));
            }
            jedisCluster = new JedisCluster(nodes, RedisClusterCommonConfig.getConnectionTimeout(),
                    RedisClusterCommonConfig.getSocketTimeout(), RedisClusterCommonConfig.getGenericObjectPoolConfig());
        }
        return jedisCluster;
    }

    public static synchronized JedisCluster getJedisCluster(RedisClusterConfig redisClusterConfig) {
        JedisCluster jedisCluster = redisClusterMap.get(redisClusterConfig);
        if (jedisCluster == null) {
            Set<HostAndPort> nodes = new HashSet<>();
            for (Entry<Integer, String> entry : redisClusterConfig.getHostAndPort().entrySet()) {
                nodes.add(new HostAndPort(entry.getValue(), entry.getKey()));
            }
            jedisCluster = new JedisCluster(nodes, RedisClusterCommonConfig.getConnectionTimeout(),
                    RedisClusterCommonConfig.getSocketTimeout(), RedisClusterCommonConfig.getGenericObjectPoolConfig());
            redisClusterMap.put(redisClusterConfig, jedisCluster);
        }
        return jedisCluster;
    }

}
