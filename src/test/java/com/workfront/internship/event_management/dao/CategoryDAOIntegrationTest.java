package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Category;
import org.junit.*;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualCategories;
import static com.workfront.internship.event_management.TestObjectCreator.NON_EXISTING_ID;
import static com.workfront.internship.event_management.TestObjectCreator.createTestCategory;
import static junit.framework.TestCase.*;

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
    public void setUp() throws DAOException, DuplicateEntryException {
        //create test category
        testCategory = createTestCategory();

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
    public void addCategory_Success() throws DAOException, ObjectNotFoundException {
        //test category already inserted in setup, read record by categoryId
        Category category = categoryDAO.getCategoryById(testCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, testCategory);
    }


    @Test(expected = DuplicateEntryException.class)
    public void addCategory_Duplicate_Entry() throws DAOException, DuplicateEntryException {
        //test category already inserted into db, insert dublicate category
        categoryDAO.addCategory(testCategory);  //event_category.title field in db is unique
    }

    @Test
    public void getAllCategories_Found() throws DAOException {
        //test method
        List<Category> categoryList = categoryDAO.getAllCategories();

        assertNotNull(categoryList);
        assertFalse(categoryList.isEmpty());
        assertEquals(categoryList.size(), 1);

        assertEqualCategories(categoryList.get(0), testCategory);
    }

    @Test
    public void getAllCategories_Empty_List() throws DAOException, ObjectNotFoundException {
        //delete inserted category from db
        categoryDAO.deleteCategory(testCategory.getId());

        //test method
        List<Category> categoryList = categoryDAO.getAllCategories();

        assertNotNull(categoryList);
        assertTrue(categoryList.isEmpty());
    }

    @Test
    public void getCategoryById_Found() throws DAOException, ObjectNotFoundException {
        //test method
        Category category = categoryDAO.getCategoryById(testCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, testCategory);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getCategoryById_Not_Found() throws DAOException, ObjectNotFoundException {
        //test method
        categoryDAO.getCategoryById(NON_EXISTING_ID);
    }

    @Test
    public void updateCategory_Found() throws DAOException, DuplicateEntryException, ObjectNotFoundException {
        //create new category with the same id
        Category updatedCategory = TestObjectCreator.createTestCategory();
        updatedCategory.setId(testCategory.getId());

        //test method
        categoryDAO.updateCategory(updatedCategory);

        //read updated record from db
        Category category = categoryDAO.getCategoryById(updatedCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, updatedCategory);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateCategory_Not_Found() throws DAOException, DuplicateEntryException, ObjectNotFoundException {
        //create new category with non-existing id
        Category updatedCategory = TestObjectCreator.createTestCategory();

        //test method
        categoryDAO.updateCategory(updatedCategory);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteCategory_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        categoryDAO.deleteCategory(testCategory.getId());

        categoryDAO.getCategoryById(testCategory.getId());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteCategory_Not_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        categoryDAO.deleteCategory(TestObjectCreator.NON_EXISTING_ID);
    }

    @Test
    public void deleteAllCategories_Found() throws DAOException {
        //testing method
        categoryDAO.deleteAllCategories();

        List<Category> categoryList = categoryDAO.getAllCategories();

        assertNotNull(categoryList);
        assertTrue(categoryList.isEmpty());
    }

}
