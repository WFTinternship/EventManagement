package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import org.junit.*;
import org.mockito.internal.util.reflection.Whitebox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.workfront.internship.event_management.TestObjectCreator.*;
import static com.workfront.internship.event_management.controller.util.PageParameters.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 8/30/16.
 */
public class UserControllerUnitTest {

    private static UserController userController;

    private UserService userService;
    private User testUser;
    private HttpServletRequest testRequest;
    private HttpSession testSession;

    @BeforeClass
    public static void setUpClass() {
        userController = new UserController();
    }

    @AfterClass
    public static void tearDownClass() {
        userController = null;
    }


    @Before
    public void setUp() {
        //create test user object
        testUser = createTestUser();

        userService = mock(UserService.class);
        Whitebox.setInternalState(userController, "userService", userService);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);

//        when(testRequest.getParameter("email")).thenReturn(VALID_EMAIL);
//        when(testRequest.getParameter("password")).thenReturn(VALID_PASSWORD);
        when(testRequest.getSession()).thenReturn(testSession);
    }

    @After
    public void tearDown() {
        //delete test user object
        testUser = null;

        userService = null;
    }

    @Test
    public void login_Success() {
        when(userService.login(anyString(), anyString())).thenReturn(testUser);

        CustomResponse response = userController.login(testRequest);

        assertEquals("Status is incorrect", response.getStatus(), ACTION_SUCCESS);
        verify(testSession).setAttribute("user", testUser);
    }

    @Test
    public void login_Fail() {
        doThrow(InvalidObjectException.class).when(userService).login(anyString(), anyString());

        //method under test
        CustomResponse result = userController.login(testRequest);

        assertEquals("Status is incorrect", result.getStatus(), ACTION_FAIL);
        verify(testSession, never()).setAttribute(anyString(), anyString());
    }

    @Test
    public void isEmailFree_False() {
        when(userService.getUserByEmail(anyString())).thenReturn(testUser);

        String response = userController.isEmailFree(testRequest);
        assertEquals("Incorrect response", response, RESPONSE_FALSE);
    }

    @Test
    public void isEmailFree_True() {
        when(userService.getUserByEmail(anyString())).thenReturn(null);

        String response = userController.isEmailFree(testRequest);
        assertEquals("Incorrect response", response, RESPONSE_TRUE);
    }

    @Test
    public void logout() {

        String redirectPage = userController.logout(testRequest);
        assertEquals("Incorrect redirect page", redirectPage, "forward:/home");

        verify(testSession).setAttribute("user", null);
        verify(testSession).invalidate();
    }
}
