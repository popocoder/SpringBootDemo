package bootdemo.demo.hbase.pool;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import bootdemo.demo.hbase.config.HbaseConfig;

/**
 * @ClassName HbasePool
 * @Description Hbase连接池
 * @author LBQ
 * @Date 2018年5月31日 下午10:22:06
 * @version 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "hbase")
public class HbasePool {

    private static Logger logger = Logger.getLogger(HbasePool.class);

    /**
     * @Field @connectNumMax : 连接池最大数
     */
    private static Integer connectNumMax;

    /**
     * @Field @connectNumMin : 连接池最小数
     */
    private static Integer connectNumMin;

    /**
     * @Field @counter : 计数器
     */
    private static Integer counter = 0;

    private static LinkedBlockingQueue<Connection> queue = new LinkedBlockingQueue<Connection>();

    /**
     * @Description 归还连接
     * @param con
     */
    public static void returnConnect(Connection con) {
        if (con == null) {
            return;
        }
        if (counter > connectNumMin) {
            try {
                con.close();
            } catch (IOException e) {
                logger.error("归还连接失败！", e);
            } finally {
                con=null;
                counter--;
            }
        } else {
            queue.add(con);
        }
    }

    /**
     * @Description 获取连接
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        if (!queue.isEmpty()) {
            return queue.poll();
        } else {
            if (counter >= connectNumMax) {
                throw new Exception("Hbase连接池已满，请释放连接资源！目前连接数已达:[" + counter + "]");
            } else {
                Connection con;
                try {
                    con = ConnectionFactory.createConnection(HbaseConfig.getConfiguration());
                    counter++;
                } catch (IOException e) {
                    throw new Exception("Hbase连接创建失败，请检查连接配置！", e);
                }
                return con;
            }
        }
    }

    public void setConnectNumMax(Integer connectNumMax) {
        HbasePool.connectNumMax = connectNumMax;
    }

    public void setConnectNumMin(Integer connectNumMin) {
        HbasePool.connectNumMin = connectNumMin;
    }

    /**
     * @Description 存活连接数
     * @return
     */
    public static Integer conNum() {
        return counter;
    }

}
