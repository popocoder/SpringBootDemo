package bootdemo.demo.hbase;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class HTest {

    private static Configuration config = null;
    private static Connection connection = null;
    private static Table table = null;

    public static void main(String[] args) throws Exception {
        config = HBaseConfiguration.create();
        // config.set("hadoop.home.dir", "E:\\LearnResource\\Hadoop\\hadoop-2.6.5");
        // System.setProperty("hadoop.home.dir", "E:\\LearnResource\\Hadoop\\hadoop-2.6.5");
        // config.set("hbase.zookeeper.quorum", "hellotest");// 单机

        config.set("hbase.zookeeper.quorum", "linux1,linux2,linux3");// zookeeper地址
        config.set("hbase.zookeeper.property.clientPort", "2181");// zookeeper端口
        connection = ConnectionFactory.createConnection(config);
        table = connection.getTable(TableName.valueOf("user"));
//        testCrateTable();
         testDropTable();
        // insertDate();
        // insertBatch();
        // uodateData();
        // deleteData();
        // queryData();
        // scanDataByFilter1();
        // scanDataByFilter3();
        table.close();
        connection.close();
    }

    public static void testCrateTable() throws IOException {
        // 表管理类
        Admin admin = connection.getAdmin();

        try {
            TableDescriptor tableDesc = TableDescriptorBuilder.newBuilder(TableName.valueOf("user"))
                    .addColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("name")).build())
                    .addColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("age")).build()).build();
            admin.createTable(tableDesc);
        } finally {
            admin.close();
        }
        System.out.println("创建user表");
    }

    public static void testDropTable() throws IOException {
        Admin admin = connection.getAdmin();

        try {
            admin.disableTable(TableName.valueOf("user"));
            admin.deleteTable(TableName.valueOf("user"));
        } finally {
            admin.close();
        }
        System.out.println("删除表成功");
    }

    public static void insertData() throws IOException {
        // 表管理类
        Put put = new Put(Bytes.toBytes("wangwu_1"));
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("wangwang"));
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes(50));
        table.put(put);
        System.out.println("添加成功");
    }

    public static void insertBatch() throws IOException {

        ArrayList<Put> arrayList = new ArrayList<Put>();
        for (int i = 21; i < 50; i++) {
            Put put = new Put(Bytes.toBytes("1234" + i));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("wangwu" + i));
            put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("password"), Bytes.toBytes(1234 + i));
            arrayList.add(put);
        }

        // 插入数据
        table.put(arrayList);
    }

    /**
     * 修改数据 相同 rowkey 列簇 column 会覆盖新值
     * 
     * @throws Exception
     */

    public static void uodateData() throws Exception {
        Put put = new Put(Bytes.toBytes("wangwu_1"));
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("lisi1234"));
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes(23));
        // 插入数据
        table.put(put);

    }

    /**
     * 删除数据
     * 
     * @throws IOException
     */
    public static void deleteData() throws IOException {
        // 按rowkey删除
        Delete delete = new Delete(Bytes.toBytes("123434"));
        table.delete(delete);

        // 按family删除
        Delete delete2 = new Delete(Bytes.toBytes("123435"));
        delete2.addFamily(Bytes.toBytes("info"));
        table.delete(delete2);

        // 按column删除
        Delete delete3 = new Delete(Bytes.toBytes("123436"));
        delete3.addColumns(Bytes.toBytes("info"), Bytes.toBytes("password"));
        table.delete(delete3);

        System.out.println("删除数据成功");
    }

    public static void queryData() throws IOException {
        Get get = new Get(Bytes.toBytes("123449"));
        Result result = table.get(get);
        System.out.println(Bytes.toInt(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("password"))));
        System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name"))));

    }

    /**
     * 全表扫描
     * 
     * @throws Exception
     */

    public static void scanData() throws Exception {
        Scan scan = new Scan();
        // scan.addFamily(Bytes.toBytes("info")); //扫描指定family
        // scan.addColumn(Bytes.toBytes("info"),
        // Bytes.toBytes("password"));//扫描column指定

        // 按rowkey字典序扫描
        // scan.setStartRow(Bytes.toBytes("wangsf_0"));
        // scan.setStopRow(Bytes.toBytes("wangwu"));
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println(Bytes.toInt(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("password"))));
            System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name"))));
            // System.out.println(Bytes.toInt(result.getValue(Bytes.toBytes("info2"),
            // Bytes.toBytes("password"))));
            // System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("info2"),
            // Bytes.toBytes("name"))));
        }
    }

    /**
     * 全表扫描的过滤器 列值过滤器
     * 
     * @throws Exception
     */

    public static void scanDataByFilter1() throws Exception {

        // 创建全表扫描的scan
        Scan scan = new Scan();
        // 过滤器：列值过滤器
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("name"),
                CompareOp.EQUAL, Bytes.toBytes("lisi1234"));
        // 设置过滤器
        scan.setFilter(filter);

        // 打印结果集
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println(Bytes.toInt(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name"))));
            System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("age"))));
            // System.out.println(Bytes.toInt(result.getValue(Bytes.toBytes("info2"),
            // Bytes.toBytes("password"))));
            // System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("info2"),
            // Bytes.toBytes("name"))));
        }

    }

    /**
     * rowkey过滤器
     * 
     * @throws Exception
     */
    public static void scanDataByFilter2() throws Exception {

        // 创建全表扫描的scan
        Scan scan = new Scan();
        // 匹配rowkey以wangsenfeng开头的
        RowFilter filter = new RowFilter(CompareOp.EQUAL, new RegexStringComparator("^123"));
        // 设置过滤器
        scan.setFilter(filter);
        // 打印结果集
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println(Bytes.toInt(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("password"))));
            System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name"))));
            // System.out.println(Bytes.toInt(result.getValue(Bytes.toBytes("info2"),
            // Bytes.toBytes("password"))));
            // System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("info2"),
            // Bytes.toBytes("name"))));
        }

    }

    /**
     * 匹配列名前缀
     * 
     * @throws Exception
     */

    public static void scanDataByFilter3() throws Exception {

        // 创建全表扫描的scan
        Scan scan = new Scan();
        // 匹配rowkey以wangsenfeng开头的
        ColumnPrefixFilter filter = new ColumnPrefixFilter(Bytes.toBytes("na7"));
        // 设置过滤器
        scan.setFilter(filter);
        // 打印结果集
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println("rowkey：" + Bytes.toString(result.getRow()));
            System.out.println(
                    "info:name：" + Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name"))));
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info"), Bytes.toBytes("age")) != null) {
                System.out.println(
                        "info:age：" + Bytes.toInt(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("age"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info"), Bytes.toBytes("sex")) != null) {
                System.out.println(
                        "infi:sex：" + Bytes.toInt(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("sex"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("name")) != null) {
                System.out.println(
                        "info2:name：" + Bytes.toString(result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("name"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("age")) != null) {
                System.out.println(
                        "info2:age：" + Bytes.toInt(result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("age"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("sex")) != null) {
                System.out.println(
                        "info2:sex：" + Bytes.toInt(result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("sex"))));
            }
        }

    }

    /**
     * 过滤器集合
     * 
     * @throws Exception
     */

    public static void scanDataByFilter4() throws Exception {

        // 创建全表扫描的scan
        Scan scan = new Scan();
        // 过滤器集合：MUST_PASS_ALL（and）,MUST_PASS_ONE(or)
        FilterList filterList = new FilterList(Operator.MUST_PASS_ONE);
        // 匹配rowkey以wangsenfeng开头的
        RowFilter filter = new RowFilter(CompareOp.EQUAL, new RegexStringComparator("^wangsenfeng"));
        // 匹配name的值等于wangsenfeng
        SingleColumnValueFilter filter2 = new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("name"),
                CompareOp.EQUAL, Bytes.toBytes("zhangsan"));
        filterList.addFilter(filter);
        filterList.addFilter(filter2);
        // 设置过滤器
        scan.setFilter(filterList);
        // 打印结果集
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println("rowkey：" + Bytes.toString(result.getRow()));
            System.out.println(
                    "info:name：" + Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name"))));
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info"), Bytes.toBytes("age")) != null) {
                System.out.println(
                        "info:age：" + Bytes.toInt(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("age"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info"), Bytes.toBytes("sex")) != null) {
                System.out.println(
                        "infi:sex：" + Bytes.toInt(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("sex"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("name")) != null) {
                System.out.println(
                        "info2:name：" + Bytes.toString(result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("name"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("age")) != null) {
                System.out.println(
                        "info2:age：" + Bytes.toInt(result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("age"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("sex")) != null) {
                System.out.println(
                        "info2:sex：" + Bytes.toInt(result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("sex"))));
            }
        }

    }

}