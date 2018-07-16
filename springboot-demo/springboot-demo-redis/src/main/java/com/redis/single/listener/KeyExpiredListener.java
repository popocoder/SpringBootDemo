package com.redis.single.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPubSub;

/**
 * @ClassName KeyExpiredListener
 * @Description [监听示例],需根据业务需求,自行编写逻辑
 * @author LBQ
 * @Date 2018年6月12日 下午3:32:42
 * @version 1.0.0
 */
public abstract class KeyExpiredListener extends JedisPubSub {

    Logger log = LoggerFactory.getLogger(KeyExpiredListener.class);

    private String host;

    private Integer port;

    public KeyExpiredListener(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 监听管道
     */
    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        log.info("====监听Redis单机=KeyExpired=[" + host + ":" + port + "]");
        System.out.println("onPSubscribe " + pattern + " " + subscribedChannels);
    }

    /**
     * 接收消息管道
     **/
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        log.info("========监听Redis单机=KeyExpired=[" + host + ":" + port + "]-接收到消息:" + message);
        System.out.println("onPMessage pattern " + pattern + " " + channel + " " + message);
    }
}
