package com.workfront.internship.event_management.service;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.dao.CategoryDAOImpl;
import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.util.HashGenerator;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualUsers;
import static com.workfront.internship.event_management.TestObjectCreator.NON_EXISTING_ID;
import static com.workfront.internship.event_management.TestObjectCreator.VALID_ID;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/27/16.
 */
public class UserServiceUnitTest {

    private static UserService userService;
    private static EmailService emailService;
    private UserDAO userDAO;

    @BeforeClass
    public static void setUpClass() {
        userService = new UserServiceImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        userService = null;
    }

    private User testUser;

    @Before
    public void setUp() {
        userDAO = Mockito.mock(UserDAOImpl.class);
        emailService = Mockito.mock(EmailServiceImpl.class);

        //inject mocks
        Whitebox.setInternalState(userService, "userDAO", userDAO);
        Whitebox.setInternalState(userService, "emailService", emailService);

        //create test user object
        testUser = TestObjectCreator.createTestUser();
    }

    @After
    public void tearDown() {
        //delete test user object
        testUser = null;

        userDAO = null;
        //  emailService = null;
    }

    //Testing addAccount method
    @Test(expected = InvalidObjectException.class)
    public void addAccount_InvalidUser() {
        testUser.setEmail(TestObjectCreator.INVALID_EMAIL);

        //method under test
        userService.addAccount(testUser);
    }

    @Test
    public void addAccount_EncryptPassword() {
        when(userDAO.addUser(testUser)).thenReturn(1);

        String expectedPassword = HashGenerator.generateHashString(testUser.getPassword());

        //method under test
        userService.addAccount(testUser);

        String actualPassword = testUser.getPassword();
        assertEquals("Unable to hash password", actualPassword, expectedPassword);
    }

    @Test(expected = OperationFailedException.class)
    public void addAccount_InsertFailed_DuplicateUser() throws DuplicateEntryException, DAOException {
        when(userDAO.addUser(testUser)).thenThrow(DuplicateEntryException.class);

        //method under test
        userService.addAccount(testUser);
    }

    @Test(expected = OperationFailedException.class)
    public void addAccount_UnableToSendConfirmationEmail() throws DuplicateEntryException, DAOException, javax.mail.MessagingException {
        when(emailService.sendConfirmationEmail(testUser)).thenThrow(MessagingException.class);

        //method under test
        userService.addAccount(testUser);
    }

    //Testing editAccount method
    @Test(expected = InvalidObjectException.class)
    public void editAccount_InvalidUser() {
        testUser.setEmail(TestObjectCreator.INVALID_EMAIL);

        //method under test
        userService.editAccount(testUser);
    }

    @Test
    public void editAccount_Success() {
        when(userDAO.updateUser(testUser)).thenReturn(true);

        //method under test
        boolean success = userService.editAccount(testUser);

        assertTrue(success);
        Mockito.verify(userDAO).updateUser(testUser);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void editAccount_UserNotFound() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        when(userDAO.updateUser(testUser)).thenReturn(false);

        //method under test
        userService.editAccount(testUser);
    }

    @Test(expected = OperationFailedException.class)
    public void editAccount_DuplicateUser() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(DuplicateEntryException.class).when(userDAO).updateUser(testUser);

        //method under test
        userService.editAccount(testUser);
    }

    //Testing verifyAccount method
    @Test(expected = InvalidObjectException.class)
    public void verifyAccount_InvalidUserId() throws DAOException, ObjectNotFoundException {
        //method under test
        userService.verifyAccount(TestObjectCreator.INVALID_ID);

        verify(userDAO).updateVerifiedStatus(TestObjectCreator.INVALID_ID);
    }

    @Test
    public void verifyAccount_Success() throws DAOException, ObjectNotFoundException {
        when(userDAO.updateVerifiedStatus(VALID_ID)).thenReturn(true);

//method under test
        userService.verifyAccount(VALID_ID);
        verify(userDAO).updateVerifiedStatus(VALID_ID);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void verifyAccount_UserNotFound() throws DAOException, ObjectNotFoundException {
        when(userDAO.updateVerifiedStatus(anyInt())).thenReturn(false);

        //method under test
        userService.verifyAccount(VALID_ID);

        Mockito.verify(userDAO).updateVerifiedStatus(VALID_ID);
    }

    // Testing login method
    @Test(expected = InvalidObjectException.class)
    public void login_InvalidEmailForm() {
        //method under test
        userService.login(TestObjectCreator.INVALID_EMAIL, testUser.getPassword());
    }

    @Test(expected = InvalidObjectException.class)
    public void login_EmptyEmail() {
        //method under test
        userService.login("", testUser.getPassword());
    }

    @Test(expected = InvalidObjectException.class)
    public void login_NullEmail() {
        //method under test
        userService.login(null, testUser.getPassword());
    }

    @Test(expected = InvalidObjectException.class)
    public void login_NullPassword() {
        //method under test
        userService.login(testUser.getEmail(), null);
    }

    @Test(expected = OperationFailedException.class)
    public void login_NonExistingEmail() throws ObjectNotFoundException, DAOException {
        when(userDAO.getUserByEmail(TestObjectCreator.NON_EXISTING_EMAIL)).thenReturn(null);

        //method under test
        userService.login(TestObjectCreator.NON_EXISTING_EMAIL, testUser.getPassword());
    }

    @Test(expected = OperationFailedException.class)
    public void login_WrongPassword() throws ObjectNotFoundException, DAOException {
        when(userDAO.getUserByEmail(testUser.getEmail())).thenReturn(testUser);

        //method under test
        userService.login(testUser.getEmail(), TestObjectCreator.WRONG_PASSWORD);
    }

    @Test
    public void login_Success() {
        String notEncryptedPassword = testUser.getPassword();

        //set encrypted password to test user
        testUser.setPassword(HashGenerator.generateHashString(testUser.getPassword()));

        when(userDAO.getUserByEmail(testUser.getEmail())).thenReturn(testUser);

        //method under test
        User actualUser = userService.login(testUser.getEmail(), notEncryptedPassword);

        assertEqualUsers(testUser, actualUser);
    }

    //Testing deleteAccount method
    @Test(expected = InvalidObjectException.class)
    public void deleteAccount_InvalidUserId() {
        //method under test
        userService.deleteAccount(TestObjectCreator.INVALID_ID);
    }

    @Test
    public void deleteAccount_UserNotFound() {
        when(userDAO.deleteUser(NON_EXISTING_ID)).thenReturn(false);

        //method under test
        boolean success = userService.deleteAccount(NON_EXISTING_ID);
        assertFalse(success);
    }

    @Test
    public void deleteAccount_Success() {
        when(userDAO.deleteUser(VALID_ID)).thenReturn(true);

        //method under test
        userService.deleteAccount(VALID_ID);

        verify(userDAO).deleteUser(VALID_ID);
    }

    //Testing getUserById method
    @Test(expected = InvalidObjectException.class)
    public void getUserById_InvalidUserId() {
        //method under test
        userService.getUserById(TestObjectCreator.INVALID_ID);
    }

    @Test(expected = InvalidObjectException.class)
    public void getUsersMatchingEmail_EmptyString() {
        //method under test
        userService.getUsersMatchingEmail("");
    }

    @Test
    public void getUsersMatchingEmail_Success() {
        List<User> userList = new ArrayList<>();
        userList.add(testUser);

        when(userDAO.getUsersMatchingEmail(testUser.getEmail())).thenReturn(userList);

        //method under test
        List<User> actualUserList = userService.getUsersMatchingEmail(testUser.getEmail());

        assertNotNull("User list is null", actualUserList);
        assertFalse("User list is empty", actualUserList.isEmpty());
        assertEqualUsers(testUser, actualUserList.get(0));
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getUserById_UserNotFound() {
        when(userDAO.getUserById(NON_EXISTING_ID)).thenReturn(null);

        //method under test
        userService.getUserById(NON_EXISTING_ID);
    }

    @Test
    public void getUserById_Success() {
        testUser.setId(VALID_ID);
        when(userDAO.getUserById(VALID_ID)).thenReturn(testUser);

        //method under test
        User actualUser = userService.getUserById(VALID_ID);
        assertEqualUsers(actualUser, testUser);
    }

    //Testing getUserByEmail method
    @Test(expected = InvalidObjectException.class)
    public void getUserByEmail_InvalidEmailForm() {
        //method under test
        userService.getUserByEmail(TestObjectCreator.INVALID_EMAIL);
    }

    @Test
    public void getUserByEmail_Success() {
        when(userDAO.getUserByEmail(testUser.getEmail())).thenReturn(testUser);

        //method under test
        User actualUser = userService.getUserByEmail(testUser.getEmail());

        assertEqualUsers(actualUser, testUser);
    }

    //Testing getAllUsers method
    @Test
    public void getAllUsers_Success() {
        //method under test
        userService.getAllUsers();

        verify(userDAO).getAllUsers();
    }

    //Testing deleteAllUsers method
    @Test
    public void deleteAllUsers_Success() {
        //method under test
        userService.deleteAllUsers();

        verify(userDAO).deleteAllUsers();
    }

}
