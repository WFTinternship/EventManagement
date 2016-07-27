package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestHelper;
import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.exception.DuplicateEntryException;
import com.workfront.internship.event_management.exception.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.util.HashGeneratorUtil;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import static junit.framework.TestCase.assertEquals;
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
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void addAccount_ValidUser_EncryptPassword() throws OperationFailedException, DAOException {
        String expectedPassword = HashGeneratorUtil.generateHashString(testUser.getPassword());

        //method under test
        userService.addAccount(testUser);

        String actualPassword = testUser.getPassword();
        assertEquals("Unable to hash password.", actualPassword, expectedPassword);
    }

    @Test
    public void addAccount_ValidUser_Inserted_EmailSent() throws OperationFailedException, DAOException {
        when(userDAO.addUser(testUser)).thenReturn(10);
        when(emailService.sendVerificationEmail(testUser)).thenReturn(true);

        //method under test
        userService.addAccount(testUser);

        Mockito.verify(emailService).sendVerificationEmail(testUser);
    }

    @Test
    public void addAccount_ValidUser_Inserted_EmailSent_False() throws OperationFailedException, DAOException {
        when(userDAO.addUser(testUser)).thenReturn(10);
        when(emailService.sendVerificationEmail(testUser)).thenReturn(false);

        thrown.expect(OperationFailedException.class);
        thrown.expectMessage("Unable to send verification email!");

        //method under test
        userService.addAccount(testUser);

        Mockito.verify(emailService).sendVerificationEmail(testUser);
    }

    @Test //(expected = OperationFailedException.class)
    public void addAccount_Insert_Fail_Throws_DuplicateException() throws OperationFailedException, DAOException {
        when(userDAO.addUser(testUser)).thenThrow(DuplicateEntryException.class);

        thrown.expect(OperationFailedException.class);
        thrown.expectMessage("User with this email already exists!");

        //test method
        userService.addAccount(testUser);
    }

    @Test
    public void addAccount_Insert_Fail_Throws_DAOException() throws OperationFailedException, DAOException {
        when(userDAO.addUser(testUser)).thenThrow(DAOException.class);

        thrown.expect(OperationFailedException.class);
        thrown.expectMessage("Database error!");

        //test method
        userService.addAccount(testUser);
    }

    @Test
    public void addAccount_InvalidUser() throws OperationFailedException {
        testUser.setEmail(TestHelper.INVALID_EMAIL);

        thrown.expect(OperationFailedException.class);
        thrown.expectMessage("Invalid user object!");

        //method under test
        userService.addAccount(testUser);
    }
}
