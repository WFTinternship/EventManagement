package com.workfront.internship.event_management;

import com.workfront.internship.event_management.datasource.UserDAO;
import com.workfront.internship.event_management.datasource.UserDAOImpl;

import java.sql.*;

/**
 * Created by Hermine Turshujyan on 6/29/16.
 */
public class Test {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAOImpl();
        userDAO.getAllUsers();
    }


}
