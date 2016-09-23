package com.workfront.internship.event_management.spring;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactory;

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
        props.load(TestApplicationConfig.class.getClassLoader().getResourceAsStream("db.properties"));

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        dataSource.setUsername(props.getProperty("jdbc.username"));
        dataSource.setPassword(props.getProperty("jdbc.password"));
        dataSource.setUrl(props.getProperty("jdbc.url.test"));

        return dataSource;

//        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//        EmbeddedDatabase db = builder
//                .setType(EmbeddedDatabaseType.H2)
//                .addScript("db/schema.sql")
//                .addScript("db/data.sql")
//                .build();
//        return db;
    }


    @Bean
    public VelocityEngine getVelocityEngine() throws VelocityException, IOException{
        return null;
    }

    @Bean
    public JavaMailSenderImpl javaMailSenderImpl() throws IOException {

        return null;
    }
}
