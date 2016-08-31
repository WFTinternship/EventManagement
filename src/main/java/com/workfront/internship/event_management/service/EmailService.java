package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.User;
import org.springframework.stereotype.Component;

/**
 * Created by Hermine Turshujyan 7/25/16.
 */
@Component
public interface EmailService {

    public boolean sendVerificationEmail(User user);
}
