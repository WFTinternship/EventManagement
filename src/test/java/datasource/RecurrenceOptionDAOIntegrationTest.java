package datasource;

import com.workfront.internship.event_management.DAO.RecurrenceOptionDAO;
import com.workfront.internship.event_management.DAO.RecurrenceOptionDAOImpl;
import com.workfront.internship.event_management.DAO.RecurrenceTypeDAO;
import com.workfront.internship.event_management.DAO.RecurrenceTypeDAOImpl;
import com.workfront.internship.event_management.model.RecurrenceOption;
import com.workfront.internship.event_management.model.RecurrenceType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Hermine Turshujyan 7/16/16.
 */
public class RecurrenceOptionDAOIntegrationTest {

    private static RecurrenceOptionDAO recurrenceOptionDAO;
    private static RecurrenceTypeDAO recurrenceTypeDAO;
    private RecurrenceOption testRecurrenceOption;
    private RecurrenceType testRecurrenceType;

    @BeforeClass
    public static void setUpClass() {
        recurrenceOptionDAO = new RecurrenceOptionDAOImpl();
        recurrenceTypeDAO = new RecurrenceTypeDAOImpl();
    }

    @Before
    public void setUp() {

        //create test recurrence type and option objects
        testRecurrenceType = TestHelper.createTestRecurrenceType();
        testRecurrenceOption = TestHelper.createTestRecurrenceOption();

        //insert into db
        int recurrenceTypeId = recurrenceTypeDAO.addRecurrenceType(testRecurrenceType);
        testRecurrenceType.setId(recurrenceTypeId);

        testRecurrenceOption.setRecurrenceTypeId(recurrenceTypeId);
        int optionId = recurrenceOptionDAO.addRecurrenceOption(testRecurrenceOption);
        testRecurrenceOption.setId(optionId);
    }

    @After
    public void tearDown() {
        //delete test records from db
        recurrenceTypeDAO.deleteAllRecurrenceTypes();
        recurrenceOptionDAO.deleteAllRecurrenceOptions();

        //delete test object
        testRecurrenceOption = null;
        testRecurrenceType = null;
    }

    @Test
    public void addRecurrenceOption_Success() {
        //test record already inserted in setup, read record from db
        RecurrenceOption recurrenceOption = recurrenceOptionDAO.getRecurrenceOption(testRecurrenceOption.getId());

        assertNotNull(recurrenceOption);
        assertEqualRepeatOptions(recurrenceOption, testRecurrenceOption);
    }

    @Test(expected = RuntimeException.class)
    public void addRecurrenceOption_Dublicate_Entry() {

        //test record already inserted into db, insert dublicate entry
        recurrenceOptionDAO.addRecurrenceOption(testRecurrenceOption);
    }

    @Test
    public void getAllRecurrenceOptions_Found() {
        //test method
        List<RecurrenceOption> recurrenceOption = recurrenceOptionDAO.getAllRecurrenceOptions();

        assertNotNull(recurrenceOption);
        assertFalse(recurrenceOption.isEmpty());
        assertEquals(recurrenceOption.size(), 1);
        assertEqualRepeatOptions(recurrenceOption.get(0), testRecurrenceOption);
    }

    @Test
    public void getAllRecurrenceOptions_Not_Found() {
        //delete inserted tet record
        recurrenceOptionDAO.deleteRecurrenceOption(testRecurrenceOption.getId());

        //test method
        List<RecurrenceOption> recurrenceOption = recurrenceOptionDAO.getAllRecurrenceOptions();

        assertNotNull(recurrenceOption);
        assertTrue(recurrenceOption.isEmpty());
    }

    @Test
    public void getRecurrenceOptionById_Found() {
        //test method
        RecurrenceOption recurrenceOption = recurrenceOptionDAO.getRecurrenceOption(testRecurrenceOption.getId());

        assertNotNull(recurrenceOption);
        assertEqualRepeatOptions(recurrenceOption, testRecurrenceOption);
    }

    @Test
    public void getRecurrenceOptionById_Not_Found() {
        //test method
        RecurrenceOption recurrenceOption = recurrenceOptionDAO.getRecurrenceOption(TestHelper.NON_EXISTING_ID);

        assertNull(recurrenceOption);
    }

    @Test
    public void getRecurrenceOptionsByRecurrenceType_Found() {
        //test method
        List<RecurrenceOption> recurrenceOption = recurrenceOptionDAO.getRecurrenceOptionsByRecurrenceType(testRecurrenceType.getId());

        assertNotNull(recurrenceOption);
        assertFalse(recurrenceOption.isEmpty());
        assertEquals(recurrenceOption.size(), 1);
        assertEqualRepeatOptions(recurrenceOption.get(0), testRecurrenceOption);
    }

    @Test
    public void getRecurrenceOptionsByRecurrenceType_Not_Found() {
        //test method
        List<RecurrenceOption> recurrenceOption = recurrenceOptionDAO.getRecurrenceOptionsByRecurrenceType(TestHelper.NON_EXISTING_ID);

        assertNotNull(recurrenceOption);
        assertTrue(recurrenceOption.isEmpty());
    }

    @Test
    public void updateRecurrenceOption_Success() {
        //update test object
        testRecurrenceOption.setTitle("Changed test title");

        //test method
        recurrenceOptionDAO.updateRecurrenceOption(testRecurrenceOption);

        //read updated record from db
        RecurrenceOption updatedRecurrenceOption = recurrenceOptionDAO.getRecurrenceOption(testRecurrenceOption.getId());

        assertNotNull(updatedRecurrenceOption);
        assertEqualRepeatOptions(updatedRecurrenceOption, testRecurrenceOption);
    }

    @Test
    public void updateRepeatOption_Not_Found() {
        //create new recurrence option with non-existing id
        RecurrenceOption recurrenceOption = TestHelper.createTestRecurrenceOption();

        //test method
        boolean updated = recurrenceOptionDAO.updateRecurrenceOption(recurrenceOption);

        assertFalse(updated);
    }

    @Test
    public void deleteRecurrenceOption_Success() {
        //testing method
        boolean deleted = recurrenceOptionDAO.deleteRecurrenceOption(testRecurrenceOption.getId());

        RecurrenceOption recurrenceOption = recurrenceOptionDAO.getRecurrenceOption(testRecurrenceOption.getId());

        assertTrue(deleted);
        assertNull(recurrenceOption);
    }

    @Test
    public void deleteRecurrencetOption_NotFound() {
        //testing method
        boolean deleted = recurrenceOptionDAO.deleteRecurrenceOption(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteRepeatOptionsByRecurrenceType_Success() {
        //testing method
        boolean deleted = recurrenceOptionDAO.deleteRecurrenceOptionsByRecurrenceType(testRecurrenceOption.getRecurrenceTypeId());

        List<RecurrenceOption> recurrenceOptionList = recurrenceOptionDAO.getRecurrenceOptionsByRecurrenceType(testRecurrenceOption.getRecurrenceTypeId());

        assertNotNull(recurrenceOptionList);
        assertTrue(recurrenceOptionList.isEmpty());
        assertTrue(deleted);
    }

    @Test
    public void deleteRepeatOptionsByRecurrenceType_Not_Found() {
        //testing method
        boolean deleted = recurrenceOptionDAO.deleteRecurrenceOptionsByRecurrenceType(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteAllRecurrenceOptions_Success() {
        //testing method
        boolean deleted = recurrenceOptionDAO.deleteAllRecurrenceOptions();

        List<RecurrenceOption> recurrenceOptionList = recurrenceOptionDAO.getAllRecurrenceOptions();

        assertNotNull(recurrenceOptionList);
        assertTrue(recurrenceOptionList.isEmpty());
        assertTrue(deleted);
    }

    @Test
    public void deleteAllRecurrenceOptions_Not_Found() {
        //delete inserted record
        recurrenceOptionDAO.deleteRecurrenceOption(testRecurrenceOption.getId());

        //testing method
        boolean deleted = recurrenceOptionDAO.deleteAllRecurrenceOptions();

        assertFalse(deleted);
    }

    //helper methods
    public void assertEqualRepeatOptions(RecurrenceOption actualRecurrenceOption, RecurrenceOption expectedRecurrenceOption) {
        assertEquals(actualRecurrenceOption.getId(), expectedRecurrenceOption.getId());
        assertEquals(actualRecurrenceOption.getRecurrenceTypeId(), expectedRecurrenceOption.getRecurrenceTypeId());
        assertEquals(actualRecurrenceOption.getAbbreviation(), expectedRecurrenceOption.getAbbreviation());
        assertEquals(actualRecurrenceOption.getTitle(), expectedRecurrenceOption.getTitle());

    }
}
