package bootdemo.demo.kafka.consumer.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class CollectQueue {
    private CollectQueue() {

    }

    public static final ConcurrentHashMap<String, Queue<String>> HASHMAP = new ConcurrentHashMap<>();

    public static Queue<String> getQueue(String topic) {
        HASHMAP.containsKey(topic);
        if (!HASHMAP.containsKey(topic) || null == HASHMAP.get(topic)) {
            HASHMAP.put(topic, new LinkedBlockingQueue<String>());
        }
        return HASHMAP.get(topic);
    }
}
