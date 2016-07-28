package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestHelper;
import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.util.HashGenerator;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import static com.workfront.internship.event_management.TestHelper.assertEqualUsers;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
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

    //Testing addAccount method
    @Test(expected = OperationFailedException.class)
    public void addAccount_InvalidUser() {
        testUser.setEmail(TestHelper.INVALID_EMAIL);

        //method under test
        userService.addAccount(testUser);
    }

    @Test
    public void addAccount_EncryptPassword() throws DuplicateEntryException, DAOException {
        when(userDAO.addUser(testUser)).thenReturn(1);
        when(emailService.sendVerificationEmail(testUser)).thenReturn(true);

        String expectedPassword = HashGenerator.generateHashString(testUser.getPassword());

        //method under test
        userService.addAccount(testUser);

        String actualPassword = testUser.getPassword();
        assertEquals("Unable to hash password.", actualPassword, expectedPassword);
    }

    @Test(expected = OperationFailedException.class)
    public void addAccount_InsertFailed_DuplicateUser() throws DuplicateEntryException, DAOException {
        when(userDAO.addUser(testUser)).thenThrow(DuplicateEntryException.class);

        //method under test
        userService.addAccount(testUser);
    }

    @Test(expected = OperationFailedException.class)
    public void addAccount_InsertFailed_DBError() throws DuplicateEntryException, DAOException {
        when(userDAO.addUser(testUser)).thenThrow(DAOException.class);

        //method under test
        userService.addAccount(testUser);
    }

    @Test
    public void addAccount_EmailSent_Success() throws DuplicateEntryException, DAOException {
        when(userDAO.addUser(testUser)).thenReturn(TestHelper.VALID_ID);
        when(emailService.sendVerificationEmail(testUser)).thenReturn(true);

        //method under test
        userService.addAccount(testUser);

        Mockito.verify(emailService).sendVerificationEmail(testUser);
    }

    @Test(expected = OperationFailedException.class)
    public void addAccount_EmailSent_Fail() throws DuplicateEntryException, DAOException {
        when(userDAO.addUser(testUser)).thenReturn(TestHelper.VALID_ID);
        when(emailService.sendVerificationEmail(testUser)).thenReturn(false);

        //method under test
        userService.addAccount(testUser);

        Mockito.verify(emailService).sendVerificationEmail(testUser);
    }

    //Testing editAccount method
    @Test(expected = OperationFailedException.class)
    public void editAccount_InvalidUser() {
        testUser.setEmail(TestHelper.INVALID_EMAIL);

        //method under test
        userService.editAccount(testUser);
    }

    @Test
    public void editAccount_Success() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        //method under test
        userService.editAccount(testUser);

        Mockito.verify(userDAO).updateUser(testUser);
    }

    @Test(expected = OperationFailedException.class)
    public void editAccount_UserNotFound() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(ObjectNotFoundException.class).when(userDAO).updateUser(testUser);

        //method under test
        userService.editAccount(testUser);
    }

    @Test(expected = OperationFailedException.class)
    public void editAccount_DuplicateUser() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(DuplicateEntryException.class).when(userDAO).updateUser(testUser);

        //method under test
        userService.editAccount(testUser);
    }

    @Test(expected = OperationFailedException.class)
    public void editAccount_DBError() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(DAOException.class).when(userDAO).updateUser(testUser);

        //method under test
        userService.editAccount(testUser);
    }

    //Testing verifyAccount method
    @Test(expected = OperationFailedException.class)
    public void verifyAccount_InvalidUserId() throws DAOException, ObjectNotFoundException {
        //method under test
        userService.verifyAccount(TestHelper.INVALID_ID);

        Mockito.verify(userDAO).updateVerifiedStatus(TestHelper.INVALID_ID);
    }

    @Test
    public void verifyAccount_Success() throws DAOException, ObjectNotFoundException {
        //method under test
        userService.verifyAccount(TestHelper.VALID_ID);

        Mockito.verify(userDAO).updateVerifiedStatus(TestHelper.VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void verifyAccount_UserNotFound() throws DAOException, ObjectNotFoundException {
        doThrow(ObjectNotFoundException.class).when(userDAO).updateVerifiedStatus(anyInt());

        //method under test
        userService.verifyAccount(TestHelper.VALID_ID);

        Mockito.verify(userDAO).updateVerifiedStatus(TestHelper.VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void verifyAccount_DBError() throws DAOException, ObjectNotFoundException {
        doThrow(DAOException.class).when(userDAO).updateVerifiedStatus(anyInt());

        //method under test
        userService.verifyAccount(TestHelper.VALID_ID);

        Mockito.verify(userDAO).updateVerifiedStatus(TestHelper.VALID_ID);
    }

    // Testing login method
    @Test(expected = OperationFailedException.class)
    public void login_InvalidEmailForm() {
        //method under test
        userService.login(TestHelper.INVALID_EMAIL, testUser.getPassword());
    }

    @Test(expected = OperationFailedException.class)
    public void login_EmptyEmail() {
        //method under test
        userService.login("", testUser.getPassword());
    }

    @Test(expected = OperationFailedException.class)
    public void login_EmptyPassword() {
        //method under test
        userService.login(testUser.getEmail(), "");
    }

    @Test(expected = OperationFailedException.class)
    public void login_NullEmail() {
        //method under test
        userService.login(null, testUser.getPassword());
    }

    @Test(expected = OperationFailedException.class)
    public void login_NullPassword() {
        //method under test
        userService.login(testUser.getEmail(), null);
    }

    @Test(expected = OperationFailedException.class)
    public void login_DBError() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(userDAO).getUserByEmail(testUser.getEmail());

        //method under test
        userService.login(testUser.getEmail(), testUser.getPassword());
    }

    @Test(expected = OperationFailedException.class)
    public void login_NonExistingEmail() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(userDAO).getUserByEmail(TestHelper.NON_EXISTING_EMAIL);

        //method under test
        userService.login(TestHelper.NON_EXISTING_EMAIL, testUser.getPassword());
    }

    @Test(expected = OperationFailedException.class)
    public void login_WrongPassword() throws ObjectNotFoundException, DAOException {
        when(userDAO.getUserByEmail(testUser.getEmail())).thenReturn(testUser);

        //method under test
        userService.login(testUser.getEmail(), TestHelper.WRONG_PASSWORD);
    }

    @Test
    public void login_Success() throws ObjectNotFoundException, DAOException {
        String notEncryptedPassword = testUser.getPassword();

        //set encrypted password to test user
        testUser.setPassword(HashGenerator.generateHashString(testUser.getPassword()));

        when(userDAO.getUserByEmail(testUser.getEmail())).thenReturn(testUser);

        //method under test
        User actualUser = userService.login(testUser.getEmail(), notEncryptedPassword);

        assertEqualUsers(testUser, actualUser);
    }

    //Testing deleteAccount method
    @Test(expected = OperationFailedException.class)
    public void deleteAccount_InvalidUserId() {
        //method under test
        userService.deleteAccount(TestHelper.INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteAccount_DBError() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(userDAO).deleteUser(TestHelper.VALID_ID);

        //method under test
        userService.deleteAccount(TestHelper.VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteAccount_UserNotFound() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(userDAO).deleteUser(TestHelper.NON_EXISTING_ID);

        //method under test
        userService.deleteAccount(TestHelper.NON_EXISTING_ID);
    }

    @Test
    public void deleteAccount_Success() throws ObjectNotFoundException, DAOException {
        // TODO: 7/29/16 implement
    }

    //Testing getUserById method
    @Test(expected = OperationFailedException.class)
    public void getUserById_InvalidUserId() {
        //method under test
        userService.getUserById(TestHelper.INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getUserById_DBError() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(userDAO).getUserById(TestHelper.VALID_ID);

        //method under test
        userService.getUserById(TestHelper.VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getUserById_UserNotFound() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(userDAO).getUserById(TestHelper.NON_EXISTING_ID);

        //method under test
        userService.getUserById(TestHelper.NON_EXISTING_ID);
    }

    @Test
    public void getUserById_Success() throws ObjectNotFoundException, DAOException {
        when(userDAO.getUserById(TestHelper.VALID_ID)).thenReturn(testUser); // TODO: 7/29/16 check
    }

    //Testing getUserByEmail method
    @Test(expected = OperationFailedException.class)
    public void getUserByEmail_InvalidEmailForm() {
        //method under test
        userService.getUserByEmail(TestHelper.INVALID_EMAIL);
    }

    @Test(expected = OperationFailedException.class)
    public void getUserByEmail_DBError() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(userDAO).getUserByEmail(testUser.getEmail());

        //method under test
        userService.getUserByEmail(testUser.getEmail());
    }

    @Test(expected = OperationFailedException.class)
    public void getUserByEmail_UserNotFound() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(userDAO).getUserByEmail(TestHelper.NON_EXISTING_EMAIL);

        //method under test
        userService.getUserByEmail(TestHelper.NON_EXISTING_EMAIL);
    }

    @Test
    public void getUserByEmail_Success() throws ObjectNotFoundException, DAOException {
        when(userDAO.getUserByEmail(testUser.getEmail())).thenReturn(testUser); // TODO: 7/29/16 check
    }

}
