package com.workfront.internship.event_management.datasource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;

/**
 * Created by hermine on 7/1/16.
 */
public class GenericDAO {

    protected void closeResources(ResultSet rs, Statement stmt, Connection conn) {

        try {
            if(rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
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

    public boolean deleteEntryById(String tableName, int columnId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM "+ tableName + " WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, columnId);
            affectedRows = preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return affectedRows != 0;
    }

}
