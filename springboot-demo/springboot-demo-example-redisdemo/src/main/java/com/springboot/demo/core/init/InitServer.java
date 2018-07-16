package com.springboot.demo.core.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @ClassName InitServer
 * @Description 加载完配置文件后,调用afterPropertiesSet()方法一次
 * @author LBQ
 * @Date 2018年6月13日 下午4:10:34
 * @version 1.0.0
 */
@SuppressWarnings("resource")
@Component
public class InitServer implements InitializingBean {

    Logger log = LoggerFactory.getLogger(InitServer.class);

    @Override
    public void afterPropertiesSet() throws Exception {
//        log.info("启动监听器");
//        for (Entry<Integer, String> entry : RedisClusterConfig.getHostAndPort().entrySet()) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    new JedisCluster(new HostAndPort(entry.getValue(), entry.getKey()))
//                            .psubscribe(new KeyExpiredListener(entry.getValue(), entry.getKey()), "*");
//                }
//            }).start();
//        }
    }

}
