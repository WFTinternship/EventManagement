package com.workfront.internship.event_management.spring;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

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

        BasicDataSource dataSource = new BasicDataSource();

        //loading db property list
        Properties props = new Properties();
        props.load(DevApplicationConfig.class.getClassLoader().getResourceAsStream("db.properties"));

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        dataSource.setUsername(props.getProperty("jdbc.username"));
        dataSource.setPassword(props.getProperty("jdbc.password"));
        dataSource.setUrl(props.getProperty("jdbc.url"));

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
        VelocityEngineFactory factory = new VelocityEngineFactory();
        Properties props = new Properties();
        props.put("resource.loader", "class");
        props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        factory.setVelocityProperties(props);

        return factory.createVelocityEngine();
    }

    @Bean
    public JavaMailSenderImpl javaMailSenderImpl() throws IOException {

        //load smtp properties
        ClassLoader classLoader = DevApplicationConfig.class.getClassLoader();
        Properties properties = new Properties();
        properties.load(classLoader.getResourceAsStream("mail.smtp.properties"));

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        //set smtp properties
        mailSender.setJavaMailProperties(properties);
        mailSender.setHost(properties.getProperty("mail.smtp.host"));
        mailSender.setPort(Integer.parseInt(properties.getProperty("mail.smtp.port")));
        mailSender.setProtocol(properties.getProperty("mail.transport.protocol"));

        //Set gmail and password
        mailSender.setUsername(properties.getProperty("user.email"));
        mailSender.setPassword(properties.getProperty("user.password"));

        return mailSender;
    }


}
