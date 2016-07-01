package com.workfront.internship.event_management;

import com.workfront.internship.event_management.datasource.UserDAO;
import com.workfront.internship.event_management.datasource.UserDAOImpl;
import com.workfront.internship.event_management.model.User;

import java.sql.*;

/**
 * Created by Hermine Turshujyan on 6/29/16.
 */
public class Test {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAOImpl();
        userDAO.getAllUsers();

        User user = new User();
        user.setFirstName("Anna")
                .setLastName("Asmangulyan")
                .setUsername("asmangulyan")
                .setPassword("password")
                .setEmail("aa@gmail.com");

        userDAO.insertUser(user);

    }


}
