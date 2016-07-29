package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.model.User;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class UserServiceIntegrationTest {

    private static UserService userService;
    private User testUser;

    @BeforeClass
    public static void setUpClass() throws DAOException {
        userService = new UserServiceImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        userService = null;
    }

    @Before
    public void setUp() throws DAOException, DuplicateEntryException {
        //create test user
        testUser = TestObjectCreator.createTestUser();

    }

    @After
    public void tearDown() throws DAOException {
        //delete inserted test users from db
        userService.deleteAllUsers();

        //delete test user object
        testUser = null;
    }

    @Test
    public void addAccount_() {
        EmailService emailService = Mockito.mock(EmailServiceImpl.class);
        Whitebox.setInternalState(userService, "emailService", emailService);
        when(emailService.sendVerificationEmail(testUser)).thenReturn(true);

        userService.addAccount(testUser);

        User actualUser = userService.getUserById(testUser.getId());
        assertNotNull(actualUser);
    }
}
