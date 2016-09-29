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

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.*;

/**
 * Created by Hermine Turshujyan 7/20/16.
 */
@Component
public class UserServiceImpl implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private EmailService emailService;

    @Override
    public User addAccount(User user) {

        //set user default fields
        setDefaultFields(user);

        //check if user object is valid
        if (!isValidUser(user)) {
            String message = "Invalid user object";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        //encrypt user password
        String encryptedPassword = HashGenerator.generateHashString(user.getPassword());
        user.setPassword(encryptedPassword);

        try {
            //insert user into db
            int userId = userDAO.addUser(user);

            if (userId == 0) {
                String message = "Could not add user into database";
                logger.error(message);
                throw new OperationFailedException(message);
            }

            //set generated it to user if successfully inserted
            user.setId(userId);

//            emailService.sendConfirmationEmail(user);
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("User with email " + user.getEmail() + " already exists!", e);
        }
        return user;
    }

    @Override
    public boolean editAccount(User user) {

        if (!isValidUser(user)) {
            String message = "Invalid user object";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        boolean success;

        try {
            success = userDAO.updateUser(user);

            if (!success) {
                String message = "User not found";
                logger.error(message);
                throw new ObjectNotFoundException(message);
            }
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("User with email " + user.getEmail() + " already exists!", e);
        }

        return success;
    }

    @Override
    public boolean verifyAccount(int userId) {
        if (userId < 1) {
            String message = "Invalid user id";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        boolean success = userDAO.updateVerifiedStatus(userId);

        if (!success) {
            String message = "User not found";
            logger.error(message);
            throw new ObjectNotFoundException(message);
        }

        return success;
    }

    @Override
    public boolean deleteAccount(int userId) {
        if (userId < 1) {
            String message = "Invalid user id";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        return userDAO.deleteUser(userId);
    }

    @Override
    public User login(String email, String password) {

        //email/password validation
        if (isEmptyString(email)) {
            String message = "Empty email";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        if (isEmptyString(password)) {
            String message = "Empty password";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        if (!isValidEmailAddressForm(email)) {
            String message = "Invalid email address form";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        //read user data from db
        User user = userDAO.getUserByEmail(email);

        String hashedPassword = HashGenerator.generateHashString(password);

        //check if passwords match
        if (user != null && user.getPassword().equals(hashedPassword)) {
            return user;
        } else {
            String message = "Invalid email/password combination!";
            logger.warn(message);
            throw new OperationFailedException(message);
        }
    }

    @Override
    public User getUserById(int userId) {
        if (userId < 1) {
            String message = "Invalid user id";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        //get user data from db
        User user = userDAO.getUserById(userId);

        if (user == null) {
            String message = "User not found";
            logger.error(message);
            throw new ObjectNotFoundException(message);
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        if (!isValidEmailAddressForm(email)) {
            String message = "Invalid email address form!";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        //get user data from db
        return userDAO.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersMatchingEmail(String email) {
        if (isEmptyString(email)) {
            String message = "Empty email string";
            logger.error(message);
            throw new InvalidObjectException(message);
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
