package bootdemo.demo.redis.cluster;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import bootdemo.demo.redis.cluster.config.RedisClusterConfig;
import bootdemo.demo.redis.single.listener.KeyExpiredListener;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@SuppressWarnings("resource")
@Component
public class InitServer implements InitializingBean {

    Logger log = LoggerFactory.getLogger(InitServer.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("启动监听器");
        for (Entry<Integer, String> entry : RedisClusterConfig.getHostAndPort().entrySet()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new JedisCluster(new HostAndPort(entry.getValue(), entry.getKey()))
                            .psubscribe(new KeyExpiredListener(entry.getValue(), entry.getKey()), "*");
                }
            }).start();
        }
    }

}
