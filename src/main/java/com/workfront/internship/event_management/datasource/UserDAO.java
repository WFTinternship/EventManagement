package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.User;

import java.util.List;

/**
 * Created by hermine on 7/1/16.
 */
public interface UserDAO {
    public List<User> getAllUsers();
    public User getUserById(int id);
    public User getUserByUsername(String username);
    public User getUserByEmail(String email);
    public void updateUser(User user);
    public void insertUser(User user);
    public void deleteUser(User user);
}
