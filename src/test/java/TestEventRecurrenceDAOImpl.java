import com.workfront.internship.event_management.datasource.DataSourceManager;
import com.workfront.internship.event_management.datasource.EventRecurrenceDAO;
import com.workfront.internship.event_management.datasource.EventRecurrenceDAOImpl;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventCategory;
import com.workfront.internship.event_management.model.EventRecurrence;
import com.workfront.internship.event_management.model.datehelpers.RecurrenceType;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hermine on 7/13/16.
 */
public class TestEventRecurrenceDAOImpl {
    private static EventRecurrenceDAO eventRecurrenceDAO;
    private EventCategory testCategory;
    private Event testEvent;
    private RecurrenceType testRecurrenceType;
    private EventRecurrence testEventRecurrence;

    @BeforeClass
    public static void setUpClass() {
        eventRecurrenceDAO = new EventRecurrenceDAOImpl();
    }

    @Before
    public void setUp() {
        //creating test objects
        testCategory = TestHelper.createTestCategory();
        testEvent = TestHelper.createTestEvent();
        testRecurrenceType = TestHelper.createTestRecurrenceType();
        testEventRecurrence = TestHelper.createTestEventRecurrence();

        //inserting test objects to db
        int categoryId = TestHelper.insertTestCategoryToDB(testCategory);
        testCategory.setId(categoryId);

        testEvent.setCategory(testCategory);
        int eventId = TestHelper.insertTestEventToDB(testEvent);
        testEvent.setId(eventId);

        testRecurrenceType = TestHelper.createTestRecurrenceType();
        int recTypeId = TestHelper.insertTestRecurrenceTypeToDB(testRecurrenceType);
        testRecurrenceType.setId(recTypeId);

        testEventRecurrence.setEventId(testEvent.getId()).setRecurrenceType(testRecurrenceType);
        int evRecId = TestHelper.insertTestEventRecurrenceToDB(testEventRecurrence);
        testEventRecurrence.setId(evRecId);

    }

    @After
    public void tearDown() {
        TestHelper.deleteTestEventRecurrenceFromDB(testEventRecurrence.getId());
        TestHelper.deleteTestRecurrenceTypeFromDB(testRecurrenceType.getId());
        TestHelper.deleteTestEventFromDB(testEvent.getId());
        TestHelper.deleteTestCategoryFromDB(testCategory.getId());

        testRecurrenceType = null;
        testEventRecurrence = null;
        testEvent = null;
        testCategory = null;
    }

    @Test
    public void testInsertEventRecurrence(){
        TestHelper.deleteTestEventRecurrenceFromDB(testEventRecurrence.getId());
        int newEvRecId = eventRecurrenceDAO.insertEventRecurrence(testEventRecurrence);
        EventRecurrence actualRecurrence = getTestEventRecurrenceFromDB(newEvRecId);

        try {
            assertEquals(actualRecurrence.getEventId(), testEventRecurrence.getEventId());
            assertEquals(actualRecurrence.getRepeatOn(), testEventRecurrence.getRepeatOn());
            assertEquals(actualRecurrence.getRepeatInterval(), testEventRecurrence.getRepeatInterval());
            assertNotNull(actualRecurrence.getRepeatEndDate());
        } finally {
            TestHelper.deleteTestEventRecurrenceFromDB(newEvRecId);
        }
    }

    @Test //---
    public void testInsertEventRecurrences(){
//        TestHelper.deleteTestEventRecurrenceFromDB(testEventRecurrence.getId());
//        int newEvRecId = eventRecurrenceDAO.insertEventRecurrence(testEventRecurrence);
//        EventRecurrence actualRecurrence = getTestEventRecurrenceFromDB(newEvRecId);
//
//        try {
//            assertEquals(actualRecurrence.getEventId(), testEventRecurrence.getEventId());
//            assertEquals(actualRecurrence.getRepeatOn(), testEventRecurrence.getRepeatOn());
//            assertEquals(actualRecurrence.getRepeatInterval(), testEventRecurrence.getRepeatInterval());
//            assertNotNull(actualRecurrence.getRepeatEndDate());
//        } finally {
//            TestHelper.deleteTestEventRecurrenceFromDB(newEvRecId);
//        }
    }

    @Test //--
    public void testGetEventRecurrencesByEventId(){}

    @Test
    public void testUpdateEventRecurrence(){
        testEventRecurrence.setRepeatInterval(7).setRepeatOn("New test repeat on string");
        eventRecurrenceDAO.updateEventRecurrence(testEventRecurrence);
        EventRecurrence actualRecurrence = getTestEventRecurrenceFromDB(testEventRecurrence.getId());

        assertEquals(actualRecurrence.getEventId(), testEventRecurrence.getEventId());
        assertEquals(actualRecurrence.getRepeatOn(), testEventRecurrence.getRepeatOn());
        assertEquals(actualRecurrence.getRepeatInterval(), testEventRecurrence.getRepeatInterval());
    }

    @Test //---
    public void testDeleteEventRecurrece(){
        eventRecurrenceDAO.deleteEventRecurrece(testEventRecurrence.getId());
        getTestEventRecurrenceFromDB(testEventRecurrence.getId());
    }

    //helper methods
    private EventRecurrence getTestEventRecurrenceFromDB( int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        EventRecurrence recurrence =null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM  event_recurrence where id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                 recurrence = new EventRecurrence();
                recurrence.setId(rs.getInt("id"))
                        .setEventId(rs.getInt("event_id"))
                        .setRepeatInterval(rs.getInt("repeat_interval"))
                        .setRepeatOn(rs.getString("repeat_on"))
                        .setRepeatEndDate(rs.getTimestamp("repeat_end"));
            }
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            TestHelper.closeResources(rs, stmt, conn);
        }
        return recurrence;
    }
}
