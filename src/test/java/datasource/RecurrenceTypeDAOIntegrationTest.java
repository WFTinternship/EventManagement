package datasource;

import com.workfront.internship.event_management.datasource.RecurrenceTypeDAO;
import com.workfront.internship.event_management.datasource.RecurrenceTypeDAOImpl;
import com.workfront.internship.event_management.model.RecurrenceType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
public class RecurrenceTypeDAOIntegrationTest {

    private static RecurrenceTypeDAO recurrenceTypeDAO = null;
    RecurrenceType testRecurrenceType = null;


    @BeforeClass
    public static void setUpClass(){
        recurrenceTypeDAO = new RecurrenceTypeDAOImpl();
    }

    @Before
    public void setUp() {
        //create test recurrence type
        testRecurrenceType = TestHelper.createTestRecurrenceType();

        //insert test record into db and get generated id
        int recurrenceTypeId = recurrenceTypeDAO.addRecurrenceType(testRecurrenceType);
        testRecurrenceType.setId(recurrenceTypeId);
    }

    @After
    public void tearDown() {
        recurrenceTypeDAO.deleteAllRecurrenceTypes();
    }

    @Test
    public void addCategory_Success() {
      /*  //test category already inserted in setup, read record by categoryId
        Category category = categoryDAO.getCategoryById(testCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, testCategory);*/
    }

    @Test(expected = RuntimeException.class)
    public void addCategory_Dublicate_Entry() {
       /* //test category already inserted into db, insert dublicate category
        categoryDAO.addCategory(testCategory);  //event_category.title field in db is unique*/
    }

    @Test
    public void getAllRecurrenceTypes_Found() {
        //test method
        List<RecurrenceType> recurrenceTypeList = recurrenceTypeDAO.getAllRecurrenceTypes();

        assertNotNull(recurrenceTypeList);
        assertFalse(recurrenceTypeList.isEmpty());
        assertEquals(recurrenceTypeList.size(), 1);

        assertEqualRecurrenceTypes(recurrenceTypeList.get(0), testRecurrenceType);
    }

    @Test
    public void getAllRecurrenceTypes_Empty_List() {
        //delete inserted record from db
        recurrenceTypeDAO.deleteRecurrenceType(testRecurrenceType.getId());

        //test method
        List<RecurrenceType> recurrenceTypeList = recurrenceTypeDAO.getAllRecurrenceTypes();

        assertNotNull(recurrenceTypeList);
        assertTrue(recurrenceTypeList.isEmpty());
    }

    @Test
    public void getRecurrenceTypeById_Found() {
        //test method (test record already inserted in seUp())
        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(testRecurrenceType.getId());

        assertNotNull(recurrenceType);
        assertEqualRecurrenceTypes(recurrenceType, testRecurrenceType);
    }

    @Test
    public void getRecurrenceTypeById_Not_Found() {
        //test method
        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(TestHelper.NON_EXISTING_ID);

        assertNull(recurrenceType);
    }

    @Test
    public void updateRecurrenceType_Found() {
     /*   //create new category with the same id
        Category updatedCategory = TestHelper.createTestCategory();
        updatedCategory.setId(testCategory.getId());

        //test method
        categoryDAO.updateCategory(updatedCategory);

        //read updated record from db
        Category category = categoryDAO.getCategoryById(updatedCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, updatedCategory);*/
    }

    @Test
    public void updateRecurrenceType_Not_Found() {
        //create new category with non-existing id
     /*   Category updatedCategory = TestHelper.createTestCategory();

        //test method
        boolean updated = categoryDAO.updateCategory(updatedCategory);

        assertFalse(updated);*/
    }

    @Test
    public void deleteRecurrenceType_Found() {
        //testing method
        boolean deleted = recurrenceTypeDAO.deleteRecurrenceType(testRecurrenceType.getId());

        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(testRecurrenceType.getId());

        assertTrue(deleted);
        assertNull(recurrenceType);
    }

    @Test
    public void deleteRecurrenceType_Not_Found() {
        //testing method
        boolean deleted = recurrenceTypeDAO.deleteRecurrenceType(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteAllRecurrenceTypes_Found() {
        //testing method
        boolean deleted = recurrenceTypeDAO.deleteAllRecurrenceTypes();

        List<RecurrenceType> recurrenceTypeList = recurrenceTypeDAO.getAllRecurrenceTypes();

        assertNotNull(recurrenceTypeList);
        assertTrue(recurrenceTypeList.isEmpty());
        assertTrue(deleted);
    }

    @Test
    public void deleteAllRecurrenceTypes_Not_Found() {
        //delete inserted test record
        recurrenceTypeDAO.deleteRecurrenceType(testRecurrenceType.getId());

        //testing method
        boolean deleted = recurrenceTypeDAO.deleteAllRecurrenceTypes();

        assertFalse(deleted);
    }


    //helper methods
    private void assertEqualRecurrenceTypes(RecurrenceType actualRecurrenceType, RecurrenceType expectedRecurrenceType) {
        assertEquals(actualRecurrenceType.getTitle(), expectedRecurrenceType.getTitle());
        assertEquals(actualRecurrenceType.getIntervalUnit(), expectedRecurrenceType.getIntervalUnit());
        assertEquals(actualRecurrenceType.getRecurrenceOptions(), expectedRecurrenceType.getRecurrenceOptions());
    }


}
