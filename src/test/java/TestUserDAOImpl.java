import com.workfront.internship.event_management.datasource.UserDAO;
import com.workfront.internship.event_management.datasource.UserDAOImpl;
import com.workfront.internship.event_management.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

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

        //insert into db
        int userId = userDAO.insertUser(testUser);

        //set inserted id
        testUser.setId(userId);
    }

    @After
    public void tearDown() {
        //delete test user from db
        userDAO.deleteUser(testUser.getId());

        testUser = null;
    }

    @Test
    public void testInsertUser() {
        //already inserted into db
        assertNotEquals(testUser.getId(), 0);
    }

    @Test //-
    public void testGetAllUsers() {
       /* List<User> expectedUsers = getAllUsersFromDB();
        List<User> actualUsers = userDAO.getAllUsers();
        assertEquals(actualUsers.size(), expectedUsers.size());
        for (int i = 0; i < actualUsers.size(); i++) {
            assertEquals(expectedUsers.get(i).getId(), actualUsers.get(i).getId());
            assertEquals(expectedUsers.get(i).getFirstName(), actualUsers.get(i).getFirstName());
            assertEquals(expectedUsers.get(i).getLastName(), actualUsers.get(i).getLastName());
            assertEquals(expectedUsers.get(i).getUsername(), actualUsers.get(i).getUsername());
            assertEquals(expectedUsers.get(i).getPassword(), actualUsers.get(i).getPassword());
            assertEquals(expectedUsers.get(i).getEmail(), actualUsers.get(i).getEmail());
            assertEquals(expectedUsers.get(i).getPhoneNumber(), actualUsers.get(i).getPhoneNumber());
            assertEquals(expectedUsers.get(i).getAvatarPath(), actualUsers.get(i).getAvatarPath());
            assertEquals(expectedUsers.get(i).isVerified(), actualUsers.get(i).isVerified());
            assertEquals(expectedUsers.get(i).getRegistrationDate(), actualUsers.get(i).getRegistrationDate());
        }*/
    }

    @Test
    public void testGetUserById() {
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
