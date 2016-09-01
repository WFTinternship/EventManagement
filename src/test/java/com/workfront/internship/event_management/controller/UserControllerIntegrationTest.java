package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.controller.util.TestHttpServletRequest;
import com.workfront.internship.event_management.controller.util.TestHttpSession;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.workfront.internship.event_management.TestObjectCreator.createTestUser;
import static com.workfront.internship.event_management.controller.util.PageParameters.ACTION_SUCCESS;
import static com.workfront.internship.event_management.controller.util.PageParameters.RESPONSE_FALSE;
import static org.junit.Assert.assertEquals;

/**
 * Created by Hermine Turshujyan 8/30/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class UserControllerIntegrationTest {

    @Autowired
    private UserController userController;
    @Autowired
    private UserService userService;

    private TestHttpServletRequest testRequest;
    private TestHttpSession testSession;

    private String testEmail;
    private String testPassword;

    @Before
    public void setUp() {
        //create test user, insert into db
        User testUser = createTestUser();
        testEmail = testUser.getEmail();
        testPassword = testUser.getPassword();

        //insert user into db
        userService.addAccount(testUser);

        //create mocks
        testRequest = new TestHttpServletRequest(); //mock(HttpServletRequest.class);
        testSession = new TestHttpSession(); //mock(HttpSession.class);
    }

    @After
    public void tearDown() {
        userService.deleteAllUsers();
    }

    @Test
    public void login() {
        testRequest.setParameter("email", testEmail);
        testRequest.setParameter("password", testPassword);

        CustomResponse result = userController.login(testRequest);

        assertEquals("Status is incorrect", result.getStatus(), ACTION_SUCCESS);
    }

    @Test
    public void logout() {
        testSession.setAttribute("user", new User());
        String redirectPage = userController.logout(testRequest);

        assertEquals("Incorrect redirect page", redirectPage, "forward:/home");
    }

    @Test
    public void isEmailFree() {
        testRequest.setParameter("email", testEmail);
        String result = userController.isEmailFree(testRequest);

        assertEquals("Incorrect response", result, RESPONSE_FALSE);
    }

    @Test
    public void register() {
        // userController.register(tes)
        // CustomResponse result = userController.register(testRequest);

        //assertEquals("Status is incorrect", result.getStatus(), ACTION_SUCCESS);
    }
}
