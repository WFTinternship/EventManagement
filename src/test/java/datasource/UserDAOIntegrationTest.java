package datasource;

import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
import com.workfront.internship.event_management.model.User;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by Hermine Turshujyan 7/8/16.
 */
public class UserDAOIntegrationTest {

    private static UserDAO userDAO;
    private User testUser;

    @BeforeClass
    public static void setUpClass() {
        userDAO = new UserDAOImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        userDAO = null;
    }

    @Before
    public void setUp() {
        //create test user
        testUser = TestHelper.createTestUser();

        //insert test user into db, get generated id
        int userId = userDAO.addUser(testUser);

        //set id to test user
        testUser.setId(userId);
    }

    @After
    public void tearDown() {
        //delete inserted test users from db
        userDAO.deleteAllUsers();

        //delete test user object
        testUser = null;
    }

    @Test
    public void insertUser_Sucess() {
        //test user already inserted in setup, read record by userId
        User user = userDAO.getUserById(testUser.getId());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test(expected = RuntimeException.class)
    public void insertUser_Dublicate_Entry() {
        //test user already inserted into db, insert dublicate user
        userDAO.addUser(testUser); //username, email fields in db are unique
    }

    @Test
    public void getAllUsers_Found() {
        //create test users list, insert into db
        List<User> testUserList = createTestUserList();

        //test method
        List<User> userList = userDAO.getAllUsers();

        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        assertUserLists(userList, testUserList);
    }

    @Test
    public void getAllUsers_Empty_List() {
        //delete inserted user from db
        userDAO.deleteUser(testUser.getId());

        //test method
        List<User> userList = userDAO.getAllUsers();

        assertNotNull(userList);
        assertTrue(userList.isEmpty());
    }

    @Test
    public void getUserById_Found() {
        //testing method
        User user = userDAO.getUserById(testUser.getId());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test
    public void getUserById_Not_Found() {
        //testing method
        User user = userDAO.getUserById(TestHelper.NON_EXISTING_ID);

        assertNull(user);
    }

    @Test
    public void getUserByUsername_Found() {
        //testing method
        User user = userDAO.getUserByUsername(testUser.getUsername());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
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

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test
    public void getUserByEmail_Not_Fount() {
        //testing method
        User user = userDAO.getUserByEmail(TestHelper.NON_EXISTING_EMAIL);

        assertNull(user);
    }

    @Test
    public void updateVerifiedStatus_Success() {
        //testing method
        boolean updated = userDAO.updateVerifiedStatus(testUser.getId());

        //read updated record from db
        User user = userDAO.getUserById(testUser.getId());

        assertNotNull(user);
        assertTrue(user.isVerified());
        assertTrue(updated);
    }

    @Test
    public void updateVerifiedStatus_Not_Found() {
        //testing method
        boolean updated = userDAO.updateVerifiedStatus(TestHelper.NON_EXISTING_ID);

        assertFalse(updated);
    }

    @Test
    public void updateUser_Success() {
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

    @Test
    public void updateUser_Not_Found() {
        //create new user without id
        User updatedUser = TestHelper.createTestUser();

        //test method
        boolean updated = userDAO.updateUser(updatedUser);

        assertFalse(updated);
    }

    @Test
    public void deleteUser_Success() {
        //test method
        boolean deleted = userDAO.deleteUser(testUser.getId());

        User user = userDAO.getUserById(testUser.getId());

        assertTrue(deleted);
        assertNull(user);
    }


    @Test
    public void deleteUser_Not_Found() {
        //test method
        boolean deleted = userDAO.deleteUser(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }


    @Test
    public void deleteAllUsers_Success() {
        //test method
        boolean deleted = userDAO.deleteAllUsers();

        List<User> userList = userDAO.getAllUsers();
        assertTrue(userList.isEmpty());
        assertTrue(deleted);
    }

    @Test
    public void deleteAllUsers_Not_Found() {
        //delete inserted user
        userDAO.deleteUser(testUser.getId());

        //test method
        boolean deleted = userDAO.deleteAllUsers();

        assertFalse(deleted);
    }

    //helper methods
    private void assertEqualUsers(User expectedUser, User actualUser) {
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

    private void assertUserLists(List<User> expectedList, List<User> actualList) {
        assertEquals(actualList.size(), expectedList.size());
        for(int i = 0; i < actualList.size(); i++) {
            assertEqualUsers(actualList.get(i), expectedList.get(i));
        }
    }

    private List<User> createTestUserList(){
        //create second test category
        User secondTestUser = TestHelper.createTestUser();

        //insert second category into db
        int userId = userDAO.addUser(secondTestUser);
        secondTestUser.setId(userId);

        //create test media list
        List<User> testUserList = new ArrayList<>();
        testUserList.add(testUser);
        testUserList.add(secondTestUser);

        return testUserList;
    }
}
