package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.model.User;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public interface UserDAO {

    //insert record into db
    int addUser(User user);

    //read data from db
    List<User> getAllUsers();

    User getUserById(int userId);

    User getUserByEmail(String email);

    //update record in db
    boolean updateVerifiedStatus(int userId);

    boolean updateUser(User user);

    //delete record from db
    boolean deleteUser(int userId);

    boolean deleteAllUsers();
}
