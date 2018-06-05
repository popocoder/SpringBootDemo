package bootdemo.demo.hbase.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HbasePO
 * @Description 表结构
 * @author LBQ
 * @Date 2018年6月2日 上午10:34:06
 * @version 1.0.0
 */
public class HbaseData {

    /**
     * @Field @columnFamily : 列族名
     */
    private String columnFamily;

    /**
     * @Field @columnValueMap : 行名 所对应值
     */
    private Map<String, String> columnValueMap;
    
    /**
     * @Field @timeStamp : 时间戳
     */
    private Long timeStamp;

    public HbaseData(String columnFamily, Map<String, String> columnValueMap) {
        this.columnFamily = columnFamily;
        this.columnValueMap = columnValueMap;
    }

    public HbaseData(String columnFamily, String mapKey, String mapValue) {
        this.columnFamily = columnFamily;
        if (this.columnValueMap == null) {
            this.columnValueMap = new HashMap<>();
        }
        this.columnValueMap.put(mapKey, mapValue);
    }

    public HbaseData() {
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public void setColumnFamily(String columnFamily) {
        this.columnFamily = columnFamily;
    }

    public Map<String, String> getColumnValueMap() {
        return columnValueMap;
    }

    public void setColumnValueMap(Map<String, String> columnValueMap) {
        this.columnValueMap = columnValueMap;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "HbaseData [columnFamily=" + columnFamily + ", columnValueMap=" + columnValueMap + ", timeStamp="
                + timeStamp + "]";
    }

}
