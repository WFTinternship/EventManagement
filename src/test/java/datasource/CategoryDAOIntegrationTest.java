package datasource;

import com.workfront.internship.event_management.dao.*;
import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.exception.DuplicateEntryException;
import com.workfront.internship.event_management.model.Category;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Hermine Turshujyan 7/9/16.
 */
public class CategoryDAOIntegrationTest {

    private static CategoryDAO categoryDAO;
    private Category testCategory;

    @BeforeClass
    public static void setUpClass() throws DAOException {
        categoryDAO = new CategoryDAOImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        categoryDAO = null;
    }

    @Before
    public void setUp() throws DAOException {
        //create test category
        testCategory = TestHelper.createTestCategory();

        //insert test user into db, get generated id
        int categoryId = categoryDAO.addCategory(testCategory);
        testCategory.setId(categoryId);
    }

    @After
    public void tearDown() throws DAOException {
        //delete test categories from db
        categoryDAO.deleteAllCategories();

        //delete test category object
        testCategory = null;
    }

    @Test
    public void addCategory_Success() throws DAOException {
        //test category already inserted in setup, read record by categoryId
        Category category = categoryDAO.getCategoryById(testCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, testCategory);
    }

    @Test(expected = DuplicateEntryException.class)
    public void addCategory_Dublicate_Entry() throws DAOException {
        //test category already inserted into db, insert dublicate category
        categoryDAO.addCategory(testCategory);  //event_category.title field in db is unique
    }

    @Test
    public void getAllCategories_Found() throws DAOException {
        //create test categoryList, insert into db
        List<Category> testCategoryList = createTestCategoryList();

        //test method
        List<Category> categoryList = categoryDAO.getAllCategories();

        assertNotNull(categoryList);
        assertFalse(categoryList.isEmpty());
        assertCategoryLists(categoryList, testCategoryList);
    }

    @Test
    public void getAllCategories_Empty_List() throws DAOException {
        //delete inserted category from db
        categoryDAO.deleteCategory(testCategory.getId());

        //test method
        List<Category> categoryList = categoryDAO.getAllCategories();

        assertNotNull(categoryList);
        assertTrue(categoryList.isEmpty());
    }

    @Test
    public void getCategoryById_Found() throws DAOException {
        //test method
        Category category = categoryDAO.getCategoryById(testCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, testCategory);
    }

    @Test
    public void getCategoryById_Not_Found() throws DAOException {
        //test method
        Category category = categoryDAO.getCategoryById(TestHelper.NON_EXISTING_ID);

        assertNull(category);
    }

    @Test
    public void getCategoryByTitle_Found() throws DAOException {
        //test method
        Category category = categoryDAO.getCategoryByTitle(testCategory.getTitle());

        assertNotNull(category);
        assertEqualCategories(category, testCategory);
    }

    @Test
    public void getCategoryByTitle_Not_Found() throws DAOException {
        //test method
        Category category = categoryDAO.getCategoryByTitle(TestHelper.NON_EXISTING_TITLE);

        assertNull(category);
    }


    @Test
    public void updateCategory_Found() throws DAOException {
        //create new category with the same id
        Category updatedCategory = TestHelper.createTestCategory();
        updatedCategory.setId(testCategory.getId());

        //test method
        categoryDAO.updateCategory(updatedCategory);

        //read updated record from db
        Category category = categoryDAO.getCategoryById(updatedCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, updatedCategory);
    }

    @Test
    public void updateCategory_Not_Found() throws DAOException {
        //create new category with non-existing id
        Category updatedCategory = TestHelper.createTestCategory();

        //test method
        boolean updated = categoryDAO.updateCategory(updatedCategory);

        assertFalse(updated);
    }

    @Test
    public void deleteCategory_Found() throws DAOException {
        //testing method
        boolean deleted = categoryDAO.deleteCategory(testCategory.getId());

        Category category = categoryDAO.getCategoryById(testCategory.getId());

        assertTrue(deleted);
        assertNull(category);
    }

    @Test
    public void deleteCategory_Not_Found() throws DAOException {
        //testing method
        boolean deleted = categoryDAO.deleteCategory(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteAllCategories_Found() throws DAOException {
        //testing method
        boolean deleted = categoryDAO.deleteAllCategories();

        List<Category> categoryList = categoryDAO.getAllCategories();

        assertNotNull(categoryList);
        assertTrue(categoryList.isEmpty());
        assertTrue(deleted);
    }

    @Test
    public void deleteAllCategories_Not_Found() throws DAOException {
        //delete inserted category
        categoryDAO.deleteCategory(testCategory.getId());

        //testing method
        boolean deleted = categoryDAO.deleteAllCategories();

        assertFalse(deleted);
    }

    //helper methods
    private void assertEqualCategories(Category expectedCategory, Category actualCategory) {
        assertEquals(expectedCategory.getId(), actualCategory.getId());
        assertEquals(expectedCategory.getTitle(), actualCategory.getTitle());
        assertEquals(expectedCategory.getDescription(), actualCategory.getDescription());
        assertNotNull(expectedCategory.getCreationDate());
    }

    private void assertCategoryLists(List<Category> expectedList, List<Category> actualList) {
        assertEquals(actualList.size(), expectedList.size());
        for(int i = 0; i < actualList.size(); i++) {
            assertEqualCategories(actualList.get(i), expectedList.get(i));
        }
    }

    private List<Category> createTestCategoryList() throws DAOException {
        //create second test category
        Category secondTestCategory = TestHelper.createTestCategory();

        //insert second category into db
        int categoryId = categoryDAO.addCategory(secondTestCategory);
        secondTestCategory.setId(categoryId);

        //create test media list
        List<Category> testCategoryList = new ArrayList<>();
        testCategoryList.add(testCategory);
        testCategoryList.add(secondTestCategory);

        return testCategoryList;
    }
}
