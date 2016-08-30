package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.JsonResponse;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import org.junit.*;
import org.mockito.internal.util.reflection.Whitebox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.workfront.internship.event_management.TestObjectCreator.createTestUser;
import static com.workfront.internship.event_management.controller.util.PageParameters.ACTION_FAIL;
import static com.workfront.internship.event_management.controller.util.PageParameters.ACTION_SUCCESS;
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

//        testRequest = new MockHttpServletRequest();
//        testRequest.addParameter("email", "turshujyan@gmail.com");
//        testRequest.addParameter("password", "turshujyan");

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);

        HttpServletResponse response = mock(HttpServletResponse.class);

        when(testRequest.getParameter("email")).thenReturn("turshujyan@gmail.com");
        when(testRequest.getParameter("password")).thenReturn("turshujyan");
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

        JsonResponse response = userController.login(testRequest);

        assertEquals("status is incorrect", response.getStatus(), ACTION_SUCCESS);
        verify(testSession).setAttribute("user", testUser);
    }

    @Test
    public void login_Fail() {
        doThrow(InvalidObjectException.class).when(userService).login(anyString(), anyString());

        userController.login(testRequest);

        JsonResponse result = userController.login(testRequest);
        assertEquals("status is incorrect", result.getStatus(), ACTION_FAIL);

        verify(testSession, never()).setAttribute(anyString(), anyString());

    }
}
