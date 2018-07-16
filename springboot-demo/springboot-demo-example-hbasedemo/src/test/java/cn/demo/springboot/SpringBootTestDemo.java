package cn.demo.springboot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hbase.bean.HbaseData;
import com.hbase.util.HbaseOpt;
import com.springboot.demo.HelloWorldMainApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HelloWorldMainApplication.class)
public class SpringBootTestDemo {

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

        String rowKey = "fghij456789";
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

}
