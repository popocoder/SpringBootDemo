package bootdemo.demo.kafka.consumer.thread;

import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class KafkaToLocalThread implements Runnable {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(KafkaToLocalThread.class);

    /** kafka流 */
    private KafkaStream<String, String> stream;

    /** 线程序号 */
    private int threadNumber;

    /** topic主题 */
    private Queue<String> queue;

    /** 构造方法 */
    public KafkaToLocalThread(KafkaStream<String, String> stream, int threadNumber, Queue<String> queue) {
        this.threadNumber = threadNumber;
        this.stream = stream;
        this.queue = queue;
    }

    /**
     * 取出kafka中的数据，用队列存放在jvm中，取出json串
     */
    @Override
    public void run() {
        
        ConsumerIterator<String, String> it = stream.iterator();
        
        while (it.hasNext()) {
            String json = it.next().message();
            logger.debug("Thread " + threadNumber + ",kafka message, json: " + json);
            queue.add(json);
        }
    }

}
