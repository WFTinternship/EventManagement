package com.workfront.internship.event_management.business;

import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
import com.workfront.internship.event_management.model.User;

/**
 * Created by Hermine Turshujyan 7/20/16.
 */
public class UserServiceImpl implements UserService {

    UserDAO userDAO = new UserDAOImpl();

    @Override
    public void addAccount(User user) {
        //encrypt user password
        String encryptedPassword = HashGeneratorUtil.generateHashString(user.getPassword());
        user.setPassword(encryptedPassword);

        userDAO.addUser(user);
    }

    @Override
    public void editProfile(User user) {
        userDAO.updateUser(user);
    }

    @Override
    public void deleteAccount(int userId) {
        userDAO.deleteUser(userId);
    }

    @Override
    public void login(String username, String password) {

        User user = userDAO.getUserByUsername(username);
        if (user.getPassword() == HashGeneratorUtil.generateHashString(password)) {
// TODO: 7/21/16  
        }
    }

}
