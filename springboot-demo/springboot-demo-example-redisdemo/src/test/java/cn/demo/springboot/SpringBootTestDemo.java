package cn.demo.springboot;

import java.io.IOException;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.redis.cluster.RedisCluster;
import com.redis.cluster.config.RedisClusterConfig;
import com.redis.single.RedisSingle;
import com.redis.single.config.RedisSingleConfig;
import com.redis.single.utils.RedisUtil;
import com.springboot.demo.HelloWorldMainApplication;
import com.springboot.demo.core.listener.KeyExpiredListener;
import com.springboot.demo.utils.BeanTool;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HelloWorldMainApplication.class)
public class SpringBootTestDemo {

    /**
     * @Description redis单机版-默认配置测试(加载redis.single前缀)
     */
    @Test
    public void redisDefaultTest() {
        Jedis jedis = RedisSingle.getDefaultJedisPool().getResource();
        jedis.select(RedisSingle.getDefaultDb());
        String str = jedis.set("aaaa", "bbb", "nx", "ex", 10);
        jedis.close();
        System.out.println(str);
    }

    /**
     * @Description 启动redis单机版-默认监听器(加载redis.single前缀)
     * @throws IOException
     */
    @Test
    public void redisDefaultLisenerTest() throws IOException {
        RedisSingleConfig config = BeanTool.getBean(RedisSingleConfig.class);
        System.out.println("启动监听器");
        new Thread(new Runnable() {
            @SuppressWarnings("resource")
            @Override
            public void run() {
                new Jedis(config.getHost(), config.getPort())
                        // (*代表监听所有库,__keyevent@4__:expired 只监听4库key失效事件)
                        .psubscribe(new KeyExpiredListener(config.getHost(), config.getPort()), "*");
            }
        }).start();
        System.in.read();
    }

    /**
     * @Description 默认redis工具类,自动释放资源
     * @throws Exception
     */
    @Test
    public void redisDefaultTest1() throws Exception {
        String str = RedisUtil.set("aaaa", "bbb");
        System.out.println(str);
    }

    @Value("${admin.redis.host}")
    private String host;

    @Value("${admin.redis.port}")
    private Integer port;

    @Value("${admin.redis.db}")
    private Integer db;

    /**
     * @Description redis单机版-个性配置测试(自定义加载host,port)
     * @throws Exception
     */
    @Test
    public void redisAdminTest() throws Exception {
        Jedis jedis = RedisSingle.getJedisPool(new RedisSingleConfig(host, port)).getResource();
        jedis.select(db);
        String str = jedis.set("aaaa", "bbb", "nx", "ex", 10);
        jedis.close();
        System.out.println(str);
    }

    /**
     * @Description 启动redis单机版-个性配置测试(前缀admin.redis)
     * @throws IOException
     */
    @Test
    public void redisAdminLisenerTest() throws IOException {
        RedisSingleConfig config = new RedisSingleConfig(host, port, db);
        System.out.println("启动监听器");
        new Thread(new Runnable() {
            @SuppressWarnings("resource")
            @Override
            public void run() {
                new Jedis(config.getHost(), config.getPort())
                        // (*代表监听所有库,__keyevent@4__:expired 只监听4库key失效事件)
                        .psubscribe(new KeyExpiredListener(config.getHost(), config.getPort()), "*");
            }
        }).start();
        System.in.read();
    }

    /**
     * @throws IOException
     * @Description redis集群版-默认配置测试(加载redis.cluster前缀)
     */
    @Test
    public void redisClusterDefaultTest() throws IOException {
        JedisCluster jedisCluster = RedisCluster.getDefaultJedisCluster();
        String str = jedisCluster.set("aaaa", "bbb", "nx", "ex", 10);
        System.out.println(str);
    }

    /**
     * @Description 启动redis集群默认监听器(加载redis.cluster前缀)
     * @throws IOException
     */
    @Test
    public void redisClusterDefaultLisenerTest() throws IOException {
        System.out.println("启动监听器");
        for (Entry<Integer, String> entry : BeanTool.getBean(RedisClusterConfig.class).getHostAndPort().entrySet()) {
            new Thread(new Runnable() {
                @SuppressWarnings("resource")
                @Override
                public void run() {
                    new JedisCluster(new HostAndPort(entry.getValue(), entry.getKey()))
                            .psubscribe(new KeyExpiredListener(entry.getValue(), entry.getKey()), "*");
                }
            }).start();
        }
        System.in.read();
    }

    @Value("${admin.cluster.hostAndPort}")
    private String hostAndPort;

    /**
     * @Description redis集群版-个性配置测试(自定义加载hostAndPort)
     * @throws IOException
     */
    @Test
    public void redisClusterAdminTest() throws IOException {
        JedisCluster jedisCluster = RedisCluster.getJedisCluster(new RedisClusterConfig(hostAndPort));
        String str = jedisCluster.set("aaaa", "bbb", "nx", "ex", 10);
        System.out.println(str);
    }

    /**
     * @Description 启动自定义监听器(加载自定义前缀 "${admin.cluster.hostAndPort}")
     * @throws IOException
     */
    @Test
    public void redisClusterAdminLisenerTest() throws IOException {
        System.out.println("启动监听器");
        for (Entry<Integer, String> entry : new RedisClusterConfig(hostAndPort).getHostAndPort().entrySet()) {
            new Thread(new Runnable() {
                @SuppressWarnings("resource")
                @Override
                public void run() {
                    new JedisCluster(new HostAndPort(entry.getValue(), entry.getKey()))
                            .psubscribe(new KeyExpiredListener(entry.getValue(), entry.getKey()), "*");
                }
            }).start();
        }
        System.in.read();
    }

    // @Test
    // public void test(){
    // RedisSingleConfig config1 = new RedisSingleConfig("192.168.212.212", "1001");
    // RedisSingleConfig config2 = new RedisSingleConfig("192.168.212.212", "1001");
    // System.out.println(config1.equals(config2));
    // System.out.println(config1.hashCode()==config2.hashCode());
    // Map<Object, Object> map = new HashMap<>();
    // map.put(config1, 11);
    // map.put(config2, 22);
    // System.out.println(map.keySet());
    // System.out.println(map.values());
    // }

}
