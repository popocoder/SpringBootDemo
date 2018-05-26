//package bootdemo.demo.kafka;
//
//public class demo {
//
//}
//package com.awifi.athena.deviceService.core.heartbeat.kafka;
//
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import org.apache.log4j.Logger;
//
//import com.awifi.athena.deviceService.core.heartbeat.thread.HeartBeatSetThread;
//import com.awifi.athena.deviceService.core.heartbeat.utils.PropertyPlaceholderUtil;
//import com.awifi.athena.deviceservice.common.utils.PropertiesUtil;
//
//import kafka.consumer.Consumer;
//import kafka.consumer.ConsumerConfig;
//import kafka.consumer.KafkaStream;
//import kafka.javaapi.consumer.ConsumerConnector;
//import kafka.serializer.StringDecoder;
//import kafka.utils.VerifiableProperties;
//
//public class HeartBeatKafkaConsumer {
//
//    /**
//     * 日志
//     */
//    private static Logger LOG = Logger.getLogger(HeartBeatKafkaConsumer.class);
//    
//    /**
//     * 消费者主题
//     */
//    private String topic;
//
//    /**
//     * kafka消费者
//     */
//    private final ConsumerConnector consumer;
//    
//    /**设备状态：上下线 主题*/
//    private String statusTopic;
//
//    /**
//     * 线程池
//     */
//    private ExecutorService executor;
//    
//    public HeartBeatKafkaConsumer(String topicName,String statusTopic) {
//        this.consumer = Consumer.createJavaConsumerConnector(getConsumerConfig());
//        this.topic = topicName;
//        this.statusTopic = statusTopic;
//        LOG.info("初始化kafka topic = " + this.topic);
//    }
//    
//    /**
//    * @Title:getConsumerConfig
//    * @Description:消费者的配置
//    * @return ConsumerConfig
//     */
//    private ConsumerConfig getConsumerConfig() {
//        Properties props = new Properties();
//
//        String zookeeperConnects = PropertyPlaceholderUtil.getProperty("consumer.zookeeper.connect");
//        String groupId = PropertyPlaceholderUtil.getProperty("consumer.group.id");
//        String syncTimeOut = PropertyPlaceholderUtil.getProperty("consumer.zookeeper.session.timeout.ms");
//        String synctimes = PropertyPlaceholderUtil.getProperty("consumer.zookeeper.sync.time.ms");
//        String autoCommit = PropertyPlaceholderUtil.getProperty("consumer.auto.commit.interval.ms");
//        String reset = PropertyPlaceholderUtil.getProperty("consumer.auto.offset.reset");
//        String retries = PropertyPlaceholderUtil.getProperty("consumer.rebalance.max.retries");
//        String backoff = PropertyPlaceholderUtil.getProperty("consumer.rebalance.backoff.ms");
//        props.put("zookeeper.connect", zookeeperConnects);
//        props.put("group.id", groupId);
//        props.put("zookeeper.session.timeout.ms", syncTimeOut);
//        props.put("zookeeper.sync.time.ms", synctimes);
//        props.put("auto.commit.interval.ms", autoCommit);
//        props.put("auto.offset.reset", reset);
//        props.put("rebalance.max.retries", retries);
//        props.put("rebalance.backoff.ms", backoff);
//        return new ConsumerConfig(props);
//    }
//
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
//
//    /**
//    * @Title:consume
//    * @Description:消费kafka的数据
//    * @param numThreads 线程数
//     */
//    public void consume(int numThreads) {
//        // 消费消息
//        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
//        topicCountMap.put(topic, numThreads);
//
//        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
//        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
//
//        Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,
//                keyDecoder, valueDecoder);
//
//        List<KafkaStream<String, String>> streams = consumerMap.get(topic);
//
//        executor = Executors.newFixedThreadPool(numThreads);
//
//        for (KafkaStream<String, String> stream : streams) {
//            executor.submit(new HeartBeatSetThread(stream, statusTopic));
//        }
//    }
//
//}
//
//package com.awifi.athena.deviceService.core.heartbeat.kafka;
//
//
//import java.util.Properties;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.awifi.athena.deviceService.core.heartbeat.utils.PropertyPlaceholderUtil;
//
//import kafka.javaapi.producer.Producer;
//import kafka.producer.KeyedMessage;
//import kafka.producer.ProducerConfig;
//
//public class HeartBeatKafkaProducer {
//
//    /** Logger */
//    private static final Logger LOG = LoggerFactory.getLogger(HeartBeatKafkaProducer.class);
//
//    /** Singleton，饥汉 */
//    private static HeartBeatKafkaProducer INSTANCE = new HeartBeatKafkaProducer();
//
//    /**
//     * 定义生产者对象
//     */
//    private Producer<String, String> producer;
//
//    private HeartBeatKafkaProducer() {
//        producer = new Producer<String, String>(getProducerConfig());
//    }
//
//    /**
//    * @Title:getProducerConfig
//    * @Description:加载配置
//    * @return ProducerConfig
//     */
//    private ProducerConfig getProducerConfig() {
//        String brokerList = PropertyPlaceholderUtil.getProperty("producer.metadata.broker.list");
//        String serClass = PropertyPlaceholderUtil.getProperty("producer.serializer.class");
//        String keySerClass = PropertyPlaceholderUtil.getProperty("producer.key.serializer.class");
//        String requestAsks = PropertyPlaceholderUtil.getProperty("producer.request.required.acks");
//        Properties props = new Properties();
//        props.put("metadata.broker.list", brokerList);
//        props.put("serializer.class", serClass);
//        props.put("key.serializer.class", keySerClass);
//        props.put("request.required.acks", requestAsks);
//        return new ProducerConfig(props);
//    }
//
//    /**
//    * @Title:getProducerInstance
//    * @Description:获得kafka生产者单例
//    * @return HeartBeatKafkaProducer
//     */
//    public static HeartBeatKafkaProducer getProducerInstance() {
//        if (null == INSTANCE) {
//            INSTANCE = new HeartBeatKafkaProducer();
//        }
//        LOG.debug("Singleton instance:" + INSTANCE);
//        return INSTANCE;
//    }
//
//    /**
//     * 初始化
//     */
//    private void initProducer() {
//        if (null == this.producer) {
//            producer = new Producer<String, String>(getProducerConfig());
//        }
//    }
//
//    /**
//     * @param topic 主题
//     * @param message 消息
//     */
//    public void send(String topic, String message) {
//        try {
//            if (null == producer) {
//                initProducer();
//            }
//            producer.send(new KeyedMessage<String, String>(topic, ((int) (Math.random() * 100) + ""), message));
//            LOG.info("反馈处理结果：" + message);
//        } catch (Exception e) {
//            LOG.error("kafka发送失败", e);
//            retrySend(topic, message, 3);
//        }
//
//    }
//
//    /**
//    * @Title:retrySend
//    * @Description: kafka重发机制
//    * @param topic 主题
//    * @param message 消息
//    * @param tryTime 重试次数
//     */
//    private void retrySend(String topic, String message, int tryTime) {
//        int i = 1;
//        LOG.info("由于kafka发送失败重试" + i + "次");
//        while (i <= tryTime) {
//            try {
//                if (null == producer) {
//                    initProducer();
//                }
//                producer.send(new KeyedMessage<String, String>(topic, ((int) (Math.random() * 1000) + ""), message));
//                i = tryTime + 1;
//            } catch (Exception e) {
//                LOG.error("kafka发送失败", e);
//                i++;
//                try {
//                    Thread.sleep(i * 1000L);
//                } catch (InterruptedException e1) {
//                    LOG.error("沉睡拦截失败", e1);
//                }
//            }
//        }
//    }
//
//    /**
//     * 关系释放资源
//     */
//    public void close() {
//        try {
//            if (null != producer) {
//                producer.close();
//            }
//        } catch (Exception e) {
//        }
//    }
//
//}
//
