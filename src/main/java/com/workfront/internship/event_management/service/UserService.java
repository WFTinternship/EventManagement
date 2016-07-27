package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.User;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
interface UserService {

    //CRUD operations with user
    int addAccount(User user);

    boolean editProfile(User user);

    boolean verifyAccount(int userId);

    User login(String email, String password);

    boolean deleteAccount(int userId);

    User getUserById(int userId);

    User getUserByEmail(String email);

    //operations with all users
    List<User> getAllUsers();

    boolean deleteAllUsers();
}
