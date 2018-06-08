package bootdemo.demo.redis.cluster;

import java.util.HashSet;
import java.util.Map.Entry;

import bootdemo.demo.redis.cluster.config.RedisClusterConfig;

import java.util.Set;

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

    public static synchronized JedisCluster getJedisCluster() {
        Set<HostAndPort> nodes = new HashSet<>();
        if (jedisCluster == null) {
            for (Entry<Integer, String> entry : RedisClusterConfig.getHostAndPort().entrySet()) {
                nodes.add(new HostAndPort(entry.getValue(), entry.getKey()));
            }
            jedisCluster = new JedisCluster(nodes, RedisClusterConfig.getConnectionTimeout(),
                    RedisClusterConfig.getSocketTimeout(), RedisClusterConfig.getGenericObjectPoolConfig());
        }
        return jedisCluster;
    }
}
