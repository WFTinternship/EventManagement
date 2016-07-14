import com.workfront.internship.event_management.datasource.EventCategoryDAO;
import com.workfront.internship.event_management.datasource.EventCategoryDAOImpl;
import com.workfront.internship.event_management.model.EventCategory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by Hermine Turshujyan 7/9/16.
 */
public class TestEventCategoryDAOImpl {

    private static EventCategoryDAO categoryDAO;
    private EventCategory testCategory;

    @BeforeClass
    public static void setUpClass() {
        categoryDAO = new EventCategoryDAOImpl();
    }

    @Before
    public void setUp() {
        //create test category
        testCategory = TestHelper.createTestCategory();

        //insert test user into db, get generated id
        int categoryId = categoryDAO.insertCategory(testCategory);

        //set id to test category
        testCategory.setId(categoryId);
    }

    @After
    public void tearDown() {
        //delete test categories from db
        categoryDAO.deleteAllCategories();

        // testCategory = null; //todo check
    }

    @Test
    public void insertCategory_Success() {
        //test category already inserted in setup, read record by categoryId
        EventCategory category = categoryDAO.getCategoryById(testCategory.getId());

        assertCategories(category, testCategory);
    }

    @Test(expected = RuntimeException.class)
    public void insertCategory_Dublicate_Entry() {
        //test category already inserted into db, insert dublicate category
        categoryDAO.insertCategory(testCategory);
    }

    @Test
    public void getAllCategories_Found() {
        //test category inserted in setup, insert another user
        EventCategory testCategory1 = TestHelper.createTestCategory();
        int categoryID = categoryDAO.insertCategory(testCategory1);
        testCategory1.setId(categoryID);

        //test method
        List<EventCategory> categoryList = categoryDAO.getAllCategories();

        assertEquals(categoryList.size(), 2);
        assertCategories(categoryList.get(0), testCategory);
        assertCategories(categoryList.get(1), testCategory1);
    }

    @Test
    public void getAllCategories_Empty_List() {
        //delete inserted user from db
        categoryDAO.deleteCategory(testCategory.getId());

        //test method
        List<EventCategory> categoryList = categoryDAO.getAllCategories();

        assertTrue(categoryList.isEmpty());
    }

    @Test
    public void getCategoryById_Found() {
        //test method
        EventCategory category = categoryDAO.getCategoryById(testCategory.getId());

        assertCategories(category, testCategory);
    }

    @Test
    public void getCategoryById_Not_Found() {
        //test method
        EventCategory category = categoryDAO.getCategoryById(TestHelper.NON_EXISTING_ID);

        assertNull(category);
    }

    @Test
    public void updateCategory() {
        //create new category with the same id
        EventCategory updatedCategory = TestHelper.createTestCategory();
        updatedCategory.setId(testCategory.getId());

        //test method
        categoryDAO.updateCategory(updatedCategory);

        //read updated record from db
        EventCategory category = categoryDAO.getCategoryById(updatedCategory.getId());

        assertCategories(category, updatedCategory);
    }

    @Test
    public void deleteCategory() {
        //testing method
        categoryDAO.deleteCategory(testCategory.getId());

        EventCategory category = categoryDAO.getCategoryById(testCategory.getId());
        assertNull(category);
    }

    @Test
    public void deleteAllCategories() {
        //testing method
        categoryDAO.deleteAllCategories();

        List<EventCategory> categoryList = categoryDAO.getAllCategories();
        assertTrue(categoryList.isEmpty());
    }


    //helper methods
    private void assertCategories(EventCategory expectedCategory, EventCategory actualCategory) {
        assertEquals(expectedCategory.getId(), actualCategory.getId());
        assertEquals(expectedCategory.getTitle(), actualCategory.getTitle());
        assertEquals(expectedCategory.getDescription(), actualCategory.getDescription());
        assertNotNull(expectedCategory.getCreationDate());
    }
}
