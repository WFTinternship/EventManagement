package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.User;
import org.junit.*;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualUsers;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 7/8/16.
 */
public class UserDAOIntegrationTest {

    private static UserDAO userDAO;
    private User testUser;

    @BeforeClass
    public static void setUpClass() throws DAOException {
        userDAO = new UserDAOImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        userDAO = null;
    }

    @Before
    public void setUp() throws DAOException, DuplicateEntryException {
        //create test user
        testUser = TestObjectCreator.createTestUser();

        //insert test user into db, get generated id
        int userId = userDAO.addUser(testUser);

        //set id to test user
        testUser.setId(userId);
    }

    @After
    public void tearDown() throws DAOException {
        //delete inserted test users from db
        userDAO.deleteAllUsers();

        //delete test user object
        testUser = null;
    }

    @Test
    public void addUser_Success() throws DAOException, ObjectNotFoundException {
        //test user already inserted in setup, get record by userId
        User user = userDAO.getUserById(testUser.getId());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test(expected = DuplicateEntryException.class)
    public void addUser_Duplicate_Entry() throws DuplicateEntryException, DAOException {
        //method under test, insert duplicate user
        userDAO.addUser(testUser);
    }

    @Test
    public void getAllUsers_Found() throws DAOException {
        //method under test
        List<User> userList = userDAO.getAllUsers();

        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        assertEquals(userList.size(), 1);
        assertEqualUsers(userList.get(0), testUser);
    }

    @Test
    public void getAllUsers_Empty_List() throws DAOException, ObjectNotFoundException {
        //delete inserted user from db
        userDAO.deleteUser(testUser.getId());

        //method under test
        List<User> userList = userDAO.getAllUsers();

        assertNotNull(userList);
        assertTrue(userList.isEmpty());
    }

    @Test
    public void getUserById_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        User user = userDAO.getUserById(testUser.getId());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getUserById_Not_Found() throws DAOException, ObjectNotFoundException {
        //method under test
        userDAO.getUserById(TestObjectCreator.NON_EXISTING_ID);
    }

    @Test
    public void getUserByEmail_Found() throws DAOException, ObjectNotFoundException {
        //method under test
        User user = userDAO.getUserByEmail(testUser.getEmail());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getUserByEmail_Not_Fount() throws DAOException, ObjectNotFoundException {
        //method under test
        userDAO.getUserByEmail(TestObjectCreator.NON_EXISTING_EMAIL);
    }

    @Test
    public void updateVerifiedStatus_Success() throws DAOException, ObjectNotFoundException {
        //method under test
        userDAO.updateVerifiedStatus(testUser.getId());

        //read updated record from db
        User user = userDAO.getUserById(testUser.getId());

        assertNotNull(user);
        assertTrue(user.isVerified());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateVerifiedStatus_Not_Found() throws DAOException, ObjectNotFoundException {
        //method under test
        userDAO.updateVerifiedStatus(TestObjectCreator.NON_EXISTING_ID);
    }

    @Test
    public void updateUser_Success() throws DAOException, DuplicateEntryException, ObjectNotFoundException {
        //create new user with the same id
        User updatedUser = TestObjectCreator.createTestUser();
        updatedUser.setId(testUser.getId());

        //test method
        userDAO.updateUser(updatedUser);

        //read updated record from db
        User user = userDAO.getUserById(updatedUser.getId());

        assertNotNull(user);
        assertEqualUsers(user, updatedUser);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateUser_Not_Found() throws DAOException, DuplicateEntryException, ObjectNotFoundException {
        //create new user without id
        User updatedUser = TestObjectCreator.createTestUser();

        //method under test
        userDAO.updateUser(updatedUser);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteUser_Success() throws DAOException, ObjectNotFoundException {
        //method under test
        userDAO.deleteUser(testUser.getId());

        //non-existing user, method should throw ObjectNotFoundException
        userDAO.getUserById(testUser.getId());
    }


    @Test(expected = ObjectNotFoundException.class)
    public void deleteUser_Not_Found() throws DAOException, ObjectNotFoundException {
        //method under test
        userDAO.deleteUser(TestObjectCreator.NON_EXISTING_ID);
    }


    @Test
    public void deleteAllUsers_Success() throws DAOException {
        //method under test
        userDAO.deleteAllUsers();

        List<User> userList = userDAO.getAllUsers();
        assertTrue(userList.isEmpty());
    }

    @Test
    public void deleteAllUsers_Not_Found() throws DAOException, ObjectNotFoundException {
        //delete inserted user
        userDAO.deleteUser(testUser.getId());

        //method under test
        userDAO.deleteAllUsers();
    }

    private void assertUserLists(List<User> expectedList, List<User> actualList) {
        assertEquals(actualList.size(), expectedList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEqualUsers(actualList.get(i), expectedList.get(i));
        }
    }


}
