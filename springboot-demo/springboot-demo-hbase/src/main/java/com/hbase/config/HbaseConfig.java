package com.hbase.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hbase")
public class HbaseConfig {

    private static Configuration conf = null;

    private static String zookeeperQuorum;

    private static String zookeeperPropertyClientPort;

    private static String zookeeperZnodeParent;

    private static String clientPause;

    private static String clientRetriesNumber;

    private static String rpcTimeout;

    private static String clientOperationTimeout;

    private static String clientScannerTimeoutPeriod;

    /**
     * @Description 获取Hbase配置对象 Configuration
     * @return
     */
    public static synchronized Configuration getConfiguration() {
        if (conf == null) {
            conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", zookeeperQuorum);
            conf.set("hbase.zookeeper.property.clientPort", zookeeperPropertyClientPort);
            conf.set("hbase.zookeeper.znode.parent", zookeeperZnodeParent);
            conf.set("hbase.client.pause", clientPause);
            conf.set("hbase.client.retries.number", clientRetriesNumber);
            conf.set("hbase.rpc.timeout", rpcTimeout);
            conf.set("hbase.client.operation.timeout", clientOperationTimeout);
            conf.set("hbase.client.scanner.timeout.period", clientScannerTimeoutPeriod);
        }
        return conf;
    }
    
//    private static A a =  new A();
//    
//    public static A getA(){
//        return a;
//    }
    
    private static A a;
    
    static{
        a = new A();
    }
    
    public static A getA() {
        return a;
    }    

    public void setZookeeperQuorum(String zookeeperQuorum) {
        HbaseConfig.zookeeperQuorum = zookeeperQuorum;
    }

    public void setZookeeperPropertyClientPort(String zookeeperPropertyClientPort) {
        HbaseConfig.zookeeperPropertyClientPort = zookeeperPropertyClientPort;
    }

    public void setZookeeperZnodeParent(String zookeeperZnodeParent) {
        HbaseConfig.zookeeperZnodeParent = zookeeperZnodeParent;
    }

    public void setClientPause(String clientPause) {
        HbaseConfig.clientPause = clientPause;
    }

    public void setClientRetriesNumber(String clientRetriesNumber) {
        HbaseConfig.clientRetriesNumber = clientRetriesNumber;
    }

    public void setRpcTimeout(String rpcTimeout) {
        HbaseConfig.rpcTimeout = rpcTimeout;
    }

    public void setClientOperationTimeout(String clientOperationTimeout) {
        HbaseConfig.clientOperationTimeout = clientOperationTimeout;
    }

    public void setClientScannerTimeoutPeriod(String clientScannerTimeoutPeriod) {
        HbaseConfig.clientScannerTimeoutPeriod = clientScannerTimeoutPeriod;
    }

}
