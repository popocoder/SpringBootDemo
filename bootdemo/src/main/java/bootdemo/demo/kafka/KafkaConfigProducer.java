package bootdemo.demo.kafka;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import kafka.producer.ProducerConfig;

@Component
@ConfigurationProperties(prefix = "producer")
public class KafkaConfigProducer {

    private static ProducerConfig producerConfig;

    private static String metadata_broker_list;

    private static String request_required_acks;

    private static String key_serializer_class;

    private static String serializer_class;

    public static String topic_alarm;

    public static String getTopic_alarm() {
        return topic_alarm;
    }

    public void setTopic_alarm(String topic_alarm) {
        KafkaConfigProducer.topic_alarm = topic_alarm;
    }

    public static synchronized ProducerConfig getProducerConfig() {
        if (producerConfig == null) {
            Properties props = new Properties();
            props.put("metadata.broker.list", metadata_broker_list);
            props.put("serializer.class", serializer_class);
            props.put("key.serializer.class", key_serializer_class);
            props.put("request.required.acks", request_required_acks);
            producerConfig = new ProducerConfig(props);
        }
        return producerConfig;
    }

    public static String getMetadata_broker_list() {
        return metadata_broker_list;
    }

    public void setMetadata_broker_list(String metadata_broker_list) {
        KafkaConfigProducer.metadata_broker_list = metadata_broker_list;
    }

    public static String getRequest_required_acks() {
        return request_required_acks;
    }

    public void setRequest_required_acks(String request_required_acks) {
        KafkaConfigProducer.request_required_acks = request_required_acks;
    }

    public static String getKey_serializer_class() {
        return key_serializer_class;
    }

    public void setKey_serializer_class(String key_serializer_class) {
        KafkaConfigProducer.key_serializer_class = key_serializer_class;
    }

    public static String getSerializer_class() {
        return serializer_class;
    }

    public void setSerializer_class(String serializer_class) {
        KafkaConfigProducer.serializer_class = serializer_class;
    }

}
