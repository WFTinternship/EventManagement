package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
@Component
public class GenericDAO {

    static final Logger LOGGER = Logger.getLogger(GenericDAO.class);

    @Autowired
    DataSource dataSource;

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

    boolean deleteRecordById(String tableName, int id) throws DAOException, ObjectNotFoundException {
        return deleteRecord(tableName, "id", id);
    }

    void deleteAllRecords(String tableName) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            //get connection
            conn = dataSource.getConnection();

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

    boolean deleteRecord(String tableName, String columnName, Object columnValue) throws DAOException, ObjectNotFoundException {

        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        String query = "DELETE FROM " + tableName + " WHERE " + columnName + " = ? ";

        try {
            //get connection
            conn = dataSource.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setObject(1, columnValue);

            //execute query
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                success = true;
            }

        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
        return success;
    }

}
