package com.workfront.internship.event_management.datasource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import java.io.IOException;
import java.sql.*;

/**
 * Created by hermine on 7/1/16.
 */
public class DataSourceManager {

    private static DataSourceManager dsManager;
    private BasicDataSource ds;

    private DataSourceManager() throws IOException, SQLException {
        ds = new BasicDataSource();
        ds.setUsername(DBConfig.USERNAME);
        ds.setPassword(DBConfig.PASSWORD);
        ds.setUrl(DBConfig.CONN_STRING);
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

    public void closeConnection() throws SQLException {
            this.ds.close();
    }
}