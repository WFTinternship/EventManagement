package com.workfront.internship.event_management.service.util;

import com.workfront.internship.event_management.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hermine Turshujyan 7/28/16.
 */
public class Validator {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN); // TODO: 7/28/16 read more

    public static boolean isValidEmailAddressForm(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidUser(User user) {
        boolean valid = false;
        if (user != null) {
            if (user.getFirstName() != null && !user.getFirstName().isEmpty()
                    && user.getLastName() != null && !user.getLastName().isEmpty()
                    && user.getPassword() != null && !user.getPassword().isEmpty()
                    && user.getEmail() != null && isValidEmailAddressForm(user.getEmail())) {
                valid = true;
            }
        }
        return valid;
    }

    public static boolean isEmptyString(String string) {
        return (string == null || (string != null && string.isEmpty()));
    }
}
