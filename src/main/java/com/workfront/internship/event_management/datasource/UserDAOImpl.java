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
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM user";
            stmt = conn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            usersList = createUsersListFromRS(rs);
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
        return getUserByField("id", userId);
    }

    public User getUserByUsername(String username) {
        return getUserByField("username", username);
    }

    public User getUserByEmail(String email) {
        return getUserByField("email", email);
    }

    public void updateUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "UPDATE user SET first_name = ?, last_name = ?, username = ?, password = ?, " +
                    "email = ?, phone_number = ?, avatar_path = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getPhoneNumber());
            preparedStatement.setString(7, user.getAvatarPath());
            preparedStatement.setInt(8, user.getId());
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
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO user "
                    + "(first_name, last_name, username, password, email, phone_number, avatar_path) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ? )";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getPhoneNumber());
            preparedStatement.setString(7, user.getAvatarPath());
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
        deleteRecordById("user", userId);
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        try {
//            conn = DataSourceManager.getInstance().getConnection();
//            String sqlStr = "DELETE FROM user WHERE id = ?";
//            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
//            preparedStatement.setInt(1, userId);
//            preparedStatement.executeUpdate();
//        } catch (IOException e) {
//            System.out.println("IOException " + e.getMessage());
//        } catch (SQLException e) {
//            System.out.println("SQLException " + e.getMessage());
//        } finally {
//            closeResources(rs, stmt, conn);
//        }
    }

    //helper methods
    private User getUserByField(String columnName, Object columnValue) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = new User();
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM user WHERE ? = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, columnName);
            stmt.setObject(2, columnValue);
            rs = stmt.executeQuery();
            user = createUserFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
        return user;
    }

    private User createUserFromRS(ResultSet rs) throws SQLException {
        User user = new User();
        while (rs.next()) {
              user.setId(rs.getInt("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setUsername(rs.getString("username"))
                    .setEmail(rs.getString("email"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"))
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setVerified(rs.getBoolean("is_verified"));
        }
        return user;
    }

    private List<User> createUsersListFromRS(ResultSet rs) throws SQLException {
        List<User> usersList = new ArrayList<User>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setUsername(rs.getString("username"))
                    .setEmail(rs.getString("email"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"))
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setVerified(rs.getBoolean("is_verified"));
            usersList.add(user);
        }
        return usersList ;
    }
}
