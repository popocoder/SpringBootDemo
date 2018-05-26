package bootdemo.demo.controll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import bootdemo.demo.bean.Egg;
import bootdemo.demo.bean.Person;

//控制层
@Controller
public class HelloController {
    
    @Autowired
    private Person person;
    @Autowired
    private Egg egg;
    
    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        System.out.println(egg);
        
        return "Hello World!";
        
    }
}
