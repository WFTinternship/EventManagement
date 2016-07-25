package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.exception.DuplicateEntryException;
import com.workfront.internship.event_management.model.User;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public interface UserDAO {

    //insert record into db
    int addUser(User user) throws DAOException;

    //read data from db
    List<User> getAllUsers() throws DAOException;

    User getUserById(int userId) throws DAOException;

    User getUserByEmail(String email) throws DAOException;

    //update record in db
    boolean updateVerifiedStatus(int userId) throws DAOException;

    boolean updateUser(User user) throws DAOException;

    //delete record from db
    boolean deleteUser(int userId) throws DAOException;

    boolean deleteAllUsers() throws DAOException;
}
