package bootdemo.demo.hbase.bean;

import java.util.List;

/**
 * @ClassName HbaseDataDTO
 * @Description Hbase数据传输
 * @author LBQ
 * @Date 2018年6月2日 上午11:36:36
 * @version 1.0.0
 */
public class HbaseDataDTO {

    /**
     * @Field @rowKey : 行键值
     */
    private String rowKey;

    /**
     * @Field @hbaseDTOList : 表结构 columnFamilys - column - value
     */
    private List<HbaseData> hbaseDataList;

    /**
     * @Field @timeStamp : 时间戳
     */
    private Long timeStamp;

    public HbaseDataDTO() {
    }

    public HbaseDataDTO(String rowKey, List<HbaseData> hbaseDataList) {
        this.rowKey = rowKey;
        this.hbaseDataList = hbaseDataList;
    }

    public HbaseDataDTO(String rowKey, Long timeStamp, List<HbaseData> hbaseDataList) {
        this.rowKey = rowKey;
        this.timeStamp = timeStamp;
        this.hbaseDataList = hbaseDataList;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public List<HbaseData> getHbaseDataList() {
        return hbaseDataList;
    }

    public void setHbaseDataList(List<HbaseData> hbaseDataList) {
        this.hbaseDataList = hbaseDataList;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "HbaseDataDTO [rowKey=" + rowKey + ", hbaseDataList=" + hbaseDataList + ", timeStamp=" + timeStamp + "]";
    }

}
