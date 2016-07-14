import com.workfront.internship.event_management.datasource.UserDAO;
import com.workfront.internship.event_management.datasource.UserDAOImpl;
import com.workfront.internship.event_management.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by hermine on 7/8/16.
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
    }

    @After
    public void tearDown() {
        //delete test users from db
        userDAO.deleteAllUsers();

        testUser = null;
    }

    @Test
    public void insertUser_Unique_Success() {
        //Testing method
        int userId = userDAO.insertUser(testUser);
        testUser.setId(userId);

        User actualUser = userDAO.getUserById(testUser.getId());
        assertNotEquals(testUser.getId(), 0);
    }

    @Test(expected=RuntimeException.class)
    public void insertUser_Dublicate_ExceptionThrown() {

        //testing method
        int userId = userDAO.insertUser(testUser);
        testUser.setId(userId);

        //insert user with the same unique fields
        userDAO.insertUser(testUser);
    }

    @Test
    public void testGetUserById_ValidId_Found() {
        //testing method
         User actualUser = userDAO.getUserById(testUser.getId());

        assertEquals(actualUser.getId(), testUser.getId());
        assertEquals(actualUser.getId(), testUser.getId());
        assertEquals(actualUser.getFirstName(), testUser.getFirstName());
        assertEquals(actualUser.getLastName(), testUser.getLastName());
        assertEquals(actualUser.getUsername(), testUser.getUsername());
        assertEquals(actualUser.getPassword(), testUser.getPassword());
        assertEquals(actualUser.getEmail(), testUser.getEmail());
    }

    @Test
    public void testGetUserById_InvalidId_NotFound() {
        //testing method
        User actualUser = userDAO.getUserById(123);

        assertNull(actualUser);
    }


    @Test
    public void testGetUserByUsername() {
        //testing method
        User actualUser = userDAO.getUserByUsername(testUser.getUsername());

        assertEquals(actualUser.getId(), testUser.getId());
        assertEquals(actualUser.getFirstName(), testUser.getFirstName());
        assertEquals(actualUser.getLastName(), testUser.getLastName());
        assertEquals(actualUser.getUsername(), testUser.getUsername());
        assertEquals(actualUser.getPassword(), testUser.getPassword());
        assertEquals(actualUser.getEmail(), testUser.getEmail());
    }

    @Test
    public void testGetUserByEmail(){
        //testing method
        User actualUser = userDAO.getUserByEmail(testUser.getEmail());

        assertEquals(actualUser.getId(), testUser.getId());
        assertEquals(actualUser.getFirstName(), testUser.getFirstName());
        assertEquals(actualUser.getLastName(), testUser.getLastName());
        assertEquals(actualUser.getUsername(), testUser.getUsername());
        assertEquals(actualUser.getPassword(), testUser.getPassword());
        assertEquals(actualUser.getEmail(), testUser.getEmail());
    }

    @Test
    public void testSetVerified() {
        //testing method
        userDAO.setVerified(testUser.getId());

        //read updated record from db
        User actualUser = userDAO.getUserById(testUser.getId());

        assertTrue(actualUser.isVerified());
    }

    @Test
    public void testUpdateUser() {
        //create new user
        User updatedUser = new User(testUser);
        updatedUser.setEmail("new_email@test.com");
        updatedUser.setPassword("nes_password");

        //testing method
        userDAO.updateUser(updatedUser);

        //read updated record from db
        User actualUser = userDAO.getUserById(testUser.getId());

        assertEquals(actualUser.getId(), updatedUser.getId());
        assertEquals(actualUser.getFirstName(), updatedUser.getFirstName());
        assertEquals(actualUser.getLastName(), updatedUser.getLastName());
        assertEquals(actualUser.getUsername(), updatedUser.getUsername());
        assertEquals(actualUser.getPassword(), updatedUser.getPassword());
        assertEquals(actualUser.getEmail(), updatedUser.getEmail());
        assertEquals(actualUser.getPhoneNumber(), updatedUser.getPhoneNumber());
        assertEquals(actualUser.getAvatarPath(), updatedUser.getAvatarPath());
        assertEquals(actualUser.isVerified(), updatedUser.isVerified());
    }

    @Test
    public void testDeleteUser() {
        //testing method
        boolean deleted = userDAO.deleteUser(testUser.getId());

        assertTrue(deleted);
    }
}
