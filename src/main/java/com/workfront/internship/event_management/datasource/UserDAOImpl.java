package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.User;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class UserDAOImpl extends GenericDAO implements UserDAO {

    @Override
    public int insertUser(User user) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sql = "INSERT INTO user (first_name, last_name, username, password, email, phone_number, " +
                    "avatar_path, verified, registration_date) VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ? )";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPhoneNumber());
            stmt.setString(7, user.getAvatarPath());
            stmt.setBoolean(8, user.isVerified());

            if (user.getRegistrationDate() != null) {
                stmt.setTimestamp(9, new Timestamp(user.getRegistrationDate().getTime()));
            } else {
                stmt.setTimestamp(9, null);
            }

            //execute query
            stmt.executeUpdate();

            //get inserted id
            id = getInsertedId(stmt);

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
            throw new RuntimeException();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return id;
    }

    @Override
    public List<User> getAllUsers() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> usersList = null;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create statement
            String sql = "SELECT * FROM user";
            stmt = conn.prepareStatement(sql);

            //execute query
            rs = stmt.executeQuery();

            //get results
            usersList = createUsersListFromRS(rs);

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return usersList;
    }

    @Override
    public User getUserById(int userId) {
        return getUserByField("id", userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return getUserByField("username", username);
    }

    @Override
    public User getUserByEmail(String email) {
        return getUserByField("email", email);
    }

    @Override
    public boolean setVerified(int userId) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sqlStr = "UPDATE user SET verified = 1 WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, userId);

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    @Override
    public boolean updateUser(User user) {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
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

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    @Override
    public boolean deleteUser(int userId) {
        return deleteRecordById("user", userId);
    }

    @Override
    public boolean deleteAllUsers() {
        return deleteAllRecords("user");
    }

    //helper methods
    private User getUserByField(String columnName, Object columnValue) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            //get connection
            conn = DataSourceManager.getInstance().getConnection();

            //create and initialize statement
            String sqlStr = "SELECT * FROM user WHERE " + columnName + " = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setObject(1, columnValue);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<User> users = createUsersListFromRS(rs);
            if (users != null) {
                user = users.get(0);
            }

        } catch (SQLException | IOException e) {
            logger.error("Exception...", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return user;
    }

    private List<User> createUsersListFromRS(ResultSet rs) throws SQLException {

        List<User> usersList = null;

        while (rs.next()) {
            if (usersList == null) {
                usersList = new ArrayList<>();
            }
            User user = new User();
            user.setId(rs.getInt("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setUsername(rs.getString("username"))
                    .setPassword(rs.getString("password"))
                    .setEmail(rs.getString("email"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setVerified(rs.getBoolean("verified"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"));

            usersList.add(user);
        }
        return usersList;
    }
}
