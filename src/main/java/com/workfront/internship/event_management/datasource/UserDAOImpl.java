package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.User;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public class UserDAOImpl extends GenericDAO implements UserDAO {

    public List<User> getAllUsers() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> usersList = null;

        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "SELECT * FROM user";
            stmt = conn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            User user = new User();
            usersList = new ArrayList<User>();
            while (rs.next()) {
                user.setId(rs.getInt("user_id"))
                        .setFirstName(rs.getString("first_name"))
                        .setLastName(rs.getString("last_name"))
                        .setUsername(rs.getString("username"))
                        .setPassword(rs.getString("password"))
                        .setEmail(rs.getString("email"))
                        .setVerified(rs.getBoolean("verified"));

                String phoneNumber = rs.getString("phone_number");
                if (phoneNumber != null) {
                    user.setPhoneNumber(phoneNumber);
                }
                usersList.add(user);
            }
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }

        return usersList;
    }

    public User getUserById(int userId) {
        return getUserByField("user_id", userId );
    }

    public User getUserByUsername(String username) {
        return getUserByField("username", username );
    }

    public User getUserByEmail(String email) {
        return getUserByField("email", email );
    }

    public void updateUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "UPDATE user SET first_name = ?, last_name = ?, username = ?, password = ?, " +
                    "email = ?, phone_number = ? WHERE USER_ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword()); //should be already encrypted ?
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getPhoneNumber());
            preparedStatement.setInt(7, user.getId());
            preparedStatement.executeUpdate();

        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public void insertUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "INSERT INTO user "
                    + "(first_name, last_name, username, password, email, phone_number) VALUES "
                    + "(?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword()); //should be already encrypted ?
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getPhoneNumber());
            preparedStatement.executeUpdate();

        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    public void deleteUser(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "DELETE user WHERE user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    private User getUserByField(String columnName, Object columnValue) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = new User();

        try {
            DataSourceManager dsManager = DataSourceManager.getInstance();
            conn = dsManager.getConnection();
            String sqlStr = "SELECT * FROM user WHERE ? = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, columnName);
            stmt.setObject(1, columnValue);
            rs = stmt.executeQuery();

            if (rs.first()) {
                user.setId(rs.getInt("id"))
                        .setFirstName(rs.getString("first_name"))
                        .setLastName(rs.getString("last_name"))
                        .setUsername(rs.getString("username"))
                        .setPassword(rs.getString("password"))
                        .setEmail(rs.getString("email"))
                        .setVerified(rs.getBoolean("verified"));

                String phoneNumber = rs.getString("phone_number");
                if (phoneNumber != null) {
                    user.setPhoneNumber(phoneNumber);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return user;
    }
}
