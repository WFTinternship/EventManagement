package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.*;
import org.junit.*;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.mock.web.MockMultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.TestObjectCreator.*;
import static com.workfront.internship.event_management.controller.util.CongrollerConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 8/30/16.
 */
public class UserControllerUnitTest {

    private static UserController userController;

    private UserService userService;
    private EventService eventService;
    private FileService fileService;

    private User testUser;
    private HttpServletRequest testRequest;
    private HttpSession testSession;
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

        userService = mock(UserServiceImpl.class);
        eventService = mock(EventServiceImpl.class);
        fileService = mock(FileService.class);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);
        testImage = mock(MockMultipartFile.class);
        servletContext = mock(ServletContext.class);

        Whitebox.setInternalState(userController, "userService", userService);
        Whitebox.setInternalState(userController, "eventService", eventService);
        Whitebox.setInternalState(userController, "fileService", fileService);

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

        assertEquals("Response status is incorrect", response.getStatus(), ACTION_SUCCESS);
        verify(testSession).setAttribute("user", testUser);
    }

    @Test
    public void login_Fail_InvalidEmail() {
        doThrow(InvalidObjectException.class).when(userService).login(anyString(), anyString());

        //method under test
        CustomResponse result = userController.login(testRequest);

        assertEquals("Response status is incorrect", result.getStatus(), ACTION_FAIL);
        verify(testSession, never()).setAttribute(anyString(), anyString());
    }

    @Test
    public void login_Fail() {
        doThrow(OperationFailedException.class).when(userService).login(anyString(), anyString());

        //method under test
        CustomResponse result = userController.login(testRequest);

        assertEquals("Response status is incorrect", result.getStatus(), ACTION_FAIL);
        verify(testSession, never()).setAttribute(anyString(), anyString());
    }

    @Test
    public void logout() {

        String redirectPage = userController.logout(testRequest);
        assertEquals("Incorrect redirect page", redirectPage, "redirect:/");

        verify(testSession).setAttribute("user", null);
        verify(testSession).invalidate();
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
    public void findUserByEmail_Found() {
        List<User> userList = new ArrayList<>();
        userList.add(testUser);

        when(testRequest.getParameter("email")).thenReturn(VALID_EMAIL);
        when(userService.getUsersMatchingEmail(VALID_EMAIL)).thenReturn(userList);

        CustomResponse customResponse = userController.findUserByEmail(testRequest);
        assertEquals("Incorrect response status", customResponse.getStatus(), ACTION_FOUND);
    }

    @Test
    public void findUserByEmail_EmptyEmail() {
        when(testRequest.getParameter("email")).thenReturn("");

        CustomResponse customResponse = userController.findUserByEmail(testRequest);
        assertEquals("Incorrect response status", customResponse.getStatus(), ACTION_FAIL);
    }

    @Test
    public void findUserByEmail_Not_Found() {
        when(testRequest.getParameter("email")).thenReturn(VALID_EMAIL);
        when(userService.getUsersMatchingEmail(VALID_EMAIL)).thenReturn(new ArrayList<User>());

        CustomResponse customResponse = userController.findUserByEmail(testRequest);
        assertEquals("Incorrect response status", customResponse.getStatus(), ACTION_NOT_FOUND);
    }

    @Test
    public void goToRegistrationPage_Success() {

        when(testSession.getAttribute("user")).thenReturn(null);

        //method under test
        String redirectPage = userController.goToRegistrationPage(testRequest);

        assertEquals("Incorrect redirect page", redirectPage, REGISTRATION_VIEW);
    }

    @Test
    public void goToMyEventsPage_Fail_NotLoggedIn() {

        when(testSession.getAttribute("user")).thenReturn(null);

        //method under test
        String redirectPage = userController.goToMyEventsPage(testRequest);

        assertEquals("Incorrect redirect page", redirectPage, HOME_VIEW_REDIRECT);
    }

    @Test
    public void goToMyEventsPage_Success() {

        when(testSession.getAttribute("user")).thenReturn(testUser);

        //method under test
        String redirectPage = userController.goToMyEventsPage(testRequest);

        assertEquals("Incorrect redirect page", redirectPage, MY_EVENTS_VIEW);
        verify(testRequest).setAttribute(eq("userOrganizedEvents"), anyListOf(Event.class));
        verify(testRequest).setAttribute(eq("userInvitedEvents"), anyListOf(Event.class));
        verify(testRequest).setAttribute(eq("userAcceptedEvents"), anyListOf(Event.class));
        verify(testRequest).setAttribute(eq("userPendingEvents"), anyListOf(Event.class));
        verify(testRequest).setAttribute(eq("userParticipatedEvents"), anyListOf(Event.class));
    }


    @Test
    public void goToRegistrationPage_Fail_LoggedInUser() {
        when(testSession.getAttribute("user")).thenReturn(testUser);

        //method under test
        String redirectPage = userController.goToRegistrationPage(testRequest);

        assertEquals("Incorrect redirect page", redirectPage, HOME_VIEW_REDIRECT);
    }

    @Test
    public void register_NonEmptyImage_InvalidType() throws IOException {
        when(testImage.isEmpty()).thenReturn(false);
        when(fileService.isValidImage(testImage)).thenReturn(false);

        CustomResponse response = userController.register(testRequest, testImage);
        assertEquals("Incorrect response status", response.getStatus(), ACTION_FAIL);
        assertEquals("Incorrect response message", response.getMessage(), "Invalid image type!");
        verify(fileService, never()).saveAvatar(anyString(), any(MockMultipartFile.class));
    }

    @Test
    public void register_NonEmptyImage_ImageSaved() throws IOException {
        when(testImage.isEmpty()).thenReturn(false);
        when(fileService.isValidImage(testImage)).thenReturn(true);

        when(servletContext.getRealPath(anyString())).thenReturn(WEB_CONTENT_ROOT);
        when(fileService.saveAvatar(WEB_CONTENT_ROOT, testImage)).thenReturn(VALID_IMAGE_NAME);

        CustomResponse response = userController.register(testRequest, testImage);
        assertEquals("Incorrect response status", response.getStatus(), ACTION_SUCCESS);
        verify(userService).addAccount(any(User.class));
    }

    @Test
    public void register_NonEmptyImage_ValidType_ImageSaveFailed() throws IOException {
        when(testImage.isEmpty()).thenReturn(false);
        when(fileService.isValidImage(testImage)).thenReturn(true);

        when(servletContext.getRealPath(anyString())).thenReturn(WEB_CONTENT_ROOT);
        doThrow(IOException.class).when(fileService).saveAvatar(WEB_CONTENT_ROOT, testImage);

        CustomResponse response = userController.register(testRequest, testImage);
        assertEquals("Incorrect response status", response.getStatus(), ACTION_FAIL);
        verify(userService, never()).addAccount(any(User.class));
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
