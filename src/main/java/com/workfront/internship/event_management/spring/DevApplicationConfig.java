package com.workfront.internship.event_management.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.sql.DataSource;
import java.io.IOException;
/**
 * Created by Hermine Turshujyan 8/29/16.
 */
@Configuration
@ComponentScan(basePackages = "com.workfront.internship.event_management")
@Profile("Development")
public class DevApplicationConfig {

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000);
        return new CommonsMultipartResolver();
    }

    @Bean
    public DataSource dataSource() throws IOException {

//        BasicDataSource dataSource = new BasicDataSource();
//
//        //loading db property list
//        Properties props = new Properties();
//        props.load(DevApplicationConfig.class.getClassLoader().getResourceAsStream("config.properties"));
//
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//
//        dataSource.setUsername(props.getProperty("jdbc.username"));
//        dataSource.setPassword(props.getProperty("jdbc.password"));
//        dataSource.setUrl(props.getProperty("jdbc.url"));
//
//        return dataSource;

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/schema.sql")
                .addScript("db/data.sql")
                .build();
        return db;
    }

//    @Bean
//    public JdbcTemplate getJdbcTemplate() throws IOException {
//        return new JdbcTemplate(dataSource());
//    }

}
