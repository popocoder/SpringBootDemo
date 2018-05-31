package bootdemo.demo.hbase.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hbase")
public class HbaseConfig {

    private static Configuration conf = null;

    private static String zookeeper_quorum;

    private static String zookeeper_property_clientPort;

    private static String zookeeper_znode_parent;

    private static String client_pause;

    private static String client_retries_number;

    private static String rpc_timeout;

    private static String client_operation_timeout;

    private static String client_scanner_timeout_period;

    /**
     * @Description 获取Hbase配置对象 Configuration
     * @return
     */
    public static synchronized Configuration getConfiguration() {
        if (conf == null) {
            conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", zookeeper_quorum);
            conf.set("hbase.zookeeper.property.clientPort", zookeeper_property_clientPort);
            conf.set("hbase.zookeeper.znode.parent", zookeeper_znode_parent);
            conf.set("hbase.client.pause", client_pause);
            conf.set("hbase.client.retries.number", client_retries_number);
            conf.set("hbase.rpc.timeout", rpc_timeout);
            conf.set("hbase.client.operation.timeout", client_operation_timeout);
            conf.set("hbase.client.scanner.timeout.period", client_scanner_timeout_period);
        }
        return conf;
    }

    public void setZookeeper_quorum(String zookeeper_quorum) {
        HbaseConfig.zookeeper_quorum = zookeeper_quorum;
    }

    public void setZookeeper_property_clientPort(String zookeeper_property_clientPort) {
        HbaseConfig.zookeeper_property_clientPort = zookeeper_property_clientPort;
    }

    public void setZookeeper_znode_parent(String zookeeper_znode_parent) {
        HbaseConfig.zookeeper_znode_parent = zookeeper_znode_parent;
    }

    public void setClient_pause(String client_pause) {
        HbaseConfig.client_pause = client_pause;
    }

    public void setClient_retries_number(String client_retries_number) {
        HbaseConfig.client_retries_number = client_retries_number;
    }

    public void setRpc_timeout(String rpc_timeout) {
        HbaseConfig.rpc_timeout = rpc_timeout;
    }

    public void setClient_operation_timeout(String client_operation_timeout) {
        HbaseConfig.client_operation_timeout = client_operation_timeout;
    }

    public void setClient_scanner_timeout_period(String client_scanner_timeout_period) {
        HbaseConfig.client_scanner_timeout_period = client_scanner_timeout_period;
    }

}
