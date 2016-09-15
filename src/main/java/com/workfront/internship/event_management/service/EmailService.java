package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.User;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by Hermine Turshujyan 7/25/16.
 */
public interface EmailService {

    public boolean sendConfirmationEmail(User user);
}
