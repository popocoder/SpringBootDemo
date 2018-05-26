package bootdemo.demo.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import kafka.consumer.Consumer;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

public class KafkaDemoConsumer {

    /**
     * 日志
     */
    private static Logger LOG = Logger.getLogger(KafkaDemoConsumer.class);

    public KafkaDemoConsumer() {
    }

//    /**
//     * 关闭线程池
//     */
//    public void shutdown() {
//        if (this.consumer != null) {
//            this.consumer.shutdown();
//        }
//        if (this.executor != null) {
//            this.executor.shutdown();
//        }
//        LOG.info("线程池已关闭");
//    }

    /**
     * @Title:consume
     * @Description:消费kafka的数据
     * @param numThreads 线程数
     */
    public void consume(int numThreads, String topic) {
        // ==========获取kafka消费者==============
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(KafkaConfigConsumer.getConsumerConfig());
        LOG.info("初始化kafka topic = " + topic);
        // ==========收费消息====================
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, numThreads);

        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

        Map<String, List<KafkaStream<String, String>>> consumerMap =
                consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);

        // =============获取数据流=================
        List<KafkaStream<String, String>> streams = consumerMap.get(topic);

        // ==============线程池====================
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int threadNumber = 0;
        for (KafkaStream<String, String> stream : streams) {
            executor.submit(new CollectThread(stream, threadNumber, topic));
            threadNumber++;
        }

    }
}
