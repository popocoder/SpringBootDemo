package bootdemo.demo.hbase.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import bootdemo.demo.hbase.bean.HbaseData;
import bootdemo.demo.hbase.bean.HbaseDataDTO;
import bootdemo.demo.hbase.pool.HbasePool;

/**
 * @ClassName HbaseOpt
 * @Description Hbase2.0.0 操作类
 * @author LBQ
 * @Date 2018年6月4日 下午9:42:32
 * @version 1.0.0
 */
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
        Connection con = null;
        Admin admin = null;
        try {
            // ============获取连接管理=============
            con = HbasePool.getConnection();
            admin = con.getAdmin();

            // ============查询表是否存在=============
            if (admin.tableExists(TableName.valueOf(tableName))) {
                throw new Exception("需要创建的表[" + tableName + "]-已经存在！");
            }

            // ============组装创建表数据============

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
                    logger.error("Hbase - Admin资源释放失败！", e);
                }
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }
        logger.info("=====创建表,表名:" + tableName + "列族:" + details);
    }

    /**
     * @Description 删除表
     * @param tableName
     * @throws Exception
     */
    public static void dropTable(String tableName) throws Exception {

        Connection con = null;
        Admin admin = null;
        try {
            // ============获取连接管理=============
            con = HbasePool.getConnection();
            admin = con.getAdmin();
            // ============查询表是否存在=============
            if (!admin.tableExists(TableName.valueOf(tableName))) {
                throw new Exception("需要删除的表[" + tableName + "]-不存在！");
            }

            // ============删除表=============
            if (!admin.isTableDisabled(TableName.valueOf(tableName))) {
                admin.disableTable(TableName.valueOf(tableName));
            }
            admin.deleteTable(TableName.valueOf(tableName));
        } finally {
            if (admin != null) {
                try {
                    admin.close();
                } catch (IOException e) {
                    logger.error("Hbase - Admin资源释放失败！", e);
                }
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }

        logger.info("=====删除表,表名:" + tableName);
    }

    /**
     * @Description 插入数据 - 单条模式
     * @param tableName String
     * @param hbaseDTOList String rowKey List<HbaseData> hbaseDataList
     * @throws Exception
     */
    public static void insertData(String tableName, String rowKey, List<HbaseData> hbaseDataList) throws Exception {
        Connection con = null;
        Table table = null;
        try {
            // ============获取连接管理==================
            con = HbasePool.getConnection();
            table = con.getTable(TableName.valueOf(tableName));

            // ============进行数据解析,封装PUT============
            Put put = new Put(Bytes.toBytes(rowKey));
            for (HbaseData hbaseData : hbaseDataList) {
                String columnFamily = hbaseData.getColumnFamily();
                for (Entry<String, String> entry : hbaseData.getColumnValueMap().entrySet()) {
                    put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(entry.getKey()),
                            Bytes.toBytes(entry.getValue()));
                }
            }
            table.put(put);
        } finally {
            if (table != null) {
                table.close();
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }
        logger.info("=====插入数据,表名:[" + tableName + "],rowKey:[" + rowKey + "],List<HbaseData>:" + hbaseDataList);
    }

    /**
     * @Description 插入数据 - 批量插入模式
     * @param tableName String
     * @param hbaseDTOList String rowKey List<HbaseData> hbaseDataList
     * @throws Exception
     */
    public static void insertBatchData(String tableName, List<HbaseDataDTO> hbaseDTOList) throws Exception {
        Connection con = null;
        Table table = null;
        try {
            // ============获取连接管理==================
            con = HbasePool.getConnection();
            table = con.getTable(TableName.valueOf(tableName));

            // ============进行数据解析,封装PUT============
            List<Put> putList = new ArrayList<>();
            for (int index = 0; index < hbaseDTOList.size(); index++) {
                HbaseDataDTO hbaseDTO = hbaseDTOList.get(index);
                Put put = new Put(Bytes.toBytes(hbaseDTO.getRowKey()));
                for (HbaseData hbaseData : hbaseDTO.getHbaseDataList()) {
                    String columnFamily = hbaseData.getColumnFamily();
                    for (Entry<String, String> entry : hbaseData.getColumnValueMap().entrySet()) {
                        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(entry.getKey()),
                                Bytes.toBytes(entry.getValue()));
                    }
                }
                putList.add(put);
                // ===============设置成1000次,提交一次==============
                if (index % 1000 == 0) {
                    table.put(putList);
                    putList.clear();
                }
            }
            table.put(putList);
            logger.info("=====插入数据,表名:[" + tableName + "],批量数据插入成功!");
        } finally {
            if (table != null) {
                table.close();
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }
    }

    public static void deleteDataByRowKey(String tableName, String rowKey) throws Exception {
        Connection con = null;
        Table table = null;
        try {
            // ============获取连接管理==================
            con = HbasePool.getConnection();
            table = con.getTable(TableName.valueOf(tableName));

            // 按rowkey删除
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        } finally {
            if (table != null) {
                table.close();
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }

        logger.info("=====删除数据,表名:[" + tableName + "],rowKey:[" + rowKey + "],删除成功!");
    }

    public static void deleteDataByFamily(String tableName, String rowKey, String family) throws Exception {
        Connection con = null;
        Table table = null;
        try {
            // ============获取连接管理==================
            con = HbasePool.getConnection();
            table = con.getTable(TableName.valueOf(tableName));

            // 按family删除
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addFamily(Bytes.toBytes(family));
            table.delete(delete);
        } finally {
            if (table != null) {
                table.close();
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }

        logger.info("=====删除数据,表名:[" + tableName + "],rowKey:[" + rowKey + "],family:[" + family + "],删除成功!");
    }

    public static void deleteDataByColumn(String tableName, String rowKey, String family, String column)
            throws Exception {

        Connection con = null;
        Table table = null;
        try {
            // ============获取连接管理==================
            con = HbasePool.getConnection();
            table = con.getTable(TableName.valueOf(tableName));

            // 按column删除
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumns(Bytes.toBytes(family), Bytes.toBytes(column));
            table.delete(delete);
        } finally {
            if (table != null) {
                table.close();
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }
        logger.info("=====删除数据,表名:[" + tableName + "],rowKey:[" + rowKey + "],family:[" + family + "],column:[" + column
                + "],删除成功!");
    }

    /**
     * @Description 查询数据(最新一条)
     * @param tableName String 表名
     * @param rowKey String 行键
     * @return
     * @throws Exception
     */
    public static Map<String, Object> queryData(String tableName, String rowKey) throws Exception {
        Connection con = null;
        Table table = null;
        try {
            // ============获取连接管理==================
            con = HbasePool.getConnection();
            table = con.getTable(TableName.valueOf(tableName));

            // ============进行GET操作==================
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);

            Map<String, Object> map = analysisQueryResult(result);

            // ============封装返回结果===============
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", map.values());
            resultMap.put("totalCount", map.values().size());
            return resultMap;
        } finally {
            if (table != null) {
                table.close();
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }
    }

    /**
     * @Description 根据条件查询数据
     * @param tableName String 表名
     * @param rowKey String 行键
     * @param version int 版本数量(查多少条数据)
     * @param paramMap key-String-family(列族) value-String[]-columns(列名)
     * @return
     * @throws Exception
     */
    public static Map<String, Object> queryDataByParams(String tableName, String rowKey, int version,
            Map<String, String[]> paramMap) throws Exception {
        Connection con = null;
        Table table = null;
        try {
            // ============获取连接管理==================
            con = HbasePool.getConnection();
            table = con.getTable(TableName.valueOf(tableName));

            // ============进行GET操作==================
            Get get = new Get(Bytes.toBytes(rowKey));
            get.readVersions(version);

            // ============组装条件==================
            assembleCondition(paramMap, get);
            Result result = table.get(get);
            Map<String, Object> map = analysisQueryResult(result);

            // ============封装返回结果===============
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", map.values());
            resultMap.put("totalCount", map.values().size());
            return resultMap;
        } finally {
            if (table != null) {
                table.close();
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }
    }

    /**
     * @Description 根据条件查询数据(根据时间进行限定)
     * @param tableName String 表名
     * @param rowKey String 行键
     * @param version int 版本数量(查多少条数据)
     * @param startTime long 开始时间戳 如果仅传开始时间戳,结束时间戳传0,则查开始时间戳对应的版本
     * @param endTime long 结束时间戳
     * @param paramMap key-String-family(列族) value-String[]-columns(列名)
     * @return
     * @throws Exception
     */
    public static Map<String, Object> queryDataByParamsAndTime(String tableName, String rowKey, int version,
            long startTime, long endTime, Map<String, String[]> paramMap) throws Exception {
        Connection con = null;
        Table table = null;
        try {
            // ============获取连接管理==================
            con = HbasePool.getConnection();
            table = con.getTable(TableName.valueOf(tableName));

            // ============进行GET操作==================
            Get get = new Get(Bytes.toBytes(rowKey));
            get.readVersions(version);
            if (endTime != 0) {
                get.setTimeRange(startTime, endTime);
            } else if (startTime != 0) {
                get.setTimeStamp(startTime);
            }
            // ============组装条件==================
            assembleCondition(paramMap, get);
            Result result = table.get(get);
            Map<String, Object> map = analysisQueryResult(result);

            // ============封装返回结果===============
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", map.values());
            resultMap.put("totalCount", map.values().size());
            return resultMap;
        } finally {
            if (table != null) {
                table.close();
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }
    }

    /**
     * @Description 装载条件
     * @param paramMap
     * @param get
     */
    private static void assembleCondition(Map<String, String[]> paramMap, Get get) {
        if (paramMap != null && !paramMap.isEmpty()) {
            for (Entry<String, String[]> entry : paramMap.entrySet()) {
                if (entry.getValue() == null) {
                    get.addFamily(Bytes.toBytes(entry.getKey()));
                } else {
                    for (String column : entry.getValue()) {
                        get.addColumn(Bytes.toBytes(entry.getKey()), Bytes.toBytes(column));
                    }
                }
            }
        }
    }

    /**
     * @Description 解析Result
     * @param result
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> analysisQueryResult(Result result) {
        Map<String, Object> map = new HashMap<>();
        for (Cell cell : result.listCells()) {
            Long timeStamp = cell.getTimestamp();
            String rowKey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
            String familyRs = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
            String keyRs =
                    Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            String valueRs = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());

            // =========参数封装=========
            Map<String, Object> map2 = (Map<String, Object>) map.get(timeStamp.toString());
            if (map2 == null) {
                map2 = new HashMap<>();
            }
            Map<String, Object> map3 = (Map<String, Object>) map2.get(familyRs);
            if (map3 == null) {
                map3 = new HashMap<>();
            }
            map3.put(keyRs, valueRs);
            map2.put(familyRs, map3);
            map2.put("rowKey", rowKey);
            map2.put("timeStamp", timeStamp.toString());
            map.put(timeStamp.toString(), map2);
        }
        return map;
    }

    /**
     * @Description 根据rowkey起始范围,获取数据
     * @param tableName
     * @param startRow
     * @param endRow
     * @return
     * @throws Exception
     */
    public static Map<String, Object> scanDataByRowRange(String tableName, String startRow, String endRow)
            throws Exception {
        Connection con = null;
        Table table = null;
        try {
            // ============获取连接管理==================
            con = HbasePool.getConnection();
            table = con.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            // 按rowkey字典序扫描
            if (StringUtils.isNoneBlank(startRow)) {
                scan.withStartRow(Bytes.toBytes(startRow));
            }
            if (StringUtils.isNoneBlank(endRow)) {
                scan.withStopRow(Bytes.toBytes(endRow));
            }
            ResultScanner scanner = table.getScanner(scan);

            List<Object> list = new ArrayList<>();
            for (Result result : scanner) {
                list.add(analysisScanResult(result));
            }
            // ============封装返回结果===============
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", list);
            resultMap.put("totalCount", list.size());
            return resultMap;
        } finally {
            if (table != null) {
                table.close();
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }
    }

    /**
     * @Description 根据正则匹配rowKey获取数据
     * @param tableName
     * @param regex
     * @return
     * @throws Exception
     */
    public static Map<String, Object> scanDataByRegex(String tableName, String regex) throws Exception {
        Connection con = null;
        Table table = null;
        try {
            // ============获取连接管理==================
            con = HbasePool.getConnection();
            table = con.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            RowFilter rowFilter = new RowFilter(CompareOperator.EQUAL, new RegexStringComparator(regex));
            scan.setFilter(rowFilter);
            ResultScanner scanner = table.getScanner(scan);

            List<Object> list = new ArrayList<>();
            for (Result result : scanner) {
                list.add(analysisScanResult(result));
            }
            // ============封装返回结果===============
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", list);
            resultMap.put("totalCount", list.size());
            return resultMap;
        } finally {
            if (table != null) {
                table.close();
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }
    }

    /**
     * @Description 根据正则统计匹配rowkey数量
     * @param tableName
     * @param regex
     * @return
     * @throws Exception
     */
    public static Map<String, Object> scanDataCountByRegex(String tableName, String regex) throws Exception {
        Connection con = null;
        Table table = null;
        try {
            // ============获取连接管理==================
            con = HbasePool.getConnection();
            table = con.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            RowFilter rowFilter = new RowFilter(CompareOperator.EQUAL, new RegexStringComparator(regex));
            scan.setFilter(rowFilter);
            scan.setFilter(new FirstKeyOnlyFilter());
            scan.setCaching(500);
            scan.setCacheBlocks(false);
            ResultScanner scanner = table.getScanner(scan);

            int totalCount = 0;
            while (scanner.next() != null) {
                totalCount++;
            }
            // ============封装返回结果===============
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("totalCount", totalCount);
            return resultMap;
        } finally {
            if (table != null) {
                table.close();
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }
    }

    /**
     * @Description 根据正则匹配rowkey限制大小查询
     * @param tableName
     * @param regex
     * @param pageSize
     * @return
     * @throws Exception
     */
    public static Map<String, Object> scanDataByRegexAndPage(String tableName, String regex, long pageSize)
            throws Exception {
        Connection con = null;
        Table table = null;
        try {
            // ============获取连接管理==================
            con = HbasePool.getConnection();
            table = con.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.setFilter(new PageFilter(pageSize));
            scan.setFilter(new RowFilter(CompareOperator.EQUAL, new RegexStringComparator(regex)));
            ResultScanner scanner = table.getScanner(scan);

            List<Object> list = new ArrayList<>();
            for (Result result : scanner) {
                list.add(analysisScanResult(result));
            }
            // ============封装返回结果===============
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", list);
            resultMap.put("totalCount", list.size());
            return resultMap;
        } finally {
            if (table != null) {
                table.close();
            }
            if (con != null) {
                HbasePool.returnConnect(con);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> analysisScanResult(Result result) {
        Map<String, Object> map = new HashMap<>();
        for (Cell cell : result.listCells()) {
            Long timeStamp = cell.getTimestamp();
            String rowKey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
            String familyRs = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
            String keyRs =
                    Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            String valueRs = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());

            // =========参数封装=========
            Map<String, Object> map2 = (Map<String, Object>) map.get(familyRs);
            if (map2 == null) {
                map2 = new HashMap<>();
            }
            map2.put(keyRs, valueRs);
            map.put(familyRs, map2);
            map.put("rowKey", rowKey);
            map.put("timeStamp", timeStamp.toString());
        }
        return map;
    }
}
