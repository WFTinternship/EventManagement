import com.workfront.internship.event_management.datasource.DataSourceManager;
import com.workfront.internship.event_management.datasource.RecurrenceTypeDAO;
import com.workfront.internship.event_management.datasource.RecurrenceTypeDAOImpl;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by hermine on 7/11/16.
 */
public class TestRecurrenceTypeDAOImpl {
    private static RecurrenceTypeDAO recTypeDAO = null;
    RecurrenceType testRecurrenceType = null;


    @BeforeClass
    public static void setUpClass(){
        recTypeDAO = new RecurrenceTypeDAOImpl();
    }

    @Before
    public void setUp() {
        testRecurrenceType = TestHelper.createTestRecurrenceType();
        int recTypeId = TestHelper.insertTestRecurrenceType(testRecurrenceType);
        testRecurrenceType.setId(recTypeId);
    }

    @After
    public void tearDown() {
        TestHelper.deleteTestRecurrenceType(testRecurrenceType.getId());
    }

    @Test
    public void  testInsertRecurrenceType(){
        TestHelper.deleteTestRecurrenceType(testRecurrenceType.getId());
        recTypeDAO.insertRecurrenceType(testRecurrenceType);
        RecurrenceType actualRecType = getTestRecurrenceType(testRecurrenceType.getId() + 1);
        try {
            assertEquals(testRecurrenceType.getTitle(), testRecurrenceType.getTitle());
            assertEquals(testRecurrenceType.getIntervalUnit(), testRecurrenceType.getIntervalUnit());
            assertNotNull(testRecurrenceType.getRepeatOnValues());
        } finally {
            TestHelper.deleteTestRecurrenceType(testRecurrenceType.getId() + 1);
        }
    }

    @Test
    public void testGetAllRecurrenceTypes(){
        List<RecurrenceType> expectedRecTypes = getAllRecTypes();
        List<RecurrenceType> actualRecTypes = recTypeDAO.getAllRecurrenceTypes();

        assertEquals(actualRecTypes.size(), expectedRecTypes.size());
        for (int i = 0; i < actualRecTypes.size(); i++) {
            assertEquals(testRecurrenceType.getTitle(), testRecurrenceType.getTitle());
            assertEquals(testRecurrenceType.getIntervalUnit(), testRecurrenceType.getIntervalUnit());
        }
    }

    @Test
    public void  testGetRecurrenceTypeById(){
        RecurrenceType actualRecType = recTypeDAO.getRecurrenceTypeById(testRecurrenceType.getId());
        assertEquals(testRecurrenceType.getTitle(), testRecurrenceType.getTitle());
        assertEquals(testRecurrenceType.getIntervalUnit(), testRecurrenceType.getIntervalUnit());
        assertNotNull(testRecurrenceType.getRepeatOnValues());
    }

    @Test
    public void  testDeleteRecurrenceType(){
        recTypeDAO.deleteRecurrenceType(testRecurrenceType.getId());
        assertNull(getTestRecurrenceType(testRecurrenceType.getId()));

    }

    //helper methods
    private RecurrenceType getTestRecurrenceType(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        RecurrenceType recType = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM recurrence_type LEFT JOIN repeat_on_value "
                    + "ON recurrence_type.id = repeat_on_value.recurrence_type_id "
                    + "WHERE recurrence_type.id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if(recType == null) {
                    recType = new RecurrenceType();
                    recType.setId(rs.getInt("id"))
                            .setTitle(rs.getString("title"))
                            .setIntervalUnit(rs.getString("interval_unit"));
                    if(rs.getString("repeat_on_value.title") != null) {
                        List<String> repeatOnValues = new ArrayList<String>();
                        repeatOnValues.add(rs.getString("repeat_on_value.title"));
                        recType.setRepeatOnValues(repeatOnValues);
                    }
                } else {
                    recType.getRepeatOnValues().add(rs.getString("repeat_on_value.title"));
                }
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
        return recType;
    }

    private List<RecurrenceType> getAllRecTypes() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RecurrenceType> recTypeList = new ArrayList<RecurrenceType>();
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM recurrence_type";
            stmt = conn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            while (rs.next()) {
                RecurrenceType recType = new RecurrenceType();
                recType.setId(rs.getInt("id"))
                        .setTitle(rs.getString("title"))
                        .setIntervalUnit(rs.getString("interval_unit"));
                recTypeList.add(recType);
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
        return recTypeList;
    }

}
