package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.TestHelper;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.User;
import org.junit.*;

import java.util.List;

import static com.workfront.internship.event_management.TestHelper.assertEqualUsers;
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
        testUser = TestHelper.createTestUser();

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
    public void addUser_Sucess() throws DAOException, ObjectNotFoundException {
        //test user already inserted in setup, read record by userId
        User user = userDAO.getUserById(testUser.getId());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test(expected = DuplicateEntryException.class)
    public void addUser_Duplicate_Entry() throws DuplicateEntryException, DAOException {
        //test user already inserted into db, insert duplicate user
        userDAO.addUser(testUser);
    }

    @Test
    public void getAllUsers_Found() throws DAOException {
        //test method
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

        //test method
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
        //testing method
        User user = userDAO.getUserById(TestHelper.NON_EXISTING_ID);

        assertNull(user);
    }

    @Test
    public void getUserByEmail_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        User user = userDAO.getUserByEmail(testUser.getEmail());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getUserByEmail_Not_Fount() throws DAOException, ObjectNotFoundException {
        //testing method
        User user = userDAO.getUserByEmail(TestHelper.NON_EXISTING_EMAIL);

        assertNull(user);
    }

    @Test
    public void updateVerifiedStatus_Success() throws DAOException, ObjectNotFoundException {
        //testing method
        userDAO.updateVerifiedStatus(testUser.getId());

        //read updated record from db
        User user = userDAO.getUserById(testUser.getId());

        assertNotNull(user);
        assertTrue(user.isVerified());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateVerifiedStatus_Not_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        userDAO.updateVerifiedStatus(TestHelper.NON_EXISTING_ID);
    }

    @Test
    public void updateUser_Success() throws DAOException, DuplicateEntryException, ObjectNotFoundException {
        //create new user with the same id
        User updatedUser = TestHelper.createTestUser();
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
        User updatedUser = TestHelper.createTestUser();

        //test method
        userDAO.updateUser(updatedUser);
    }

    @Test
    public void deleteUser_Success() throws DAOException, ObjectNotFoundException {
        //test method
        userDAO.deleteUser(testUser.getId());

        User user = userDAO.getUserById(testUser.getId());

        assertNull(user);
    }


    @Test(expected = ObjectNotFoundException.class)
    public void deleteUser_Not_Found() throws DAOException, ObjectNotFoundException {
        //test method
        userDAO.deleteUser(TestHelper.NON_EXISTING_ID);
    }


    @Test
    public void deleteAllUsers_Success() throws DAOException {
        //test method
        userDAO.deleteAllUsers();

        List<User> userList = userDAO.getAllUsers();
        assertTrue(userList.isEmpty());
    }

    @Test
    public void deleteAllUsers_Not_Found() throws DAOException, ObjectNotFoundException {
        //delete inserted user
        userDAO.deleteUser(testUser.getId());

        //test method
        userDAO.deleteAllUsers();
    }

    private void assertUserLists(List<User> expectedList, List<User> actualList) {
        assertEquals(actualList.size(), expectedList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEqualUsers(actualList.get(i), expectedList.get(i));
        }
    }


}
