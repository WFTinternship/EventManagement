package com.workfront.internship.event_management.service.util;

import com.workfront.internship.event_management.model.*;

import java.util.Collection;
import java.util.Date;
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
        if (isEmptyString(email)) {
            return false;
        } else {
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }

    public static boolean isValidUser(User user) {
        boolean valid = false;
        if (user != null) {
            if (!isEmptyString(user.getFirstName())
                    && !isEmptyString(user.getLastName())
                    && !isEmptyString(user.getPassword()) && user.getPassword().length() >= 6
                    && isValidEmailAddressForm(user.getEmail())
                    && user.getRegistrationDate() != null) {
                valid = true;
            }
        }
        return valid;
    }

    public static boolean isEmptyString(String string) {
        return (string != null && string.isEmpty());
    }

    public static boolean isEmptyCollection(Collection collection) {
        return (collection != null && collection.isEmpty());
    }


    public static boolean isValidCategory(Category category) {
        boolean valid = false;
        if (category != null
                && !isEmptyString(category.getTitle())
                && category.getCreationDate() != null) {
            valid = true;
        }
        return valid;
    }

    public static boolean isValidUserResponse(UserResponse userResponse) {
        boolean valid = false;
        // TODO: 7/30/16  implement
        return valid;
    }

    public static boolean isValidMediaType(MediaType mediaType) {
        boolean valid = false;
        if (mediaType != null && !isEmptyString(mediaType.getTitle())) {
            valid = true;
        }
        return valid;
    }

    private int id;
    private MediaType type;
    private String path;
    private String description;
    private int eventId;
    private int uploaderId;
    private Date uploadDate;


    public static boolean isValidMedia(Media media) {
        boolean valid = false;
        if (media != null
                && (isValidMediaType(media.getType())
                && !isEmptyString(media.getPath()))
                && media.getEventId() != 0
                && media.getUploaderId() != 0
                && media.getUploadDate() != null) {
            valid = true;
        }
        return valid;
    }

    public static boolean isValidRecurrenceType(RecurrenceType recurrenceType) {
        boolean valid = false;
        if (recurrenceType != null
                && !isEmptyString(recurrenceType.getTitle())
                && !isEmptyString(recurrenceType.getIntervalUnit())) {
            valid = true;
        }
        return valid;
    }

    public static boolean isValidEventRecurrence(EventRecurrence eventRecurrence) {
        boolean valid = false;
        if (eventRecurrence != null
                && isValidRecurrenceType(eventRecurrence.getRecurrenceType())
                && eventRecurrence.getEventId() != 0
                && eventRecurrence.getRepeatInterval() != 0) {
            valid = true;
        }
        return valid;
    }


    public static boolean isValidRecurrenceOption(RecurrenceOption recurrenceOption) {
        boolean valid = false;
        if (recurrenceOption != null
                && !isEmptyString(recurrenceOption.getTitle())
                && recurrenceOption.getRecurrenceTypeId() > 0) {
            valid = true;
        }
        return valid;
    }

    public static boolean isValidEvent(Event event) {

        boolean valid = false;
        if (event != null) {
            if (!isEmptyString(event.getTitle())
                    && event.getCategory() != null && event.getCategory().getId() > 0
                    && event.getStartDate() != null
                    && event.getEndDate() != null
                    && event.getCreationDate() != null) {
                valid = true;
            }
        }
        return valid;
    }

    public static boolean isValidInvitation(Invitation invitation) {

        boolean valid = false;
        if (invitation != null) {
            if (invitation.getId() > 0 && invitation.getEventId() > 0
                    && isValidUser(invitation.getUser())
                    && !isEmptyString(invitation.getUserRole())
                    && (invitation.getUserResponse() != null)) {
                valid = true;
            }
        }
        return valid;
    }

}
