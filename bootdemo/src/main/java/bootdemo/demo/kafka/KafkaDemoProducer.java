package bootdemo.demo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

public class KafkaDemoProducer {

    /** Logger */
    private static final Logger LOG = LoggerFactory.getLogger(KafkaDemoProducer.class);

    private Producer<String, String> producer;

    /**
     * @Description 空参构造,实例化对象
     */
    public KafkaDemoProducer() {
        initProducer();
    }

    /**
     * 初始化
     */
    private void initProducer() {
        if (null == producer) {
            producer = new Producer<>(KafkaConfigProducer.getProducerConfig());
        }
    }

    /**
     * @param topic 主题
     * @param message 消息
     */
    public void send(String topic, String message) {
        try {
            if (null == producer) {
                initProducer();
            }
            producer.send(new KeyedMessage<String, String>(topic, ((int) (Math.random() * 100) + ""), message));
            LOG.info("反馈处理结果：" + message);
        } catch (Exception e) {
            LOG.error("kafka发送失败", e);
            retrySend(topic, message, 3);
        }
    }

    /**
     * @Title:retrySend
     * @Description: kafka重发机制
     * @param topic 主题
     * @param message 消息
     * @param tryTime 重试次数
     */
    private void retrySend(String topic, String message, int tryTime) {
        int i = 1;
        LOG.info("由于kafka发送失败重试" + i + "次");
        while (i <= tryTime) {
            try {
                if (null == producer) {
                    initProducer();
                }
                producer.send(new KeyedMessage<String, String>(topic, ((int) (Math.random() * 1000) + ""), message));
                i = tryTime + 1;
            } catch (Exception e) {
                LOG.error("kafka发送失败", e);
                i++;
                try {
                    Thread.sleep(i * 1000L);
                } catch (InterruptedException e1) {
                    LOG.error("沉睡拦截失败", e1);
                }
            }
        }
    }

    /**
     * 关系释放资源
     */
    public void close() {
        try {
            if (null != producer) {
                producer.close();
            }
        } catch (Exception e) {
        }
    }
}
