package top.toptimus.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by JiangHao on 2018/9/18.
 */
@Configuration
public class ElasticSearchConfig {
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}