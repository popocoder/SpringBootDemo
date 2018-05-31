package bootdemo.demo.hbase.util;

import java.io.IOException;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import bootdemo.demo.hbase.pool.HbasePool;

public class HbaseOpt {

    private static Logger logger = Logger.getLogger(HbaseOpt.class);

    /**
     * @Description 创建表
     * @param tableName 表名
     * @param version 版本数量
     * @param columnFamilys 列族
     * @throws Exception
     */
    public static void createTable(String tableName, int version, String...columnFamilys) throws Exception {
        String details = "";
        // ============获取连接管理=============
        Connection con = HbasePool.getConnection();
        Admin admin = con.getAdmin();

        // ============查询表是否存在=============
        if (!admin.tableExists(TableName.valueOf(tableName))) {
            throw new Exception("需要创建的表[" + tableName + "]-已经存在！");
        }

        // ============组装创建表数据============
        try {
            TableDescriptorBuilder builder = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));
            for (String columnFamily : columnFamilys) {
                details += columnFamily + " ";
                builder.addColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(columnFamily))
                        .setMaxVersions(version).build());
            }
            admin.createTable(builder.build());
        } finally {
            if (admin != null) {
                try {
                    admin.close();
                } catch (IOException e) {
                    logger.error("Admin资源释放失败！", e);
                }
            }
            HbasePool.returnConnect(con);
        }
        logger.info("=====创建表,表名:" + tableName + "列族:" + details);
    }

    /**
     * @Description 删除表
     * @param tableName
     * @throws Exception
     */
    public static void dropTable(String tableName) throws Exception {

        // ============获取连接管理=============
        Connection con = HbasePool.getConnection();
        Admin admin = con.getAdmin();

        // ============查询表是否存在=============
        if (!admin.tableExists(TableName.valueOf(tableName))) {
            throw new Exception("需要删除的表[" + tableName + "]-不存在！");
        }

        // ============删除表=============
        try {
            if (!admin.isTableDisabled(TableName.valueOf(tableName))) {
                admin.disableTable(TableName.valueOf(tableName));
            }
            admin.deleteTable(TableName.valueOf(tableName));
        } finally {
            if (admin != null) {
                try {
                    admin.close();
                } catch (IOException e) {
                    logger.error("Admin资源释放失败！", e);
                }
            }
            HbasePool.returnConnect(con);
        }
        logger.info("=====删除表,表名:" + tableName);
    }
}
