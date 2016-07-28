package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.User;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
interface UserService {

    User addAccount(User user);

    void editProfile(User user);

    void verifyAccount(int userId);

    User login(String email, String password);

    User getUserById(int userId);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    void deleteAccount(int userId);

    void deleteAllUsers();
}
