package com.workfront.internship.event_management.dao;


import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class DataSourceManager {

    private static DataSourceManager dsManager;
    private BasicDataSource ds;

    private DataSourceManager() throws IOException, SQLException {

        //loading db property list
        Properties props = new Properties();
        props.load(DataSourceManager.class.getClassLoader().getResourceAsStream("config.properties"));

        ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");

        ds.setUsername(props.getProperty("jdbc.username"));
        ds.setPassword(props.getProperty("jdbc.password"));
        ds.setUrl(props.getProperty("jdbc.url"));
    }

    public static DataSourceManager getInstance() throws IOException, SQLException {
        if (dsManager == null) {
            return new DataSourceManager();
        }
        return dsManager;
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }

}