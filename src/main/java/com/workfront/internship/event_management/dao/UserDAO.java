package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.User;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public interface UserDAO {

    //insert record into db
    int addUser(User user) throws DAOException, DuplicateEntryException;

    //read data from db
    List<User> getAllUsers() throws DAOException;

    List<User> getUsersMatchingEmail(String email) throws DAOException;

    User getUserById(int userId) throws DAOException, ObjectNotFoundException;

    User getUserByEmail(String email) throws DAOException;

    //update record in db
    boolean updateVerifiedStatus(int userId) throws ObjectNotFoundException, DAOException;

    boolean updateUser(User user) throws ObjectNotFoundException, DAOException, DuplicateEntryException;

    //delete record from db
    boolean deleteUser(int userId) throws DAOException, ObjectNotFoundException;

    void deleteAllUsers() throws DAOException;
}
