import com.workfront.internship.event_management.datasource.UserDAO;
import com.workfront.internship.event_management.datasource.UserDAOImpl;
import com.workfront.internship.event_management.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Hermine Turshujyan 7/8/16.
 */
public class TestUserDAOImpl {

    private static UserDAO userDAO;
    private User testUser;

    @BeforeClass
    public static void setUpClass() {
        userDAO = new UserDAOImpl();
    }

    @Before
    public void setUp() {
        //create test user
        testUser = TestHelper.createTestUser();

        //insert test user into db, get generated id
        int userId = userDAO.insertUser(testUser);

        //set id to test user
        testUser.setId(userId);
    }

    @After
    public void tearDown() {
        //delete test users from db
        userDAO.deleteAllUsers();

        testUser = null;
    }

    @Test
    public void insertUser_Inserted() {

        //test user already inserted in setup, read record by userId
        User user = userDAO.getUserById(testUser.getId());

        assertUsers(user, testUser);
    }

    @Test(expected = RuntimeException.class)
    public void insertUser_Dublicate_Entry() {
        //test user already inserted into db, insert dublicate user
        userDAO.insertUser(testUser);
    }

    @Test
    public void getAllUsers_Found() {

        //insert second user into db
        User testUser1 = TestHelper.createTestUser();
        int userId = userDAO.insertUser(testUser1);
        testUser1.setId(userId);

        //test method
        List<User> userList = userDAO.getAllUsers();

        assertEquals(userList.size(), 2);
        assertUsers(userList.get(0), testUser);
        assertUsers(userList.get(1), testUser1);
    }

    @Test
    public void getAllUsers_Empty_List() {

        //delete inserted user from db
        userDAO.deleteUser(testUser.getId());

        //test method
        List<User> userList = userDAO.getAllUsers();
        assertTrue(userList.isEmpty());
    }

    @Test
    public void getUserById_Not_Found() {
        //testing method
        User user = userDAO.getUserById(TestHelper.NON_EXISTING_ID);

        assertNull(user);
    }

    @Test
    public void getUserById_Found() {
        //testing method
        User user = userDAO.getUserById(testUser.getId());

        assertUsers(user, testUser);
    }

    @Test
    public void getUserByUsername_Found() {
        //testing method
        User user = userDAO.getUserByUsername(testUser.getUsername());
        assertUsers(user, testUser);
    }

    @Test
    public void getUserByUsername_Not_Found() {
        //testing method
        User user = userDAO.getUserByUsername(TestHelper.NON_EXISTING_USERNAME);

        assertNull(user);
    }

    @Test
    public void getUserByEmail_Found() {
        //testing method
        User user = userDAO.getUserByEmail(testUser.getEmail());

        assertUsers(user, testUser);
    }

    @Test
    public void getUserByEmail_Not_Fount() {
        //testing method
        User user = userDAO.getUserByEmail(TestHelper.NON_EXISTING_EMAIL);

        assertNull(user);
    }

    @Test
    public void setVerified() {
        //testing method
        userDAO.setVerified(testUser.getId());

        //read updated record from db
        User actualUser = userDAO.getUserById(testUser.getId());

        assertTrue(actualUser.isVerified());
    }

    @Test
    public void updateUser() {
        //create new user with the same id
        User updatedUser = TestHelper.createTestUser();
        updatedUser.setId(testUser.getId());

        //test method
        userDAO.updateUser(updatedUser);

        //read updated record from db
        User user = userDAO.getUserById(updatedUser.getId());

        assertUsers(user, updatedUser);
    }

    @Test
    public void deleteUser() {
        //testing method
        userDAO.deleteUser(testUser.getId());

        User user = userDAO.getUserById(testUser.getId());
        assertNull(user);
    }

    @Test
    public void deleteAllUsers() {
        //testing method
        userDAO.deleteAllUsers();

        List<User> userList = userDAO.getAllUsers();
        assertTrue(userList.isEmpty());
    }

    //helper methods
    private void assertUsers(User expectedUser, User actualUser) {
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.getPhoneNumber(), actualUser.getPhoneNumber());
        assertEquals(expectedUser.getAvatarPath(), actualUser.getAvatarPath());
        assertEquals(expectedUser.isVerified(), actualUser.isVerified());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertNotNull(expectedUser.getEmail());
    }
}
