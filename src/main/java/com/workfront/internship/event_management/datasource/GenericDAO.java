package com.workfront.internship.event_management.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    protected void closeResources(Statement stmt, Connection conn) {
        closeResources(null, stmt, conn);
    }

    protected void closeResources(Connection conn) {
        closeResources(null, conn);
    }

    protected void closeResources(Statement stmt) {
        closeResources(null, stmt, null);
    }
    protected void closeResources(ResultSet rs) {
        closeResources(rs, null, null);
    }


    protected void closeResources(ResultSet rs, Statement stmt) {
        closeResources(rs, stmt, null);
    }

    protected int getInsertedId(Statement stmt) throws  SQLException{

        int id = 0;
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

//
//    public boolean deleteEntryById(String tableName, int id) {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        int affectedRows = 0;
//        try {
//            conn = DataSourceManager.getInstance().getConnection();
//            String sqlStr = "DELETE FROM "+ tableName + " WHERE id = ?";
//            stmt = conn.prepareStatement(sqlStr);
//            stmt.setInt(1, id);
//            affectedRows = stmt.executeUpdate();
//        } catch (SQLException | IOException | PropertyVetoException e) {
//            e.printStackTrace();
//        } finally {
//            closeResources(stmt, conn);
//        }
//        return affectedRows != 0;
//    }

}
