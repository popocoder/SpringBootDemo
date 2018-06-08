package bootdemo.demo.kafka.consumer.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName CollectQueue
 * @Description 采集数据队列
 * @author LBQ
 * @Date 2018年6月8日 下午10:56:10
 * @version 1.0.0
 */
public class CollectQueue {
    private CollectQueue() {

    }

    public static final ConcurrentHashMap<String, Queue<String>> HASHMAP = new ConcurrentHashMap<>();

    /**
     * @Description 根据topic动态加管道
     * @param topic
     * @return
     */
    public static Queue<String> getQueue(String topic) {
        HASHMAP.containsKey(topic);
        if (!HASHMAP.containsKey(topic) || null == HASHMAP.get(topic)) {
            HASHMAP.put(topic, new LinkedBlockingQueue<String>());
        }
        return HASHMAP.get(topic);
    }
}
