package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.User;

import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public interface UserDAO {
    boolean insertUser(User user);

    List<User> getAllUsers();

    User getUserById(int userId);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    boolean setVerified(int userId);

    boolean updateUser(User user);

    boolean deleteUser(int userId);
}
