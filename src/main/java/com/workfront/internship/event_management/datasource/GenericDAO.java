package com.workfront.internship.event_management.datasource;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;

/**
 * Created by hermine on 7/1/16.
 */

class GenericDAO {

    protected static final Logger logger = Logger.getLogger(GenericDAO.class);
    // TODO: read more about log4j framework

     void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if(rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        try {
            if(stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }

        try {
            if(conn != null) {
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
        closeResources(null, stmt, null);
    }

     void closeResources(ResultSet rs) {
        closeResources(rs, null, null);
    }

     void closeResources(ResultSet rs, Statement stmt) {
        closeResources(rs, stmt, null);
    }

     int getInsertedId(Statement stmt) throws  SQLException{

        int id = 0;
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

     boolean deleteRecordById(String tableName, int id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sqlStr = "DELETE FROM "+ tableName + " WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, id);

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (IOException | SQLException e) {
            logger.error("Exception ", e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

     boolean deleteAllRecords(String tableName) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create statement
            String sqlStr = "DELETE FROM "+ tableName;
            stmt = conn.prepareStatement(sqlStr);

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (IOException | SQLException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }


}
