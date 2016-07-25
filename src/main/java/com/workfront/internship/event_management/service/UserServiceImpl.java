package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.exception.DuplicateEntryException;
import com.workfront.internship.event_management.exception.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.util.HashGeneratorUtil;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/20/16.
 */
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    public UserServiceImpl() throws OperationFailedException {
        try {
            userDAO = new UserDAOImpl();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
    }

    @Override
    public boolean addAccount(User user) throws OperationFailedException {

        boolean success = false;

        //check if user object is valid
        if (isValid(user)) {

            //encrypt user password
            String encryptedPassword = HashGeneratorUtil.generateHashString(user.getPassword());
            user.setPassword(encryptedPassword);

            try {
                //check if user with this email already exists
                User existingUser = userDAO.getUserByEmail(user.getEmail());

                if (existingUser == null) {
                    int userId = userDAO.addUser(user);
                    if (userId != 0) {
                        success = true;
                    }
                } else {
                    throw new OperationFailedException("User with this email already exists!");
                }
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        }

        EmailService emailService = new EmailServiceImpl();
        emailService.sendVerificationEmail(user);
        // TODO: 7/25/16 send verification email 
        return success;
    }

    @Override
    public boolean editProfile(User user) throws OperationFailedException {

        boolean success = false;

        if (isValid(user)) {
            try {
                success = userDAO.updateUser(user);
               /* if(!success) {
                    throw new OperationFailedException("Non existing user.");
                }*/
            } catch (DuplicateEntryException e) {
                throw new OperationFailedException("User with this email already exists!");
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        }

        return success;
    }

    @Override
    public boolean verifyAccount(int userId) throws OperationFailedException {

        boolean success = false;

        if (userId > 0) {
            try {
                success = userDAO.updateVerifiedStatus(userId);
              /*  if(!success) {
                    throw new OperationFailedException("Non existing user.");
                }*/
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        }
        return success;
    }

    @Override
    public boolean deleteAccount(int userId) throws OperationFailedException {

        boolean success = false;
        if (userId > 0) {
            try {
                success = userDAO.deleteUser(userId);
            /*  if(!success) {
                    throw new OperationFailedException("Non existing user.");
                }*/
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        }
        return success;
    }

    @Override
    public User login(String email, String password) throws OperationFailedException {

        User validUser = null;

        try {
            User user = userDAO.getUserByEmail(email);
            if (user != null && user.getPassword().equals(HashGeneratorUtil.generateHashString(password))) {
                validUser = user;
            }
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }

        return validUser;
    }

    @Override
    public User getUserById(int userId) throws OperationFailedException {

        User user;
        try {
            user = userDAO.getUserById(userId);
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }

        return user;
    }

    @Override
    public User getUserByEmail(String email) throws OperationFailedException {

        User user;

        try {
            user = userDAO.getUserByEmail(email);
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() throws OperationFailedException {

        List<User> userList;

        try {
            userList = userDAO.getAllUsers();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
        return userList;
    }

    @Override
    public boolean deleteAllUsers() throws OperationFailedException {

        boolean success;

        try {
            success = userDAO.deleteAllUsers();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
        return success;
    }


    //helper methods
    private boolean isValid(User user) {

        boolean valid = false;

        if (user != null) {
            if (user.getFirstName() != null && user.getLastName() != null
                    && user.getPassword() != null && user.getEmail() != null) {
                valid = true;
            }
        }
        return valid;
    }
}
