package datasource;

import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.exception.DuplicateEntryException;
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
    public static void setUpClass() throws DAOException {
        userDAO = new UserDAOImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        userDAO = null;
    }

    @Before
    public void setUp() throws DAOException {
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
    public void insertUser_Sucess() throws DAOException {
        //test user already inserted in setup, read record by userId
        User user = userDAO.getUserById(testUser.getId());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test(expected = DuplicateEntryException.class)
    public void insertUser_Dublicate_Entry() throws DAOException {
        //test user already inserted into db, insert dublicate user
        userDAO.addUser(testUser); //username, email fields in db are unique
    }

    @Test
    public void getAllUsers_Found() throws DAOException {
        //create test users list, insert into db
        List<User> testUserList = createTestUserList();

        //test method
        List<User> userList = userDAO.getAllUsers();

        assertNotNull(userList);
        assertFalse(userList.isEmpty());
        assertUserLists(userList, testUserList);
    }

    @Test
    public void getAllUsers_Empty_List() throws DAOException {
        //delete inserted user from db
        userDAO.deleteUser(testUser.getId());

        //test method
        List<User> userList = userDAO.getAllUsers();

        assertNotNull(userList);
        assertTrue(userList.isEmpty());
    }

    @Test
    public void getUserById_Found() throws DAOException {
        //testing method
        User user = userDAO.getUserById(testUser.getId());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test
    public void getUserById_Not_Found() throws DAOException {
        //testing method
        User user = userDAO.getUserById(TestHelper.NON_EXISTING_ID);

        assertNull(user);
    }

    @Test
    public void getUserByEmail_Found() throws DAOException {
        //testing method
        User user = userDAO.getUserByEmail(testUser.getEmail());

        assertNotNull(user);
        assertEqualUsers(user, testUser);
    }

    @Test
    public void getUserByEmail_Not_Fount() throws DAOException {
        //testing method
        User user = userDAO.getUserByEmail(TestHelper.NON_EXISTING_EMAIL);

        assertNull(user);
    }

    @Test
    public void updateVerifiedStatus_Success() throws DAOException {
        //testing method
        boolean updated = userDAO.updateVerifiedStatus(testUser.getId());

        //read updated record from db
        User user = userDAO.getUserById(testUser.getId());

        assertNotNull(user);
        assertTrue(user.isVerified());
        assertTrue(updated);
    }

    @Test
    public void updateVerifiedStatus_Not_Found() throws DAOException {
        //testing method
        boolean updated = userDAO.updateVerifiedStatus(TestHelper.NON_EXISTING_ID);

        assertFalse(updated);
    }

    @Test
    public void updateUser_Success() throws DAOException {
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
    public void updateUser_Not_Found() throws DAOException {
        //create new user without id
        User updatedUser = TestHelper.createTestUser();

        //test method
        boolean updated = userDAO.updateUser(updatedUser);

        assertFalse(updated);
    }

    @Test
    public void deleteUser_Success() throws DAOException {
        //test method
        boolean deleted = userDAO.deleteUser(testUser.getId());

        User user = userDAO.getUserById(testUser.getId());

        assertTrue(deleted);
        assertNull(user);
    }


    @Test
    public void deleteUser_Not_Found() throws DAOException {
        //test method
        boolean deleted = userDAO.deleteUser(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }


    @Test
    public void deleteAllUsers_Success() throws DAOException {
        //test method
        boolean deleted = userDAO.deleteAllUsers();

        List<User> userList = userDAO.getAllUsers();
        assertTrue(userList.isEmpty());
        assertTrue(deleted);
    }

    @Test
    public void deleteAllUsers_Not_Found() throws DAOException {
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
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.getPhoneNumber(), actualUser.getPhoneNumber());
        assertEquals(expectedUser.getAvatarPath(), actualUser.getAvatarPath());
        assertEquals(expectedUser.isVerified(), actualUser.isVerified());
        assertNotNull(expectedUser.getEmail());
    }

    private void assertUserLists(List<User> expectedList, List<User> actualList) {
        assertEquals(actualList.size(), expectedList.size());
        for(int i = 0; i < actualList.size(); i++) {
            assertEqualUsers(actualList.get(i), expectedList.get(i));
        }
    }

    private List<User> createTestUserList() throws DAOException {
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
