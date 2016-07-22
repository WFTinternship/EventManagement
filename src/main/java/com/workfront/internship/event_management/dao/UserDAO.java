package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.DataAccessException;
import com.workfront.internship.event_management.exception.DuplicateEntryException;
import com.workfront.internship.event_management.exception.OperationFailedException;
import com.workfront.internship.event_management.model.User;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public interface UserDAO {

    //insert record into db
    int addUser(User user) throws OperationFailedException;

    //read data from db
    List<User> getAllUsers() throws OperationFailedException;

    User getUserById(int userId) throws DataAccessException;

    User getUserByEmail(String email) throws DataAccessException;

    //update record in db
    boolean updateVerifiedStatus(int userId) throws DataAccessException;

    boolean updateUser(User user) throws DataAccessException, DuplicateEntryException;

    //delete record from db
    boolean deleteUser(int userId) throws DataAccessException;

    boolean deleteAllUsers() throws DataAccessException;
}
