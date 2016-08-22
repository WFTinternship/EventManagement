package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.util.HashGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.*;

/**
 * Created by Hermine Turshujyan 7/20/16.
 */
@Component
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    // private EmailService emailService;

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

            //set generated it to user
            user.setId(userId);
           /* boolean success = emailService.sendVerificationEmail(user);
            if (!success) {
                throw new OperationFailedException("Unable to send verification email");
            }*/
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
    public void editAccount(User user) {
        if (!isValidUser(user)) {
            throw new OperationFailedException("Invalid user");
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
        if (userId < 1) {
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
        if (userId < 1) {
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
        if (isEmptyString(email)) {
            throw new OperationFailedException("Empty email");
        }

        if (isEmptyString(password)) {
            throw new OperationFailedException("Empty password");
        }

        if (!isValidEmailAddressForm(email)) {
            throw new OperationFailedException("Invalid email address form");
        }

        User user;
        try {
            //read user data from db
            user = userDAO.getUserByEmail(email);
            String str = HashGenerator.generateHashString(password);

            //check if passwords match
            if (user != null && user.getPassword().equals(HashGenerator.generateHashString(password))) {
                return user;
            } else {
                throw new OperationFailedException("Invalid email/password combination!");
            }
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public User getUserById(int userId) {
        if (userId < 1) {
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
