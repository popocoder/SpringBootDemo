package com.springboot.demo.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPubSub;

public class KeyExpiredListener extends JedisPubSub {

    Logger log = LoggerFactory.getLogger(KeyExpiredListener.class);
    
    private String host;

    private Integer port;

    public KeyExpiredListener(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        log.info("====监听Redis集群=KeyExpired=["+host+":"+port+"]");
        System.out.println("onPSubscribe " + pattern + " " + subscribedChannels);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        log.info("========监听Redis集群=KeyExpired=["+host+":"+port+"]-收到消息:"+message);
        System.out.println("onPMessage pattern " + pattern + " " + channel + " " + message);
    }
}
