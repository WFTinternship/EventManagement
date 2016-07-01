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
            String query = "select * from user";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            User user = new User();
            usersList = new ArrayList<User>();
            while (rs.next()) {
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
                usersList.add(user);
            }
        } catch (IOException e) {
            System.out.println("exception " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("exception " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }

        return usersList;
    }

    public User getUserById(int id) {
        return null;
    }

    public User getUserByUsername(String username) {
        return null;
    }

    public User getUserByEmail(String email) {
        return null;
    }

    public void updateUser(User user) {

    }

    public void insertUser(User user) {

    }

    public void deleteUser(User user) {

    }
}
