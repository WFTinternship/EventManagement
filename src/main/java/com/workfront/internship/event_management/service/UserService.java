package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.OperationFailedException;
import com.workfront.internship.event_management.model.User;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
interface UserService {

    //CRUD operations with user
    int addAccount(User user) throws OperationFailedException;

    boolean editProfile(User user) throws OperationFailedException;

    boolean verifyAccount(int userId) throws OperationFailedException;

    User login(String email, String password) throws OperationFailedException;

    boolean deleteAccount(int userId) throws OperationFailedException;

    User getUserById(int userId) throws OperationFailedException;

    User getUserByEmail(String email) throws OperationFailedException;

    //operations with all users
    List<User> getAllUsers() throws OperationFailedException;

    boolean deleteAllUsers() throws OperationFailedException;
}
