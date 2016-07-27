package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestHelper;
import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
import com.workfront.internship.event_management.exception.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.util.HashGeneratorUtil;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import static com.workfront.internship.event_management.TestHelper.assertEqualUsers;
import static junit.framework.TestCase.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Hermine Turshujyan 7/27/16.
 */
public class UserServiceUnitTest {

    private static UserService userService;
    private User testUser;
    private UserDAO userDAO;
    private EmailService emailService;

    @BeforeClass
    public static void setUpClass() throws OperationFailedException {
        userService = new UserServiceImpl();
    }


    @AfterClass
    public static void tearDownClass() {
        userService = null;
    }

    @Before
    public void setUp() {
        //create test user object
        testUser = TestHelper.createTestUser();

        userDAO = Mockito.mock(UserDAOImpl.class);
        emailService = Mockito.mock(EmailServiceImpl.class);

        Whitebox.setInternalState(userService, "userDAO", userDAO);
        Whitebox.setInternalState(userService, "emailService", emailService);
    }

    @After
    public void tearDown() {
        testUser = null;
        userDAO = null;
        emailService = null;
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void addAccount_ValidUser_EncryptPassword() {

        String expectedPassword = HashGeneratorUtil.generateHashString(testUser.getPassword());

        //method under test
        userService.addAccount(testUser);

        String actualPassword = testUser.getPassword();
        assertEquals("Unable to hash password.", actualPassword, expectedPassword);
    }

    @Test
    public void addAccount_ValidUser_EmailSent() {
        when(userDAO.addUser(testUser)).thenReturn(10);
        when(emailService.sendVerificationEmail(testUser)).thenReturn(true);

        //method under test
        userService.addAccount(testUser);

        Mockito.verify(emailService).sendVerificationEmail(testUser);
    }

    @Test
    public void addAccount_ValidUser_EmailSent_False() {
        when(userDAO.addUser(testUser)).thenReturn(10);
        when(emailService.sendVerificationEmail(testUser)).thenReturn(false);

        thrown.expect(OperationFailedException.class);
        thrown.expectMessage("Unable to send verification email!");

        //method under test
        userService.addAccount(testUser);

        Mockito.verify(emailService).sendVerificationEmail(testUser);
    }

    @Test
    public void addAccount_InvalidUser() {
        testUser.setEmail(TestHelper.INVALID_EMAIL);

        thrown.expect(OperationFailedException.class);
        thrown.expectMessage("Invalid user object!");

        //method under test
        userService.addAccount(testUser);
    }

    @Test
    public void editProfile_Success() {
        when(userDAO.updateUser(testUser)).thenReturn(true);

        //method under test
        boolean success = userService.editProfile(testUser);

        assertTrue(success);
    }

    @Test
    public void editProfile_UserNotFound() {
        when(userDAO.updateUser(testUser)).thenReturn(false);

        //method under test
        boolean success = userService.editProfile(testUser);

        assertFalse(success);
    }

    @Test
    public void editProfile_InvalidUser() {
        testUser.setEmail(TestHelper.INVALID_EMAIL);

        thrown.expect(OperationFailedException.class);
        thrown.expectMessage("Invalid user object!");

        //method under test
        userService.editProfile(testUser);
    }

    @Test
    public void verifyAccount_Success() {
        when(userDAO.updateVerifiedStatus(anyInt())).thenReturn(true);

        //method under test
        boolean success = userService.verifyAccount(1);
        assertTrue(success);
    }

    @Test
    public void verifyAccount_UserNotFound() {
        when(userDAO.updateVerifiedStatus(anyInt())).thenReturn(false);

        //method under test
        boolean success = userService.verifyAccount(1);

        assertFalse(success);
    }

    @Test
    public void verifyAccount_InvalidUserId() {
        thrown.expect(OperationFailedException.class);
        thrown.expectMessage("Invalid user id!");

        //method under test
        userService.verifyAccount(TestHelper.INVALID_ID);
    }

    @Test
    public void login_Success() {
        String email = testUser.getEmail();
        String notEncryptedPassword = testUser.getPassword();

        //encrypt password
        testUser.setPassword(HashGeneratorUtil.generateHashString(notEncryptedPassword));

        when(userDAO.getUserByEmail(testUser.getEmail())).thenReturn(testUser);

        //method under test
        User user = userService.login(email, notEncryptedPassword);

        assertEqualUsers(user, testUser);
    }

    @Test
    public void login_InvalidEmail() {
        thrown.expect(OperationFailedException.class);
        thrown.expectMessage("Invalid email!");

        //method under test
        userService.login(TestHelper.INVALID_EMAIL, "password");
    }

    @Test
    public void login_UserNotFound() {
        when(userDAO.getUserByEmail(anyString())).thenReturn(null);
        thrown.expect(OperationFailedException.class);
        thrown.expectMessage("Invalid email/password combination!");

        //method under test
        User user = userService.login(testUser.getEmail(), "password");

        assertNull(user);
    }

    @Test
    public void login_InvalidPassword() {
        when(userDAO.getUserByEmail(anyString())).thenReturn(testUser);
        thrown.expect(OperationFailedException.class);
        thrown.expectMessage("Invalid email/password combination!");

        //method under test
        User user = userService.login(testUser.getEmail(), "password");

        assertNull(user);
    }
}
