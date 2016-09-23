package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.util.HashGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    @Override
    public User addAccount(User user) {
        //set user default fields
        setDefaultFields(user);

        //check if user object is valid
        if (!isValidUser(user)) {
            throw new InvalidObjectException("Invalid user object");
        }

        //encrypt user password
        String encryptedPassword = HashGenerator.generateHashString(user.getPassword());
        user.setPassword(encryptedPassword);

        try {
            //insert user into db
            int userId = userDAO.addUser(user);

            //set generated it to user
            user.setId(userId);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("User with email " + user.getEmail() + " already exists!", e);
        }
        return user;
    }

    @Override
    public boolean editAccount(User user) {
        if (!isValidUser(user)) {
            throw new InvalidObjectException("Invalid user object");
        }

        boolean success;
        try {
            success = userDAO.updateUser(user);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("User with email " + user.getEmail() + " already exists!", e);
        }

        if (!success) {
            throw new ObjectNotFoundException("User with id " + user.getId() + " not found.");
        }
        return success;
    }

    @Override
    public boolean verifyAccount(int userId) {
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }

        boolean success = userDAO.updateVerifiedStatus(userId);
        if (!success) {
            throw new ObjectNotFoundException("User with id " + userId + " not found");
        }
        return success;
    }

    @Override
    public boolean deleteAccount(int userId) {
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }

        return userDAO.deleteUser(userId);
    }

    @Override
    public User login(String email, String password) {
        //email/password validation
        if (isEmptyString(email)) {
            throw new InvalidObjectException("Empty email");
        }

        if (isEmptyString(password)) {
            throw new InvalidObjectException("Empty password");
        }

        if (!isValidEmailAddressForm(email)) {
            throw new InvalidObjectException("Invalid email address form");
        }

        //read user data from db
        User user = userDAO.getUserByEmail(email);
        String str = HashGenerator.generateHashString(password);

        //check if passwords match
        if (user != null && user.getPassword().equals(HashGenerator.generateHashString(password))) {
            return user;
        } else {
            throw new OperationFailedException("Invalid email/password combination!");
        }
    }

    @Override
    public User getUserById(int userId) {
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }

        //get user data from db
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new ObjectNotFoundException("User with id " + userId + " not found.");
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        if (!isValidEmailAddressForm(email)) {
            throw new InvalidObjectException("Invalid email address form!");
        }

        //get user data from db
        return userDAO.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersMatchingEmail(String email) {
        if (email.isEmpty()) {
            throw new InvalidObjectException("Empty string!");
        }

        //get user data from db
        return userDAO.getUsersMatchingEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public void deleteAllUsers() {
        userDAO.deleteAllUsers();
    }

    //helper methods
    private void setDefaultFields(User user) {
        user.setVerified(false);
        user.setRegistrationDate(new Date());
    }
}
