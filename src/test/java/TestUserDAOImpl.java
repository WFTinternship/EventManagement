import com.workfront.internship.event_management.datasource.*;
import com.workfront.internship.event_management.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Created by hermine on 7/8/16.
 */
public class TestUserDAOImpl {

    private UserDAO userDAO = new UserDAOImpl();
    private User testUser;
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;

    @Before
    public void setUp() {
        testUser = TestUtil.setUpTestUser();
        try {
            conn = DataSourceManager.getInstance().getConnection();
            insertTestUser();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            deleteTestUser();
            testUser = null;
            userDAO = null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    @Test
    public void testInsertUser() throws SQLException {
        deleteTestUser();
        userDAO.insertUser(testUser);
        User actualUser = getTestUserFromDB();
        testUser.setId(actualUser.getId()); // set correct userid (AI field)
        assertEquals(actualUser.getFirstName(), testUser.getFirstName());
        assertEquals(actualUser.getLastName(), testUser.getLastName());
        assertEquals(actualUser.getUsername(), testUser.getUsername());
        assertEquals(actualUser.getPassword(), testUser.getPassword());
        assertEquals(actualUser.getEmail(), testUser.getEmail());
        assertEquals(actualUser.getPhoneNumber(), testUser.getPhoneNumber());
        assertEquals(actualUser.getAvatarPath(), testUser.getAvatarPath());
        assertFalse(actualUser.isVerified());
        assertNotNull(actualUser.getRegistrationDate());
    }

    @Test
    public void testGetAllUsers() throws SQLException {
        List<User> expectedUsers = getAllUsersFromDB();
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
        }
    }

    @Test
    public void testGetUserById() throws SQLException {
        User actualUser = userDAO.getUserById(testUser.getId());
        assertEquals(actualUser.getId(), testUser.getId());
        assertEquals(actualUser.getFirstName(), testUser.getFirstName());
        assertEquals(actualUser.getLastName(), testUser.getLastName());
        assertEquals(actualUser.getUsername(), testUser.getUsername());
        assertEquals(actualUser.getPassword(), testUser.getPassword());
        assertEquals(actualUser.getEmail(), testUser.getEmail());
        assertEquals(actualUser.getPhoneNumber(), testUser.getPhoneNumber());
        assertEquals(actualUser.getAvatarPath(), testUser.getAvatarPath());
        assertFalse(actualUser.isVerified());
        assertNotNull(actualUser.getRegistrationDate());
    }

    @Test
    public void testGetUserByUsername() throws SQLException {
        User actualUser = userDAO.getUserByUsername(testUser.getUsername());
        assertEquals(actualUser.getId(), testUser.getId());
        assertEquals(actualUser.getFirstName(), testUser.getFirstName());
        assertEquals(actualUser.getLastName(), testUser.getLastName());
        assertEquals(actualUser.getUsername(), testUser.getUsername());
        assertEquals(actualUser.getPassword(), testUser.getPassword());
        assertEquals(actualUser.getEmail(), testUser.getEmail());
        assertEquals(actualUser.getPhoneNumber(), testUser.getPhoneNumber());
        assertEquals(actualUser.getAvatarPath(), testUser.getAvatarPath());
        assertFalse(actualUser.isVerified());
        assertNotNull(actualUser.getRegistrationDate());
    }

    @Test
    public void testGetUserByEmail() throws SQLException {
        User actualUser = userDAO.getUserByEmail(testUser.getEmail());
        assertEquals(actualUser.getId(), testUser.getId());
        assertEquals(actualUser.getFirstName(), testUser.getFirstName());
        assertEquals(actualUser.getLastName(), testUser.getLastName());
        assertEquals(actualUser.getUsername(), testUser.getUsername());
        assertEquals(actualUser.getPassword(), testUser.getPassword());
        assertEquals(actualUser.getEmail(), testUser.getEmail());
        assertEquals(actualUser.getPhoneNumber(), testUser.getPhoneNumber());
        assertEquals(actualUser.getAvatarPath(), testUser.getAvatarPath());
        assertFalse(actualUser.isVerified());
        assertNotNull(actualUser.getRegistrationDate());
    }

    @Test
    public void testSetVerified() throws SQLException {
        userDAO.setVerified(testUser.getId());
        User actualUser = getTestUserFromDB();
        assertTrue(actualUser.isVerified());
    }

    @Test
    public void testUpdateUser() throws SQLException {
        User newUser = testUser;
        newUser.setEmail("new_email@test.com");
        newUser.setPassword("nes_password");

        userDAO.updateUser(newUser);
        User actualUser = getTestUserFromDB();
        assertEquals(actualUser.getId(), newUser.getId());
        assertEquals(actualUser.getFirstName(), newUser.getFirstName());
        assertEquals(actualUser.getLastName(), newUser.getLastName());
        assertEquals(actualUser.getUsername(), newUser.getUsername());
        assertEquals(actualUser.getPassword(), newUser.getPassword());
        assertEquals(actualUser.getEmail(), newUser.getEmail());
        assertEquals(actualUser.getPhoneNumber(), newUser.getPhoneNumber());
        assertEquals(actualUser.getAvatarPath(), newUser.getAvatarPath());
        assertEquals(actualUser.isVerified(), newUser.isVerified());
    }

    @Test
    public void testDeleteUser() throws SQLException {
        userDAO.deleteUser(testUser.getId());
        assertNull(getTestUserFromDB());
    }

    //helper methods
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }
    }

    private void deleteTestUser() throws SQLException {
        String sqlStr = "DELETE FROM user WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
        preparedStatement.setInt(1, testUser.getId());
        preparedStatement.executeUpdate();
    }

    private void insertTestUser() throws SQLException {
        String sqlStr = "INSERT INTO user "
                + "(id, first_name, last_name, username, password, "
                + "email, phone_number, avatar_path, verified) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testUser.getId());
        stmt.setString(2, testUser.getFirstName());
        stmt.setString(3, testUser.getLastName());
        stmt.setString(4, testUser.getUsername());
        stmt.setString(5, testUser.getPassword());
        stmt.setString(6, testUser.getEmail());
        stmt.setString(7, testUser.getPhoneNumber());
        stmt.setString(8, testUser.getAvatarPath());
        stmt.setBoolean(9, testUser.isVerified());
        stmt.executeUpdate();
    }

    private User getTestUserFromDB() throws SQLException {
        String sqlStr = "SELECT * FROM user WHERE username  = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setString(1, testUser.getUsername());
        rs = stmt.executeQuery();
        User actualUser = null;
        while (rs.next()) {
            actualUser = new User();
            actualUser.setId(rs.getInt("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setUsername(rs.getString("username"))
                    .setPassword(rs.getString("password"))
                    .setEmail(rs.getString("email"))
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setVerified(rs.getBoolean("verified"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"));
        }
        return actualUser;
    }

    private List<User> getAllUsersFromDB() throws SQLException {
        List<User> usersList = new ArrayList<User>();
        String sqlStr = "SELECT * FROM user";
        stmt = conn.prepareStatement(sqlStr);
        rs = stmt.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setUsername(rs.getString("username"))
                    .setPassword(rs.getString("password"))
                    .setEmail(rs.getString("email"))
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setVerified(rs.getBoolean("verified"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"));
            usersList.add(user);
        }
        return usersList;
    }

}
