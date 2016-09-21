package com.workfront.internship.event_management.controller.util;

/**
 * Created by Hermine Turshujyan 8/26/16.
 */
public class PageParameters {
    //jsp page names
    public static final String DEFAULT_ERROR_VIEW = "error";

    public static final String EVENT_EDIT_VIEW = "event-edit";
    public static final String EVENT_DETAILS_VIEW = "event-details";
    public static final String EVENT_INVITATION_RESPOND = "invitation-respond";
    public static final String EVENTS_LIST_VIEW = "events";

    public static final String HOME_VIEW = "index";
    public static final String REGISTRATION_VIEW = "index";
    public static final String MY_ACCOUNT_VIEW = "my-account";


    //response messages, parameter values
    public static final String ACTION_SUCCESS = "SUCCESS";
    public static final String ACTION_FAIL = "FAIL";
    public static final String ACTION_FOUND = "FOUND";
    public static final String ACTION_NOT_FOUND = "NOT FOUND";

    public static final String RESPONSE_TRUE = "true";
    public static final String RESPONSE_FALSE = "false";

    public static final String UPCOMING_EVENTS_HEADER = "Upcoming Events";
    public static final String PAST_EVENTS_HEADER = "Past Events";
    public static final String ALL_EVENTS_HEADER = "All Events";

    //error messages
    public static final String OPERATION_NOT_ALLOWED_MESSAGE = "Operation not allowed!";
    public static final String UNAUTHORIZED_ACCESS_MESSAGE = "Unauthorized access!";
    public static final String OPERATION_FAILED_MESSAGE = "An error occurred while processing your request. Please, try again later";
}
