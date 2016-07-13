import com.workfront.internship.event_management.datasource.DataSourceManager;
import com.workfront.internship.event_management.datasource.EventInvitationDAO;
import com.workfront.internship.event_management.datasource.EventInvitationDAOImpl;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventCategory;
import com.workfront.internship.event_management.model.EventInvitation;
import com.workfront.internship.event_management.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by hermine on 7/9/16.
 */
public class TestEventInvitationDAOImpl {

    private static EventInvitationDAO invitationDAO;
    private EventInvitation testInvitation;
    private User testUser;
    private Event testEvent;
    private EventCategory testCategory;

    @BeforeClass
    public static void setUpClass() {
        invitationDAO = new EventInvitationDAOImpl();
    }

    @Before
    public void setUp() {

        testUser = TestHelper.createTestUser();
        testCategory = TestHelper.createTestCategory();
        testEvent = TestHelper.createTestEvent();
        testInvitation = TestHelper.createTestInvitation();

        int userId = TestHelper.insertTestUserToDB(testUser);
        testUser.setId(userId);

        int categoryId = TestHelper.insertTestCategoryToDB(testCategory);
        testCategory.setId(categoryId);

        testEvent.setCategory(testCategory);
        int eventId = TestHelper.insertTestEventToDB(testEvent);
        testEvent.setId(eventId);

        testInvitation.setUser(testUser);
        testInvitation.setEventId(testEvent.getId());
        int invId = TestHelper.insertTestInvitationToDB(testInvitation);
        testInvitation.setId(invId);
    }

    @After
    public void tearDown() {
        //deleting test records from db
        TestHelper.deleteTestInvitationFromDB(testInvitation.getId());
        TestHelper.deleteTestEventFromDB(testEvent.getId());
        TestHelper.deleteTestUserFromDB(testUser.getId());
        TestHelper.deleteTestCategoryFromDB(testCategory.getId());

        testUser = null;
        testCategory = null;
        testEvent = null;
        testInvitation = null;
    }

    @Test
    public void testInsertInvitation(){
        TestHelper.deleteTestInvitationFromDB(testInvitation.getId());
        int newInvId = invitationDAO.insertInvitation(testInvitation);
        EventInvitation actualInvitation = getTestInvitationFromDB(newInvId);

        try {
            assertEquals(actualInvitation.getEventId(), testInvitation.getEventId());
            assertEquals(actualInvitation.getUserRole(), testInvitation.getUserRole());
            assertEquals(actualInvitation.getUserResponse(), testInvitation.getUserResponse());
            assertEquals(actualInvitation.getAttendeesCount(), testInvitation.getAttendeesCount());
            assertEquals(actualInvitation.isParticipated(), testInvitation.isParticipated());
        } finally {
            TestHelper.deleteTestInvitationFromDB(newInvId);
        }
    }

    @Test //---
    public void testInsertInvitationsList() {
    }

    @Test
    public void testGetInvitationsByEventId() {
        List<EventInvitation> invitations = invitationDAO.getInvitationsByEventId(testEvent.getId());

        assertEquals(invitations.size(), 1);
        EventInvitation actualInvitation = invitations.get(0);
        assertEquals(actualInvitation.getEventId(), testInvitation.getEventId());
        assertEquals(actualInvitation.getUser().getId(), testInvitation.getUser().getId());
        assertEquals(actualInvitation.getUserRole(), testInvitation.getUserRole());
        assertEquals(actualInvitation.getUserResponse(), testInvitation.getUserResponse());
        assertEquals(actualInvitation.getAttendeesCount(), testInvitation.getAttendeesCount());
        assertEquals(actualInvitation.isParticipated(), testInvitation.isParticipated());
    }

    @Test
    public void testGetInvitationById() {
        EventInvitation actualInvitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertEquals(actualInvitation.getEventId(), testInvitation.getEventId());
        assertEquals(actualInvitation.getUser().getId(), testInvitation.getUser().getId());
        assertEquals(actualInvitation.getUserRole(), testInvitation.getUserRole());
        assertEquals(actualInvitation.getUserResponse(), testInvitation.getUserResponse());
        assertEquals(actualInvitation.getAttendeesCount(), testInvitation.getAttendeesCount());
        assertEquals(actualInvitation.isParticipated(), testInvitation.isParticipated());
    }

    @Test
    public void testUpdateInvitation(){
        testInvitation.setUserResponse("Yes").setAttendeesCount(2).setParticipated(true);
        invitationDAO.updateInvitation(testInvitation);
        EventInvitation actualInvitation = getTestInvitationFromDB(testInvitation.getId());

        assertEquals(actualInvitation.getEventId(), testInvitation.getEventId());
        assertEquals(actualInvitation.getUserRole(), testInvitation.getUserRole());
        assertEquals(actualInvitation.getUserResponse(), testInvitation.getUserResponse());
        assertEquals(actualInvitation.getAttendeesCount(), testInvitation.getAttendeesCount());
        assertEquals(actualInvitation.isParticipated(), testInvitation.isParticipated());
    }

    @Test
    public void testDeleteInvitation() {
        invitationDAO.deleteInvitation(testInvitation.getId());
        assertNull(getTestInvitationFromDB(testInvitation.getId()));
    }

    @Test
    public void testDeleteInvitationsByEventId(){
        invitationDAO.deleteInvitationsByEventId(testInvitation.getEventId());
        assertNull(getTestInvitationFromDB(testInvitation.getId()));
    }

    //helper methods
    private EventInvitation getTestInvitationFromDB(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        EventInvitation invitation = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();

            String sqlStr = "SELECT * FROM event_invitation WHERE id  = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                invitation = new EventInvitation();
                invitation.setId(rs.getInt("id"))
                        .setEventId(rs.getInt("event_id"))
                        .setUserRole(rs.getString("user_role"))
                        .setUserResponse(rs.getString("user_response"))
                        .setAttendeesCount(rs.getInt("attendees_count"))
                        .setParticipated(rs.getBoolean("participated"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            TestHelper.closeResources(rs, stmt, conn);
        }
        return invitation;
    }


}
