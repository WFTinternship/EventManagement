package com.workfront.internship.event_management.datasource;

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

    public void deleteRecordById(String tableName, int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "DELETE ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, tableName);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

}
