package top.toptimus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
@SpringBootApplication
@ComponentScan(basePackages = {"top.toptimus"})
public class BusinessModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessModelApplication.class, args);
        System.out.println("BusinessModelApplication start!");
    }

}
