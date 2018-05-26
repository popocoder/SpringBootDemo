package bootdemo.demo.kafka.consumer.config;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import kafka.consumer.ConsumerConfig;

/**
 * @ClassName KafkaConfigConsumer
 * @Description kafka消费者配置类,由于需要先加载参数,再生产配置对象,采用懒汉单例模式
 * @author LBQ
 * @Date 2018年5月21日 上午9:43:04
 * @version 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "consumer")
public class KafkaConfigConsumer {

    private static ConsumerConfig consumerConfig;

    private static String zookeeper_connect;

    private static String group_id;

    private static String zookeeper_session_timeout_ms;

    private static String zookeeper_sync_time_ms;

    private static String auto_commit_interval_ms;

    private static String auto_offset_reset;

    private static String rebalance_max_retries;

    private static String rebalance_backoff_ms;

    /**
     * @Description 获取消费者配置对象 ConsumerConfig
     * @return
     */
    public static synchronized ConsumerConfig getConsumerConfig() {
        if (consumerConfig == null) {
            Properties props = new Properties();
            props.put("zookeeper.connect", zookeeper_connect);
            props.put("group.id", group_id);
            props.put("zookeeper.session.timeout.ms", zookeeper_session_timeout_ms);
            props.put("zookeeper.sync.time.ms", zookeeper_sync_time_ms);
            props.put("auto.commit.interval.ms", auto_commit_interval_ms);
            props.put("auto.offset.reset", auto_offset_reset);
            props.put("rebalance.max.retries", rebalance_max_retries);
            props.put("rebalance.backoff.ms", rebalance_backoff_ms);
            consumerConfig = new ConsumerConfig(props);
        }
        return consumerConfig;
    }

    public static String getZookeeper_connect() {
        return zookeeper_connect;
    }

    public void setZookeeper_connect(String zookeeper_connect) {
        KafkaConfigConsumer.zookeeper_connect = zookeeper_connect;
    }

    public static String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        KafkaConfigConsumer.group_id = group_id;
    }

    public static String getZookeeper_session_timeout_ms() {
        return zookeeper_session_timeout_ms;
    }

    public void setZookeeper_session_timeout_ms(String zookeeper_session_timeout_ms) {
        KafkaConfigConsumer.zookeeper_session_timeout_ms = zookeeper_session_timeout_ms;
    }

    public static String getZookeeper_sync_time_ms() {
        return zookeeper_sync_time_ms;
    }

    public void setZookeeper_sync_time_ms(String zookeeper_sync_time_ms) {
        KafkaConfigConsumer.zookeeper_sync_time_ms = zookeeper_sync_time_ms;
    }

    public static String getAuto_commit_interval_ms() {
        return auto_commit_interval_ms;
    }

    public void setAuto_commit_interval_ms(String auto_commit_interval_ms) {
        KafkaConfigConsumer.auto_commit_interval_ms = auto_commit_interval_ms;
    }

    public static String getAuto_offset_reset() {
        return auto_offset_reset;
    }

    public void setAuto_offset_reset(String auto_offset_reset) {
        KafkaConfigConsumer.auto_offset_reset = auto_offset_reset;
    }

    public static String getRebalance_max_retries() {
        return rebalance_max_retries;
    }

    public void setRebalance_max_retries(String rebalance_max_retries) {
        KafkaConfigConsumer.rebalance_max_retries = rebalance_max_retries;
    }

    public static String getRebalance_backoff_ms() {
        return rebalance_backoff_ms;
    }

    public void setRebalance_backoff_ms(String rebalance_backoff_ms) {
        KafkaConfigConsumer.rebalance_backoff_ms = rebalance_backoff_ms;
    }

}
