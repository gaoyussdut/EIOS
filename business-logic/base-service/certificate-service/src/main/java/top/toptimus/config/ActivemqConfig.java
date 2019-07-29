package top.toptimus.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Activemq配置
 * Created by JiangHao on 2018/10/30.
 */
@Configuration
public class ActivemqConfig {

    // 信任的包
    private static final String TRUST_PACKAGE = "top.toptimus,java.util.Map";
    // Activemq的URL
    @Value("${spring.activemq.broker-url}")
    private String BROKER_URL;

    /**
     * 将包内的类设置可信任的  否则不会消费
     */
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        // 实际上线时需要将信任的包配置好  否则不会被消费
        factory.setTrustedPackages(Arrays.asList(TRUST_PACKAGE.split(",")));
        // 为测试方便将所有包设置为可信任
        factory.setTrustAllPackages(true);
        return factory;
    }
}
