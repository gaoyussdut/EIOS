package top.toptimus.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @program: spring-boot-example
 * @description: 数据源配置类
 * @author:
 * @create: 2018-05-03 14:35
 **/

@Configuration
public class JdbcDataSourceConfig {

    @Primary
    @Bean(name = "metaDataSourceProperties")
    @Qualifier("metaDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.meta.datasource")
    public DataSourceProperties dataSourcePropertiesFoo() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "metaDataSource")
    @Qualifier("metaDataSource")
    @ConfigurationProperties(prefix = "spring.meta.datasource")
    public DataSource fooDataSource(@Qualifier("metaDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "dataSourcePropertiesBar")
    @Qualifier("dataSourcePropertiesBar")
    @ConfigurationProperties(prefix = "spring.talend-data.datasource")
    public DataSourceProperties dataSourcePropertiesBar() {
        return new DataSourceProperties();
    }

    @Bean(name = "talendDataSource")
    @Qualifier("talendDataSource")
    @ConfigurationProperties(prefix = "spring.talend-data.datasource")
    public DataSource talendDataSource(@Qualifier("dataSourcePropertiesBar") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "metaJdbcTemplate")
    @Qualifier("metaJdbcTemplate")
    public JdbcTemplate fooJdbcTemplate(@Qualifier("metaDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "talendJdbcTemplate")
    @Qualifier("talendJdbcTemplate")
    public JdbcTemplate barJdbcTemplate(@Qualifier("talendDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}