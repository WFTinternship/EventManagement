package com.workfront.internship.event_management.dao;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */

class GenericDAO {

    static final Logger LOGGER = Logger.getLogger(GenericDAO.class);
    private DataSourceManager dataSourceManager;

    GenericDAO(DataSourceManager dataSourceManager) throws Exception {
        this.dataSourceManager = dataSourceManager;
    }

    GenericDAO() {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Exception...", e);
        }
    }


    void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }
    }

    void closeResources(Statement stmt, Connection conn) {
        closeResources(null, stmt, conn);
    }

    void closeResources(Connection conn) {
        closeResources(null, conn);
    }

    void closeResources(Statement stmt) {
        closeResources(stmt, null);
    }

    int getInsertedId(Statement stmt) throws SQLException {

        int id = 0;
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

    boolean deleteRecordById(String tableName, int id) {
        return deleteRecord(tableName, "id", id);
    }

    boolean deleteAllRecords(String tableName) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create statement
            String sqlStr = "DELETE FROM " + tableName;
            stmt = conn.prepareStatement(sqlStr);

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Exception...", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    boolean deleteRecord(String tableName, String columnName, int id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            String sqlStr = "DELETE FROM " + tableName + " WHERE " + columnName + " = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, id);

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Exception ", e);
            throw new RuntimeException(e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }


}
