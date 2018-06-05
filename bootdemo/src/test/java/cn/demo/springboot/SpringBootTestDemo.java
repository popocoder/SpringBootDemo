package cn.demo.springboot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.hadoop.hbase.client.Connection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;

import bootdemo.demo.HelloWorldMainApplication;
import bootdemo.demo.hbase.bean.HbaseData;
import bootdemo.demo.hbase.bean.HbaseDataDTO;
import bootdemo.demo.hbase.pool.HbasePool;
import bootdemo.demo.hbase.util.HbaseOpt;
import bootdemo.demo.kafka.consumer.KafkaDemoConsumer;
import bootdemo.demo.kafka.consumer.queue.CollectQueue;
import bootdemo.demo.kafka.producer.KafkaDemoProducer;
import bootdemo.demo.kafka.producer.config.KafkaConfigProducer;
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

    @Test
    public void hbaseTest() {
        int a = 0;
        Queue<Connection> queue = new LinkedBlockingQueue<>();
        while (a < 40) {
            Connection connection = null;
            try {
                connection = HbasePool.getConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
            queue.add(connection);
            a++;
            System.out.println(HbasePool.conNum());
        }

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (!queue.isEmpty()) {
                HbasePool.returnConnect(queue.poll());
            }
            System.out.println(HbasePool.conNum());
        }
    }

    @Test
    public void hbasePutTest() throws Exception {
        String tableName = "userTest";
        int version = 10;
        String[] columnFamilys = { "info", "work", "money" };
        try {
            HbaseOpt.createTable(tableName, version, columnFamilys);
        } catch (Exception e) {
            System.out.println(e);
        }

        String rowKey = "abcde12345";
        HbaseOpt.deleteDataByRowKey(tableName, rowKey);

        HashMap<String, String> infoMap = new HashMap<>();
        infoMap.put("name", "lee");
        infoMap.put("age", "18");
        infoMap.put("gender", "man");
        HashMap<String, String> workMap = new HashMap<>();
        workMap.put("addr", "HangZhou");
        workMap.put("company", "awifi");
        HashMap<String, String> moneyMap = new HashMap<>();
        moneyMap.put("pay", "15k");
        List<HbaseData> hbaseDataList = new ArrayList<HbaseData>();
        hbaseDataList.add(new HbaseData("info", infoMap));
        hbaseDataList.add(new HbaseData("work", workMap));
        hbaseDataList.add(new HbaseData("money", moneyMap));
        int q = 0;
        while (q++ < 5) {
            HbaseOpt.insertData(tableName, rowKey, hbaseDataList);
        }
    }

    @Test
    public void hbaseGetTest() throws Exception {
        String tableName = "userTest";
        String rowKey = "abcde12345";

        // 仅根据rowKey查询所有
        List<Object> list1 = HbaseOpt.queryDataByParams(tableName, rowKey, 3, null);
        System.out.println(JSON.toJSONString(list1));

        // 根据rowKey - family 查
        Map<String, String[]> map = new HashMap<>();
        map.put("info", null);
        List<Object> list2 = HbaseOpt.queryDataByParams(tableName, rowKey, 3, map);
        System.out.println(JSON.toJSONString(list2));

        // 根据rowKey - family - column 查
        Map<String, String[]> map1 = new HashMap<>();
        map1.put("info", new String[] { "name", "age" });
        map1.put("work", new String[] { "addr" });
        List<Object> list3 = HbaseOpt.queryDataByParams(tableName, rowKey, 3, map1);
        System.out.println(JSON.toJSONString(list3));

        // 综合查询
        Map<String, String[]> map2 = new HashMap<>();
        map2.put("info", new String[] { "name", "age" });
        map2.put("work", null);
        map2.put("money", new String[] { "pay" });
        List<Object> list4 = HbaseOpt.queryDataByParams(tableName, rowKey, 3, map2);
        System.out.println(JSON.toJSONString(list4));
    }
}
