package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.workfront.internship.event_management.controller.util.PageParameters.ACTION_SUCCESS;
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

    private HttpServletRequest testRequest;
    private HttpSession testSession;

    @Before
    public void setUp() {
        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);

        when(testRequest.getParameter("email")).thenReturn("turshujyan@gmail.com");
        when(testRequest.getParameter("password")).thenReturn("turshujyan");
        when(testRequest.getSession()).thenReturn(testSession);
    }

    @Test
    public void login() {
        CustomResponse result = userController.login(testRequest);

        assertEquals("status is incorrect", result.getStatus(), ACTION_SUCCESS);
    }

    @Test
    public void logout() {

        String redirectPage = userController.logout(testRequest);
        assertEquals(redirectPage, "forward:/home");
    }
}
