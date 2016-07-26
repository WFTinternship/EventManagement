package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.GenericDAO;
import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.exception.DuplicateEntryException;
import com.workfront.internship.event_management.exception.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.util.HashGeneratorUtil;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/20/16.
 */
public class UserServiceImpl implements UserService {

    static final Logger LOGGER = Logger.getLogger(GenericDAO.class);
    private UserDAO userDAO;

    public UserServiceImpl() throws OperationFailedException {
        try {
            userDAO = new UserDAOImpl();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
    }

    @Override
    public int addAccount(User user) throws OperationFailedException {

        int userId = 0;

        //check if user object is valid
        if (isValidUser(user)) {

            //encrypt user password
            String encryptedPassword = HashGeneratorUtil.generateHashString(user.getPassword());
            user.setPassword(encryptedPassword);

            try {
                //check if user with this email already exists
                User existingUser = userDAO.getUserByEmail(user.getEmail());

                if (existingUser == null) {
                    userId = userDAO.addUser(user);
                } else {
                    // TODO: 7/26/16 check
                    // TODO: 7/26/16 add logs 
                    throw new OperationFailedException("User with this email already exists!");

                }
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }


            EmailService emailService = new EmailServiceImpl();
            emailService.sendVerificationEmail(user);
            // TODO: 7/25/16 send verification email
        }
        return userId;
    }

    @Override
    public boolean editProfile(User user) throws OperationFailedException {

        boolean success = false;

        if (isValidUser(user)) {
            try {
                success = userDAO.updateUser(user);
                if (!success) {
                    throw new OperationFailedException("Non existing user.");
                }
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
                if (!success) {
                    throw new OperationFailedException("Non existing user.");
                }
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
                if (!success) {
                    throw new OperationFailedException("Non existing user.");
                }
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        }
        return success;
    }

    @Override
    public User login(String email, String password) throws OperationFailedException {

        User validUser;

        //check if email structure is valid
        if (isValidEmailAddress(email)) {
            try {
                User user = userDAO.getUserByEmail(email);
                if (user != null && user.getPassword().equals(HashGeneratorUtil.generateHashString(password))) {
                    validUser = user;
                } else {
                    throw new OperationFailedException("Invalid email/password combination!");
                }
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        } else {
            throw new OperationFailedException("Invalid email!");
        }

        return validUser;
    }

    @Override
    public User getUserById(int userId) throws OperationFailedException {

        User user;

        if (userId > 0) {
            try {
                user = userDAO.getUserById(userId);
                if (user == null) {
                    throw new OperationFailedException("Non existing user!");
                }
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        } else {
            throw new OperationFailedException("Invalid user id!");
        }

        return user;
    }

    @Override
    public User getUserByEmail(String email) throws OperationFailedException {
        User user;
        if (isValidEmailAddress(email)) {
            try {
                user = userDAO.getUserByEmail(email);
                if (user == null) {
                    throw new OperationFailedException("Non existing user.");
                }
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        } else {
            throw new OperationFailedException("Invalid email address.");
        }

        return user;
    }

    @Override
    public List<User> getAllUsers() throws OperationFailedException {

        List<User> userList;

        try {
            userList = userDAO.getAllUsers();
            if (userList == null || userList.isEmpty()) {
                throw new OperationFailedException("No users in database!");
            }
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
            if (!success) {
                throw new OperationFailedException("No users to delete!");
            }
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
        return success;
    }


    //helper methods
    private boolean isValidUser(User user) {

        boolean valid = false;

        if (user != null) {
            if (user.getFirstName() != null
                    && user.getLastName() != null
                    && user.getPassword() != null
                    && user.getEmail() != null
                    && isValidEmailAddress(user.getEmail())) {
                valid = true;
            }
        }
        return valid;
    }


    private static boolean isValidEmailAddress(String email) {
        // TODO: 7/26/16
        return false;
    }
}
