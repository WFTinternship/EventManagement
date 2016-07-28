package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.User;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class UserDAOImpl extends GenericDAO implements UserDAO {

    static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class);
    private DataSourceManager dataSourceManager;

    public UserDAOImpl(DataSourceManager dataSourceManager) throws Exception {
        super(dataSourceManager);
        this.dataSourceManager = dataSourceManager;
    }

    public UserDAOImpl() throws DAOException {
        try {
            this.dataSourceManager = DataSourceManager.getInstance();
        } catch (IOException | SQLException e) {
            LOGGER.error("Could not instantiate data source manager", e);
            throw new DAOException("Could not instantiate data source manager", e);
        }
    }

    @Override
    public int addUser(User user) throws DAOException, DuplicateEntryException {

        Connection conn = null;
        PreparedStatement stmt = null;

        int id = 0;
        String query = "INSERT INTO user (first_name, last_name, email, password, phone_number, " +
                "avatar_path, verified, registration_date) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
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
            throw new DuplicateEntryException("User with email " + user.getEmail() + " already exists!", e);
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
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

        String query = "SELECT * FROM user";
        List<User> usersList = new ArrayList<>();

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create statement
            stmt = conn.prepareStatement(query);

            //execute query
            rs = stmt.executeQuery();

            //get results
            usersList = createUsersListFromRS(rs);
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(rs, stmt, conn);
        }
        return usersList;
    }

    @Override
    public User getUserById(int userId) throws DAOException, ObjectNotFoundException {
        return getUserByField("id", userId);
    }

    @Override
    public User getUserByEmail(String email) throws DAOException, ObjectNotFoundException {
        return getUserByField("email", email);
    }

    @Override
    public void updateVerifiedStatus(int userId) throws ObjectNotFoundException, DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;

        String query = "UPDATE user SET verified = 1 WHERE id = ?";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);

            //execute query
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ObjectNotFoundException("User with id " + userId + " not found!");
            }
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    @Override
    public void updateUser(User user) throws ObjectNotFoundException, DAOException, DuplicateEntryException {

        Connection conn = null;
        PreparedStatement stmt = null;

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
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ObjectNotFoundException("User with id " + user.getId() + " not found!");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.error("Duplicate user entry", e);
            throw new DuplicateEntryException("User with email " + user.getEmail() + " already exists!", e);
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    @Override
    public void deleteUser(int userId) throws DAOException, ObjectNotFoundException {
        deleteRecordById("user", userId);
    }

    @Override
    public void deleteAllUsers() throws DAOException {
        deleteAllRecords("user");
    }

    //helper methods
    private User getUserByField(String columnName, Object columnValue) throws ObjectNotFoundException, DAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        User user = null;
        String query = "SELECT * FROM user WHERE " + columnName + " = ?";

        try {
            //get connection
            conn = dataSourceManager.getConnection();

            //create and initialize statement
            stmt = conn.prepareStatement(query);
            stmt.setObject(1, columnValue);

            //execute query
            rs = stmt.executeQuery();

            //get results
            List<User> userList = createUsersListFromRS(rs);

            if (userList.isEmpty()) {
                throw new ObjectNotFoundException("User with " + columnName + " " + columnValue + " not found!");
            } else {
                user = userList.get(0);
            }
        } catch (SQLException e) {
            LOGGER.error("SQL exception", e);
            throw new DAOException(e);
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
