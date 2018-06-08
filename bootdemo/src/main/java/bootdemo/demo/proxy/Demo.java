package bootdemo.demo.proxy;

import org.springframework.beans.BeanUtils;

import bootdemo.demo.redis.single.AdminRedis;
import redis.clients.jedis.Jedis;

public class Demo {
    public static void main(String[] args) {
        CglibProxy proxy = new CglibProxy();
        // 通过生成子类的方式创建代理类
        Jedis proxyImp = (Jedis) proxy.getProxy(Jedis.class);
        
        Jedis jedis = AdminRedis.getJedisPool().getResource();
        
        BeanUtils.copyProperties(jedis, proxyImp);
        
        proxyImp.get("notify2");
    }
}
