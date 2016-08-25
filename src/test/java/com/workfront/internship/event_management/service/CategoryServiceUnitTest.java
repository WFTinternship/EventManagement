package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.dao.CategoryDAO;
import com.workfront.internship.event_management.dao.CategoryDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Category;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualCategories;
import static com.workfront.internship.event_management.TestObjectCreator.INVALID_ID;
import static com.workfront.internship.event_management.TestObjectCreator.NON_EXISTING_ID;
import static com.workfront.internship.event_management.TestObjectCreator.VALID_ID;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class CategoryServiceUnitTest {

    private static CategoryService categoryService;
    private CategoryDAO categoryDAO;
    private Category testCategory;

    @BeforeClass
    public static void setUpClass() throws OperationFailedException {
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
    @Test(expected = OperationFailedException.class)
    public void addCategory_InvalidCategory() {
        testCategory.setTitle("");

        //method under test
        categoryService.addCategory(testCategory);
    }

    @Test(expected = OperationFailedException.class)
    public void addCategory_DuplicateCategory() throws DuplicateEntryException, DAOException {
        when(categoryDAO.addCategory(testCategory)).thenThrow(DuplicateEntryException.class);

        //method under test
        categoryService.addCategory(testCategory);
    }

    @Test(expected = OperationFailedException.class)
    public void addCategory_DBError() throws DuplicateEntryException, DAOException {
        when(categoryDAO.addCategory(testCategory)).thenThrow(DAOException.class);

        //method under test
        categoryService.addCategory(testCategory);
    }

    @Test
    public void addCategory_Success() throws DuplicateEntryException, DAOException {
        testCategory.setId(VALID_ID);
        when(categoryDAO.addCategory(testCategory)).thenReturn(VALID_ID);

        //method under test
        Category actualCategory = categoryService.addCategory(testCategory);

        assertEqualCategories(actualCategory, testCategory);
    }

    //Testing getCategoryById method
    @Test(expected = OperationFailedException.class)
    public void getCategoryById_Invalid_Id() {
        //method under test
        categoryService.getCategoryById(TestObjectCreator.INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getCategoryById_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(categoryDAO).getCategoryById(TestObjectCreator.VALID_ID);

        //method under test
        categoryService.getCategoryById(TestObjectCreator.VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getCategoryById_Not_Found() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(categoryDAO).getCategoryById(TestObjectCreator.NON_EXISTING_ID);

        //method under test
        categoryService.getCategoryById(TestObjectCreator.NON_EXISTING_ID);
    }

    @Test
    public void getCategoryById_Success() throws ObjectNotFoundException, DAOException {
        testCategory.setId(TestObjectCreator.VALID_ID);
        when(categoryDAO.getCategoryById(TestObjectCreator.VALID_ID)).thenReturn(testCategory);

        //method under test
        Category actualCategory = categoryService.getCategoryById(TestObjectCreator.VALID_ID);
        assertEqualCategories(actualCategory, testCategory);
    }

    //Testing editCategory method
    @Test(expected = OperationFailedException.class)
    public void editCategory_Invalid_Category() {
        testCategory.setCreationDate(null);

        //method under test
        categoryService.editCategory(testCategory);
    }

    @Test(expected = OperationFailedException.class)
    public void editCategory_Not_Found() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(ObjectNotFoundException.class).when(categoryDAO).updateCategory(testCategory);

        //method under test
        categoryService.editCategory(testCategory);
    }

    @Test(expected = OperationFailedException.class)
    public void editCategory_Duplicate() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(DuplicateEntryException.class).when(categoryDAO).updateCategory(testCategory);

        //method under test
        categoryService.editCategory(testCategory);
    }

    @Test(expected = OperationFailedException.class)
    public void editCategory_DB_Error() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(DAOException.class).when(categoryDAO).updateCategory(testCategory);

        //method under test
        categoryService.editCategory(testCategory);
    }

    @Test
    public void editCategory_Success() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        //method under test
        categoryService.editCategory(testCategory);

        verify(categoryDAO).updateCategory(testCategory);
    }

    //Testing deleteCategory method
    @Test(expected = OperationFailedException.class)
    public void deleteCategory_Invalid_Id() {
        //method under test
        categoryService.deleteCategory(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteCategory_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(categoryDAO).deleteCategory(VALID_ID);

        //method under test
        categoryService.deleteCategory(VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteCategory_Not_Found() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(categoryDAO).deleteCategory(NON_EXISTING_ID);

        //method under test
        categoryService.deleteCategory(NON_EXISTING_ID);
    }

    @Test
    public void deleteCategory_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        categoryService.deleteCategory(VALID_ID);

        verify(categoryDAO).deleteCategory(VALID_ID);
    }

    //Testing getAllCategories method
    @Test
    public void getAllCategories_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        categoryService.getAllCategories();

        verify(categoryDAO).getAllCategories();
    }

    @Test(expected = OperationFailedException.class)
    public void getAllCategories_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(categoryDAO).getAllCategories();

        //method under test
        categoryService.getAllCategories();
    }

    //Testing deleteAllCategories method
    @Test
    public void deleteAllCategories_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        categoryService.deleteAllCategories();

        verify(categoryDAO).deleteAllCategories();
    }

    @Test(expected = OperationFailedException.class)
    public void deleteAllCategories_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(categoryDAO).deleteAllCategories();

        //method under test
        categoryService.deleteAllCategories();
    }
}
