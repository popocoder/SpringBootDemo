package cn.demo.springboot;

import java.util.Map;
import java.util.Queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import bootdemo.demo.HelloWorldMainApplication;
import bootdemo.demo.kafka.consumer.KafkaDemoConsumer;
import bootdemo.demo.kafka.consumer.queue.CollectQueue;
import bootdemo.demo.kafka.producer.KafkaDemoProducer;
import bootdemo.demo.kafka.producer.config.KafkaConfigProducer;
import bootdemo.demo.proxy.CglibProxy;
import bootdemo.demo.redis.AdminRedis;
import bootdemo.demo.redis.BizRedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HelloWorldMainApplication.class)
public class SpringBootTestDemo {

    @Autowired
    private BizRedis biz;

    @Value("${consumer.topic_alarm}")
    private String topic;

    @Test
    public void redisTest() {
        JedisPool jedisPool = BizRedis.getJedisPool();
        long count = 0;
        while (true) {
            Jedis jedis = jedisPool.getResource();
            jedis.select(BizRedis.getDb());
            Map<String, String> hgetAll = jedis.hgetAll("athena_biz_location_1");
            System.out.println(count);
            count++;
            jedis.close();
        }
    }

    private static volatile int total;

    /**
     * @Description producer 一个生产者 十万条数据 24秒左右,两个生产者则降低至 13秒
     * @throws Exception
     */
    @Test
    public void producerTest() throws Exception {
        total = 1 * 1000 * 10;
        long startTime = System.currentTimeMillis();
        KafkaDemoProducer kafkaDemoProducer = new KafkaDemoProducer();
        KafkaDemoProducer kafkaDemoProducer1 = new KafkaDemoProducer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (total > 0) {
                    kafkaDemoProducer1.send(KafkaConfigProducer.topic_alarm, "good lucky!-" + total);
                    total--;
                }
            }
        }).start();
        while (total > 0) {
            kafkaDemoProducer.send(KafkaConfigProducer.topic_alarm, "good lucky!-" + total);
            total--;
        }
        long endTime = System.currentTimeMillis();

        double totalTime = (endTime - startTime) / 1000;

        double aveTiem = total / totalTime;

        System.out.println("发送十万条消息 耗时-" + totalTime + "秒");
        System.out.println("平均耗时-" + (aveTiem) + "/每条");

        System.in.read();
    }

    @Test
    public void consumerTest() {
        KafkaDemoConsumer consumer = new KafkaDemoConsumer();
        consumer.consume(1, topic);

        Queue<String> queue = CollectQueue.getQueue(topic);
        while (true) {
            if (!queue.isEmpty()) {
                System.out.println(queue.poll());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
    
}
