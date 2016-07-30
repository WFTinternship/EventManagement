package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */

public class GenericDAO {

    static final Logger LOGGER = Logger.getLogger(GenericDAO.class);
    private DataSourceManager dataSourceManager;

    GenericDAO(DataSourceManager dataSourceManager) throws Exception {
        this.dataSourceManager = dataSourceManager;
    }

    GenericDAO() throws DAOException {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Could not instantiate data source manager", e);
            throw new DAOException("Could not instantiate data source manager", e);
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
            LOGGER.error(e);
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e);
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

    void deleteRecordById(String tableName, int id) throws DAOException, ObjectNotFoundException {
        deleteRecord(tableName, "id", id);
    }

    void deleteAllRecords(String tableName) throws DAOException {

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
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    void deleteRecord(String tableName, String columnName, Object columnValue) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "DELETE FROM " + tableName + " WHERE " + columnName + " = ?";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setObject(1, columnValue);

            //execute query
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
    }

}
