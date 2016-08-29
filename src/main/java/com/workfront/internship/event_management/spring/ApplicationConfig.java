package com.workfront.internship.event_management.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Hermine Turshujyan 8/19/16.
 */

@Configuration
@ComponentScan(basePackages = "com.workfront.internship.event_management")
public class ApplicationConfig {
/*
    @Bean
    public DataSource getDevelopmentDataSource() throws IOException {

        BasicDataSource dataSource = new BasicDataSource();

        //loading db property list
        Properties props = new Properties();
        props.load(DevApplicationConfig.class.getClassLoader().getResourceAsStream("config.properties"));

        dataSource.setDriverClassName(props.getProperty("jdbc.driver"));

        dataSource.setUsername(props.getProperty("jdbc.username"));
        dataSource.setPassword(props.getProperty("jdbc.password"));
        dataSource.setUrl(props.getProperty("jdbc.url"));

        return dataSource;
    }*/

}