package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.User;

import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public interface UserDAO {

    //insert record into db
    int insertUser(User user);

    //read data from db
    List<User> getAllUsers();
    User getUserById(int userId);
    User getUserByUsername(String username);
    User getUserByEmail(String email);

    //update record in db
    boolean setVerified(int userId);
    boolean updateUser(User user);

    //delete record from db
    boolean deleteUser(int userId);
    boolean deleteAllUsers();
}
