package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.model.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    private User testUser;


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
    public void addAccount_Success() {
        // EmailService emailService = Mockito.mock(EmailServiceImpl.class);
        // Whitebox.setInternalState(userService, "emailService", emailService);
        //  when(emailService.sendVerificationEmail(testUser)).thenReturn(true);

        userService.addAccount(testUser);

        User actualUser = userService.getUserById(testUser.getId());
        assertNotNull(actualUser);
    }
}
