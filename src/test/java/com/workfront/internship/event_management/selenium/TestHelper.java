package com.workfront.internship.event_management.selenium;

import java.util.UUID;

/**
 * Created by Hermine Turshujyan 8/12/16.
 */

public class TestHelper {
    public static final String HOME_PAGE_URL = "http://localhost:8080/index.jsp";
    public static final String EVENTS_PAGE_URL = "http://localhost:8080/events.jsp";
    public static final String REGISTRATION_PAGE_URL = "http://localhost:8080/registration.jsp";

    public static final String INVALID_EMAIL = "afasfdfdf.co"; //invalid format
    public static final String INVALID_PASSWORD = "issword"; //length < 6
    public static final String EXISTING_EMAIL = "turshujyan@gmail.com";


    public static final String FIRST_NAME = "Test firstname";
    public static final String LAST_NAME = "Test firstname";
    public static final String VALID_PASSWORD = "turshujyan";
    public static final String NON_EXISTING_EMAIL = UUID.randomUUID().toString() + "@test.com";


}
