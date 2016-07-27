package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.User;

/**
 * Created by Hermine Turshujyan 7/25/16.
 */
public interface EmailService {

    public boolean sendVerificationEmail(User user);
}
