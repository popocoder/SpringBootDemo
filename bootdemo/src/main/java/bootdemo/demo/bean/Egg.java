package bootdemo.demo.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@PropertySource(value={"classpath:egg.properties"})
@ConfigurationProperties(prefix = "egg")
public class Egg {

    private String name;
    private String num;
    private String time;

    
    
    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public String getNum() {
        return num;
    }



    public void setNum(String num) {
        this.num = num;
    }



    public String getTime() {
        return time;
    }



    public void setTime(String time) {
        this.time = time;
    }



    @Override
    public String toString() {
        return "Egg [name=" + name + ", num=" + num + ", time=" + time + "]";
    }
    
}
