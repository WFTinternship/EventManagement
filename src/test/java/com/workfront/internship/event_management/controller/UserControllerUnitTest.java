package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import org.junit.*;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.mock.web.MockMultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.workfront.internship.event_management.TestObjectCreator.SERVLET_CONTEXT_PATH;
import static com.workfront.internship.event_management.TestObjectCreator.createTestUser;
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
    // private MultipartFile testImage;
    private MockMultipartFile testImage;

    private ServletContext servletContext;

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

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);
        testImage = mock(MockMultipartFile.class);
        servletContext = mock(ServletContext.class);

        Whitebox.setInternalState(userController, "userService", userService);

        when(testRequest.getSession()).thenReturn(testSession);
        when(testSession.getServletContext()).thenReturn(servletContext);
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

    @Test
    public void register_NonEmptyImage_InvalidType() {
        when(testImage.isEmpty()).thenReturn(false);
        when(testImage.getContentType()).thenReturn("");

        CustomResponse response = userController.register(testRequest, testImage);
        assertEquals("Incorrect response status", response.getStatus(), ACTION_FAIL);
        assertEquals("Incorrect response message", response.getMessage(), "Invalid image type!");
    }

    @Ignore
    @Test
    public void register_NonEmptyImage_ImageSaved() {
        when(testImage.isEmpty()).thenReturn(false);
        when(testImage.getContentType()).thenReturn("image/jpeg");

        //create ServletContext mock
        when(servletContext.getRealPath("")).thenReturn(SERVLET_CONTEXT_PATH);


        CustomResponse response = userController.register(testRequest, testImage);
        assertEquals("Incorrect response status", response.getStatus(), ACTION_SUCCESS);
        assertEquals("Incorrect response message", response.getMessage(), "Invalid image type!");
    }

    @Ignore
    @Test
    public void register_NonEmptyImage_ValidType_ImageSaveFailed() {
        when(testImage.isEmpty()).thenReturn(false);
        when(testImage.getContentType()).thenReturn("image/jpeg");

        CustomResponse response = userController.register(testRequest, testImage);
        assertEquals("Incorrect response status", response.getStatus(), ACTION_FAIL);
        assertEquals("Incorrect response message", response.getMessage(), "Invalid image type!");
    }

    @Test
    public void register_Success() {
        when(testImage.isEmpty()).thenReturn(true);

        CustomResponse response = userController.register(testRequest, testImage);
        assertEquals("Incorrect response", response.getStatus(), ACTION_SUCCESS);
    }

    @Test
    public void register_Fail() {
        when(testImage.isEmpty()).thenReturn(true);
        when(userService.addAccount(any(User.class))).thenThrow(OperationFailedException.class);

        //method under test
        CustomResponse response = userController.register(testRequest, testImage);

        assertEquals("Incorrect response", response.getStatus(), ACTION_FAIL);
    }





}
