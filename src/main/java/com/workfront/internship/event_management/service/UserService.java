package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.User;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
public interface UserService {

    User addAccount(User user);

    boolean editAccount(User user);

    boolean verifyAccount(int userId);

    User login(String email, String password);

    User getUserById(int userId);

    User getUserByEmail(String email);

    List<User> getUsersMatchingEmail(String email);

    List<User> getAllUsers();

    boolean deleteAccount(int userId);

    void deleteAllUsers();
}
