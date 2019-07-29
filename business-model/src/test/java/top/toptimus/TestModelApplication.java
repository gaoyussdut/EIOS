package top.toptimus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TestModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestModelApplication.class, args);
    }
}


