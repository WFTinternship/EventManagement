package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.util.HashGenerator;
import org.apache.log4j.Logger;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isValidEmailAddressForm;
import static com.workfront.internship.event_management.service.util.Validator.isValidUser;

/**
 * Created by Hermine Turshujyan 7/20/16.
 */
public class UserServiceImpl implements UserService {

    static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    private UserDAO userDAO;
    private EmailService emailService;


    public UserServiceImpl() {
        try {
            userDAO = new UserDAOImpl();
            emailService = new EmailServiceImpl();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public User addAccount(User user) {
        //check if user object is valid
        if (!isValidUser(user)) {
            throw new OperationFailedException("Invalid user");
        }

        //encrypt user password
        String encryptedPassword = HashGenerator.generateHashString(user.getPassword());
        user.setPassword(encryptedPassword);

        try {
            //insert user into db
            int userId = userDAO.addUser(user);

            //if user is successfully inserted
            if (userId > 0) {
                user.setId(userId);
                boolean success = emailService.sendVerificationEmail(user);
                if (!success) {
                    throw new OperationFailedException("Unable to send verification email");
                }
            }
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("User with email " + user.getEmail() + " already exists!", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
        return user;
    }

    @Override
    public void editProfile(User user) {
        if (!isValidUser(user)) {
            throw new InvalidObjectException("Invalid user");
        }

        try {
            userDAO.updateUser(user);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("User not found", e);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("User with email " + user.getEmail() + " already exists!", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void verifyAccount(int userId) {
        if (userId <= 0) {
            throw new OperationFailedException("Invalid user id");
        }

        try {
            userDAO.updateVerifiedStatus(userId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("User not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteAccount(int userId) {
        if (userId <= 0) {
            throw new OperationFailedException("Invalid user id");
        }

        try {
            userDAO.deleteUser(userId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("User not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public User login(String email, String password) {
        //email/password validation
        if (email == null || email.isEmpty()) {
            throw new OperationFailedException("Empty email");
        }

        if (password == null || password.isEmpty()) {
            throw new OperationFailedException("Empty password");
        }

        if (!isValidEmailAddressForm(email)) {
            throw new OperationFailedException("Invalid email address form");
        }

        User user;
        try {
            //read user data from db
            user = userDAO.getUserByEmail(email);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Invalid email/password combination!");
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }

        //check if passwords match
        if (user.getPassword().equals(HashGenerator.generateHashString(password))) {
            return user;
        } else {
            throw new OperationFailedException("Invalid email/password combination!");
        }
    }

    @Override
    public User getUserById(int userId) {
        if (userId <= 0) {
            throw new OperationFailedException("Invalid user id");
        }

        try {
            //get user data from db
            return userDAO.getUserById(userId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("User not found");
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        if (!isValidEmailAddressForm(email)) {
            throw new OperationFailedException("Invalid email address form");
        }

        try {
            //get user data from db
            return userDAO.getUserByEmail(email);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("User not found");
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllUsers() {
        try {
            userDAO.deleteAllUsers();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }
}
