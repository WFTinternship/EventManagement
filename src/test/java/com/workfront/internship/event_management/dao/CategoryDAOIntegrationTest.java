package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualCategories;
import static com.workfront.internship.event_management.TestObjectCreator.NON_EXISTING_ID;
import static com.workfront.internship.event_management.TestObjectCreator.createTestCategory;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 7/9/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class CategoryDAOIntegrationTest {

    @Autowired
    private CategoryDAO categoryDAO;

    private Category testCategory;

    @Before
    public void setUp() {
        //create test category
        testCategory = createTestCategory();

        //insert test user into db, get generated id
        int categoryId = categoryDAO.addCategory(testCategory);
        testCategory.setId(categoryId);
    }

    @After
    public void tearDown() {
        //delete test categories from db
        categoryDAO.deleteAllCategories();

        //delete test category object
        testCategory = null;
    }

    @Test
    public void addCategory_Success() {
        //test category already inserted in setup, read record by categoryId
        Category category = categoryDAO.getCategoryById(testCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, testCategory);
    }

    @Test(expected = DuplicateEntryException.class)
    public void addCategory_Duplicate_Entry() {
        //test category already inserted into db, insert dublicate category
        categoryDAO.addCategory(testCategory);  //event_category.title field in db is unique
    }

    @Test
    public void getAllCategories_Found() {
        //test method
        List<Category> categoryList = categoryDAO.getAllCategories();

        assertNotNull(categoryList);
        assertFalse(categoryList.isEmpty());
        assertEquals(categoryList.size(), 1);

        assertEqualCategories(categoryList.get(0), testCategory);
    }

    @Test
    public void getAllCategories_Empty_List() {
        //delete inserted category from db
        categoryDAO.deleteCategory(testCategory.getId());

        //test method
        List<Category> categoryList = categoryDAO.getAllCategories();

        assertNotNull(categoryList);
        assertTrue(categoryList.isEmpty());
    }

    @Test
    public void getCategoryById_Found() {
        //test method
        Category category = categoryDAO.getCategoryById(testCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, testCategory);
    }

    @Test
    public void getCategoryById_Not_Found() {
        //test method
        Category category = categoryDAO.getCategoryById(NON_EXISTING_ID);
        assertNull(category);
    }

    @Test
    public void updateCategory_Found() {
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

    @Test
    public void updateCategory_Not_Found() throws DAOException, DuplicateEntryException, ObjectNotFoundException {
        //create new category with non-existing id
        Category updatedCategory = TestObjectCreator.createTestCategory();

        //test method
        boolean success = categoryDAO.updateCategory(updatedCategory);

        assertFalse(success);
    }

    @Test
    public void deleteCategory_Found() {
        //testing method
        boolean success = categoryDAO.deleteCategory(testCategory.getId());
        Category category = categoryDAO.getCategoryById(testCategory.getId());

        assertTrue(success);
        assertNull(category);
    }

    @Test
    public void deleteCategory_Not_Found() {
        //testing method
        boolean success = categoryDAO.deleteCategory(TestObjectCreator.NON_EXISTING_ID);

        assertFalse(success);
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
