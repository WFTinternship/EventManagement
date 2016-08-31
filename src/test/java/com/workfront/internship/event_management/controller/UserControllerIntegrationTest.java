package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.workfront.internship.event_management.TestObjectCreator.createTestUser;
import static com.workfront.internship.event_management.controller.util.PageParameters.ACTION_SUCCESS;
import static com.workfront.internship.event_management.controller.util.PageParameters.RESPONSE_FALSE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    private HttpServletRequest testRequest;
    private HttpSession testSession;

    private String testEmail;
    private String testPassword;

    @Before
    public void setUp() {
        //create test user, insert into db
        User testUser = createTestUser();
        testEmail = testUser.getEmail();
        testPassword = testUser.getPassword();

        userService.addAccount(testUser);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);

        when(testRequest.getParameter("email")).thenReturn(testEmail);
        when(testRequest.getParameter("password")).thenReturn(testPassword);
        when(testRequest.getSession()).thenReturn(testSession);
    }

    @After
    public void tearDown() {
        userService.deleteAllUsers();

    }

    @Test
    public void login() {
        CustomResponse result = userController.login(testRequest);

        assertEquals("Status is incorrect", result.getStatus(), ACTION_SUCCESS);
    }

    @Test
    public void logout() {
        String redirectPage = userController.logout(testRequest);

        assertEquals("Incorrect redirect page", redirectPage, "forward:/home");
    }

    @Test
    public void isEmailFree() {
        String result = userController.isEmailFree(testRequest);

        assertEquals("Incorrect response", result, RESPONSE_FALSE);
    }

    @Test
    public void register() throws Exception {
        CustomResponse result = userController.register(testRequest);

        //assertEquals("Status is incorrect", result.getStatus(), ACTION_SUCCESS);
    }
}
