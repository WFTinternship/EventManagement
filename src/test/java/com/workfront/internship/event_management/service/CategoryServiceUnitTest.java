package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.dao.CategoryDAO;
import com.workfront.internship.event_management.dao.CategoryDAOImpl;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Category;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualCategories;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.assertFalse;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class CategoryServiceUnitTest {

    private static CategoryService categoryService;
    private CategoryDAO categoryDAO;
    private Category testCategory;

    @BeforeClass
    public static void setUpClass() {
        categoryService = new CategoryServiceImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        categoryService = null;
    }

    @Before
    public void setUp() {
        //create test category object
        testCategory = TestObjectCreator.createTestCategory();

        categoryDAO = Mockito.mock(CategoryDAOImpl.class);
        Whitebox.setInternalState(categoryService, "categoryDAO", categoryDAO);
    }

    @After
    public void tearDown() {
        testCategory = null;
        categoryDAO = null;
    }

    //Testing addCategory method
    @Test(expected = InvalidObjectException.class)
    public void addCategory_InvalidCategory() {
        testCategory.setTitle("");

        //method under test
        categoryService.addCategory(testCategory);
    }

    @Test(expected = OperationFailedException.class)
    public void addCategory_DuplicateCategory() {
        when(categoryDAO.addCategory(testCategory)).thenThrow(DuplicateEntryException.class);

        //method under test
        categoryService.addCategory(testCategory);
    }

    @Test
    public void addCategory_Success() {
        testCategory.setId(VALID_ID);
        when(categoryDAO.addCategory(testCategory)).thenReturn(VALID_ID);

        //method under test
        Category actualCategory = categoryService.addCategory(testCategory);

        assertEqualCategories(actualCategory, testCategory);
    }

    //Testing getCategoryById method
    @Test(expected = InvalidObjectException.class)
    public void getCategoryById_InvalidId() {
        //method under test
        categoryService.getCategoryById(TestObjectCreator.INVALID_ID);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getCategoryById_NotFound() {
        when(categoryDAO.getCategoryById(NON_EXISTING_ID)).thenReturn(null);

        //method under test
        categoryService.getCategoryById(NON_EXISTING_ID);
    }

    @Test
    public void getCategoryById_Success() {
        testCategory.setId(TestObjectCreator.VALID_ID);
        when(categoryDAO.getCategoryById(TestObjectCreator.VALID_ID)).thenReturn(testCategory);

        //method under test
        Category actualCategory = categoryService.getCategoryById(TestObjectCreator.VALID_ID);
        assertEqualCategories(actualCategory, testCategory);
    }

    //Testing editCategory method
    @Test(expected = InvalidObjectException.class)
    public void editCategory_InvalidCategory() {
        testCategory.setCreationDate(null);

        //method under test
        categoryService.editCategory(testCategory);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void editCategory_NotFound() {
        when(categoryDAO.updateCategory(testCategory)).thenReturn(false);

        //method under test
        categoryService.editCategory(testCategory);
    }

    @Test(expected = OperationFailedException.class)
    public void editCategory_Duplicate() {
        doThrow(DuplicateEntryException.class).when(categoryDAO).updateCategory(testCategory);

        //method under test
        categoryService.editCategory(testCategory);
    }

    @Test
    public void editCategory_Success() {
        when(categoryDAO.updateCategory(testCategory)).thenReturn(true);

        //method under test
        categoryService.editCategory(testCategory);

        verify(categoryDAO).updateCategory(testCategory);
    }

    //Testing deleteCategory method
    @Test(expected = InvalidObjectException.class)
    public void deleteCategory_InvalidId() {
        //method under test
        categoryService.deleteCategory(INVALID_ID);
    }

    @Test
    public void deleteCategory_NotFound() {
        when(categoryDAO.deleteCategory(NON_EXISTING_ID)).thenReturn(false);

        //method under test
        boolean success = categoryService.deleteCategory(NON_EXISTING_ID);

        assertFalse(success);
    }

    @Test
    public void deleteCategory_Success() {
        //method under test
        categoryService.deleteCategory(VALID_ID);

        verify(categoryDAO).deleteCategory(VALID_ID);
    }

    //Testing getAllCategories method
    @Test
    public void getAllCategories_Success() {
        //method under test
        categoryService.getAllCategories();

        verify(categoryDAO).getAllCategories();
    }

    //Testing deleteAllCategories method
    @Test
    public void deleteAllCategories_Success() {
        //method under test
        categoryService.deleteAllCategories();

        verify(categoryDAO).deleteAllCategories();
    }

}
