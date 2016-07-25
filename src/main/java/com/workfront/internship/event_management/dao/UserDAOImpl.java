package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.exception.DuplicateEntryException;
import com.workfront.internship.event_management.model.User;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class UserDAOImpl extends GenericDAO implements UserDAO {

    private DataSourceManager dataSourceManager;

    public UserDAOImpl(DataSourceManager dataSourceManager) throws Exception {
        super(dataSourceManager);
        this.dataSourceManager = dataSourceManager;
    }

    public UserDAOImpl() throws DAOException {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Could not instantiate data source manager for UserDAO", e);
            throw new DAOException();
        }
    }


    @Override
    public int addUser(User user) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        int id = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            String sql = "INSERT INTO user (first_name, last_name, email, password, phone_number, " +
                    "avatar_path, verified, registration_date) VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getAvatarPath());
            stmt.setBoolean(7, user.isVerified());

            if (user.getRegistrationDate() != null) {
                stmt.setTimestamp(8, new Timestamp(user.getRegistrationDate().getTime()));
            } else {
                stmt.setTimestamp(8, null);
            }

            //execute query
            stmt.executeUpdate();

            //get inserted id
            id = getInsertedId(stmt);

        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate user entry", e);
            throw new DuplicateEntryException();
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException();
        } finally {
            closeResources(stmt, conn);
        }
        return id;
    }

    @Override
    public List<User> getAllUsers() throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<User> usersList = new ArrayList<>();

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create statement
            String sql = "SELECT * FROM user";
            stmt = conn.prepareStatement(sql);

            //execute query
            rs = stmt.executeQuery();

            //get results
            usersList = createUsersListFromRS(rs);

        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return usersList;
    }

    @Override
    public User getUserById(int userId) throws DAOException {
        return getUserByField("id", userId);
    }

    @Override
    public User getUserByEmail(String email) throws DAOException {
        return getUserByField("email", email);
    }

    @Override
    public boolean updateVerifiedStatus(int userId) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            String sqlStr = "UPDATE user SET verified = 1 WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, userId);

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException();
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    @Override
    public boolean updateUser(User user) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        int affectedRows = 0;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            String sqlStr = "UPDATE user SET first_name = ?, last_name = ?, email = ?, password = ?, " +
                    "phone_number = ?, avatar_path = ? WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getAvatarPath());
            stmt.setInt(7, user.getId());

            //execute query
            affectedRows = stmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate user entry", e);
            throw new DuplicateEntryException();
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException();
        } finally {
            closeResources(stmt, conn);
        }
        return affectedRows != 0;
    }

    @Override
    public boolean deleteUser(int userId) throws DAOException {
        return deleteRecordById("user", userId);
    }

    @Override
    public boolean deleteAllUsers() throws DAOException {
        return deleteAllRecords("user");
    }

    //helper methods
    private User getUserByField(String columnName, Object columnValue) throws DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            String sqlStr = "SELECT * FROM user WHERE " + columnName + " = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setObject(1, columnValue);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<User> users = createUsersListFromRS(rs);
            if (!users.isEmpty()) {
                user = users.get(0);
            }

        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return user;
    }

    private List<User> createUsersListFromRS(ResultSet rs) throws SQLException {

        List<User> usersList = new ArrayList<>();

        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
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
