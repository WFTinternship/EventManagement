package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.GenericDAO;
import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.exception.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.util.HashGeneratorUtil;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/20/16.
 */
public class UserServiceImpl implements UserService {

    static final Logger LOGGER = Logger.getLogger(GenericDAO.class); // TODO: 7/27/16  add logs

    private UserDAO userDAO;
    private EmailService emailService;


    public UserServiceImpl() {
        try {
            userDAO = new UserDAOImpl();
            emailService = new EmailServiceImpl();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
    }

    @Override
    public int addAccount(User user) {

        int userId = 0;

        //check if user object is valid
        if (isValidUser(user)) {

            //encrypt user password
            String encryptedPassword = HashGeneratorUtil.generateHashString(user.getPassword());
            user.setPassword(encryptedPassword);

            //insert user into db
            userId = userDAO.addUser(user);

            //if user successfully inserted
            if (userId > 0) {
                boolean success = emailService.sendVerificationEmail(user);
                if (!success) {
                    throw new OperationFailedException("Unable to send verification email!");
                }
            }
        } else {
            throw new OperationFailedException("Invalid user object!");
        }
        return userId;
    }

    @Override
    public boolean editProfile(User user) {

        boolean success;

        if (isValidUser(user)) {
            success = userDAO.updateUser(user);
        } else {
            throw new OperationFailedException("Invalid user object!");
        }

        return success;
    }

    @Override
    public boolean verifyAccount(int userId) {

        boolean success;

        if (userId > 0) {
            success = userDAO.updateVerifiedStatus(userId);
        } else {
            throw new OperationFailedException("Invalid user id!");
        }
        return success;
    }

    @Override
    public boolean deleteAccount(int userId) {

        boolean success;

        if (userId > 0) {
            success = userDAO.deleteUser(userId);
        } else {
            throw new OperationFailedException("Invalid user id!");
        }
        return success;
    }

    @Override
    public User login(String email, String password) {

        User validUser;

        //check if email is valid
        if (isValidEmailAddress(email)) {
            User user = userDAO.getUserByEmail(email);
            if (user != null && user.getPassword().equals(HashGeneratorUtil.generateHashString(password))) {
                validUser = user;
            } else {
                throw new OperationFailedException("Invalid email/password combination!");
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
            user = userDAO.getUserById(userId);
        } else {
            throw new OperationFailedException("Invalid user id!");
        }

        return user;
    }

    @Override
    public User getUserByEmail(String email) throws OperationFailedException {

        User user;

        if (isValidEmailAddress(email)) {
                user = userDAO.getUserByEmail(email);
        } else {
            throw new OperationFailedException("Invalid email address!");
        }

        return user;
    }

    @Override
    public List<User> getAllUsers() throws OperationFailedException {
        return userDAO.getAllUsers();
    }

    @Override
    public boolean deleteAllUsers() throws OperationFailedException {
        return userDAO.deleteAllUsers();
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
        String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(EMAIL_REGEX);
    }
}
