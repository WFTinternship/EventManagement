package com.workfront.internship.event_management.spring;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Hermine Turshujyan 8/29/16.
 */
@Configuration
@ComponentScan(basePackages = "com.workfront.internship.event_management")
@Profile("Test")
public class TestApplicationConfig {

    @Bean
    public DataSource getTestDataSource() throws IOException {

        BasicDataSource dataSource = new BasicDataSource();

        //loading db property list
        Properties props = new Properties();
        props.load(ApplicationConfig.class.getClassLoader().getResourceAsStream("config.properties"));

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        dataSource.setUsername(props.getProperty("jdbc.username"));
        dataSource.setPassword(props.getProperty("jdbc.password"));
        dataSource.setUrl(props.getProperty("jdbc.url.test"));

        return dataSource;
    }

    @Bean
    public Model getModel() {
        return new ExtendedModelMap();
    }

}
