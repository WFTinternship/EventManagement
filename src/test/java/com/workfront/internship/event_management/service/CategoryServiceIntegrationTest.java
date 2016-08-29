package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualCategories;
import static com.workfront.internship.event_management.TestObjectCreator.createTestCategory;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 8/29/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class CategoryServiceIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    private Category testCategory;

    @Before
    public void setUp() {
        //create test category
        testCategory = createTestCategory();
        categoryService.addCategory(testCategory);
    }

    @After
    public void tearDown() {
        //delete test category object
        testCategory = null;

        //remove from db
        categoryService.deleteAllCategories();
    }

    @Test
    public void addCategory() {
        //test category already inserted in setup, read record by categoryId
        Category category = categoryService.getCategoryById(testCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, testCategory);
    }


    @Test
    public void getAllCategories() {
        //test method
        List<Category> categoryList = categoryService.getAllCategories();

        assertNotNull(categoryList);
        assertFalse(categoryList.isEmpty());
        assertEquals(categoryList.size(), 1);

        assertEqualCategories(categoryList.get(0), testCategory);
    }

    @Test
    public void getCategoryById() {
        //test method
        Category category = categoryService.getCategoryById(testCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, testCategory);
    }

    @Test
    public void updateCategory() {
        //create new category with the same id
        Category updatedCategory = TestObjectCreator.createTestCategory();
        updatedCategory.setId(testCategory.getId());

        //test method
        categoryService.editCategory(updatedCategory);

        //read updated record from db
        Category category = categoryService.getCategoryById(updatedCategory.getId());

        assertNotNull(category);
        assertEqualCategories(category, updatedCategory);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateCategory_NotFound() {
        //create new category with non-existing id
        Category updatedCategory = createTestCategory();

        //test method
        categoryService.editCategory(updatedCategory);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteCategory() {
        //testing method
        boolean success = categoryService.deleteCategory(testCategory.getId());

        assertTrue(success);

        categoryService.getCategoryById(testCategory.getId());
    }

    @Test
    public void deleteAllCategories() throws DAOException {
        //testing method
        categoryService.deleteAllCategories();

        List<Category> categoryList = categoryService.getAllCategories();

        assertNotNull(categoryList);
        assertTrue(categoryList.isEmpty());
    }

}
