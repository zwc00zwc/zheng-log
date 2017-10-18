package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by alan.zheng on 2017/10/11.
 */
@SpringBootApplication
@ComponentScan(basePackages = "zheng.log.core,application")
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }
}
