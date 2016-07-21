package com.workfront.internship.event_management.business;

import com.workfront.internship.event_management.model.User;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
public interface UserService {

    public void addAccount(User user);

    public void editProfile(User user);

    public void deleteAccount(int userId);

    public void login(String username, String password);

}
