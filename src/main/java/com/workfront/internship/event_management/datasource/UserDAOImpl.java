package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.User;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public class UserDAOImpl extends GenericDAO implements UserDAO {

    //CREATE
    public boolean insertUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO user "
                    + "(first_name, last_name, username, password, email, phone_number, avatar_path) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ? )";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPhoneNumber());
            stmt.setString(7, user.getAvatarPath());
            affectedRows = stmt.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("SQLException " + e.getMessage());
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    //READ
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
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
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

    //UPDATE
    public boolean setVerified(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "UPDATE user SET verified = ? WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, userId);
            affectedRows = stmt.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    public boolean updateUser(User user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "UPDATE user SET first_name = ?, last_name = ?, username = ?, password = ?, " +
                    "email = ?, phone_number = ?, avatar_path = ? WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPhoneNumber());
            stmt.setString(7, user.getAvatarPath());
            stmt.setInt(8, user.getId());
            affectedRows = stmt.executeUpdate();
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
            System.out.println("PropertyVetoException " + e.getMessage());
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    //DELETE
    public boolean deleteUser(int userId) {
        return deleteEntryById("user", userId);
    }

    //helper methods
    private User getUserByField(String columnName, Object columnValue) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = new User();
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM user WHERE " + columnName + " = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setObject(1, columnValue);
            rs = stmt.executeQuery();
            user = createUserFromRS(rs);
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        } catch (PropertyVetoException e) {
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
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setVerified(rs.getBoolean("verified"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"));
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
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setVerified(rs.getBoolean("verified"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"));
            usersList.add(user);
        }
        return usersList;
    }
}
