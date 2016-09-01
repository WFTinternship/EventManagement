package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualUsers;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    private User testUser;

    @Before
    public void setUp() {
        //create test user
        testUser = createTestUser();

        userService.addAccount(testUser);
    }

    @After
    public void tearDown() {
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

        User actualUser = userService.getUserById(testUser.getId());
        assertNotNull(actualUser);
        assertEqualUsers(actualUser, testUser);
    }

    @Test(expected = OperationFailedException.class)
    public void addAccount_DuplicateEmail() {
        //method under test, insert duplicate email
        userService.addAccount(testUser);
    }

    @Test
    public void getUserById_Found() {
        //testing method
        User user = userService.getUserById(testUser.getId());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getUserById_Not_Found() {
        //method under test
        userService.getUserById(NON_EXISTING_ID);
    }

    @Test
    public void getUserByEmail_Found() {
        //method under test
        User user = userService.getUserByEmail(testUser.getEmail());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test
    public void getUserByEmail_NotFound() {
        //method under test
        User user = userService.getUserByEmail(NON_EXISTING_EMAIL);
        assertNull(user);
    }

    @Test
    public void updateVerifiedStatus_Success() {
        //method under test
        userService.verifyAccount(testUser.getId());

        //read updated record from db
        User user = userService.getUserById(testUser.getId());

        assertNotNull(user);
        assertTrue(user.isVerified());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateVerifiedStatus_NotFound() {
        //method under test
        userService.verifyAccount(NON_EXISTING_ID);
    }

    @Test
    public void updateUser_Success() {
        //create new user with the same id
        User updatedUser = createTestUser();
        updatedUser.setId(testUser.getId());

        //test method
        userService.editAccount(updatedUser);

        //read updated record from db
        User user = userService.getUserById(updatedUser.getId());

        assertNotNull(user);
        assertEqualUsers(user, updatedUser);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateUser_NotFound() {
        //create new user without id
        User updatedUser = createTestUser();

        //method under test
        userService.editAccount(updatedUser);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteAccount_Success() {

        boolean success = userService.deleteAccount(testUser.getId());
        User actualUser = userService.getUserById(testUser.getId());

        assertTrue(success);
        assertNotNull(actualUser);
    }

    @Test
    public void deleteAccount_NotFound() {

        boolean success = userService.deleteAccount(NON_EXISTING_ID);
        assertFalse(success);
    }

    @Test
    public void getAllUsers_Found() {
        //method under test
        List<User> userList = userService.getAllUsers();

        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        assertEqualUsers(userList.get(0), testUser);
    }

    @Test
    public void getAllUsers_EmptyList() {
        //delete inserted user from db
        userService.deleteAccount(testUser.getId());

        //method under test
        List<User> userList = userService.getAllUsers();

        assertNotNull(userList);
        assertTrue(userList.isEmpty());
    }

    @Test
    public void deleteAllUsers_Success() {
        //method under test
        userService.deleteAllUsers();

        List<User> userList = userService.getAllUsers();
        assertTrue(userList.isEmpty());
    }

    @Test
    public void deleteAllUsers_NotFound() {
        //delete inserted user
        userService.deleteAccount(testUser.getId());

        //method under test
        userService.deleteAllUsers();
    }
}
