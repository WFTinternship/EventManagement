package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.controller.util.TestHttpServletRequest;
import com.workfront.internship.event_management.controller.util.TestHttpSession;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.FileService;
import com.workfront.internship.event_management.service.UserService;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;

import java.io.IOException;

import static com.workfront.internship.event_management.TestObjectCreator.VALID_IMAGE_NAME;
import static com.workfront.internship.event_management.TestObjectCreator.WEB_CONTENT_ROOT;
import static com.workfront.internship.event_management.TestObjectCreator.createTestUser;
import static com.workfront.internship.event_management.controller.util.CongrollerConstants.ACTION_SUCCESS;
import static com.workfront.internship.event_management.controller.util.CongrollerConstants.HOME_VIEW_REDIRECT;
import static com.workfront.internship.event_management.controller.util.CongrollerConstants.RESPONSE_FALSE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
    private FileService fileService;

    private TestHttpServletRequest testRequest;
    private TestHttpSession testSession;
    private ServletContext servletContext;


    private String testEmail;
    private String testPassword;
    private User testUser;
    private MockMultipartFile testImage;

    @Before
    public void setUp() {
        //create test user, insert into db
        testUser = createTestUser();
        testImage = new MockMultipartFile("imgName.jpg", "img".getBytes());
        testEmail = testUser.getEmail();
        testPassword = testUser.getPassword();

        //insert user into db
        userService.addAccount(testUser);

        //create mocks
        testRequest = spy(TestHttpServletRequest.class);
        testSession = spy(TestHttpSession.class);
        fileService = mock(FileService.class);
        servletContext = mock(ServletContext.class);

        Whitebox.setInternalState(userController, "fileService", fileService);
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

        assertEquals("Incorrect redirect page", redirectPage, HOME_VIEW_REDIRECT);
    }

    @Test
    public void isEmailFree() {
        testRequest.setParameter("email", testEmail);
        String result = userController.isEmailFree(testRequest);

        assertEquals("Incorrect response", result, RESPONSE_FALSE);
    }

    @Test
    public void register() throws IOException {
        //delete inserted test user
        userService.deleteAllUsers();

        testRequest.setParameter("firstName", testUser.getFirstName());
        testRequest.setParameter("lastName", testUser.getLastName());
        testRequest.setParameter("email", testUser.getEmail());
        testRequest.setParameter("password", testUser.getPassword());
        testRequest.setParameter("phone", testUser.getPhoneNumber());

        when(testRequest.getSession()).thenReturn(testSession);
        when(testSession.getServletContext()).thenReturn(servletContext);
        when(fileService.isValidImage(testImage)).thenReturn(true);
        when(servletContext.getRealPath(anyString())).thenReturn(WEB_CONTENT_ROOT);
        when(fileService.saveAvatar(WEB_CONTENT_ROOT, testImage)).thenReturn(VALID_IMAGE_NAME);

        //method under test
        userController.register(testRequest, testImage);

        CustomResponse response = userController.login(testRequest);
        assertEquals("Registration failed, could not login", response.getStatus(), ACTION_SUCCESS);

    }
}
