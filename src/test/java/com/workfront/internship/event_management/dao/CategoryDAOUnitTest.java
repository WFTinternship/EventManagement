package com.workfront.internship.event_management.dao;

/**
 * Created by Hermine Turshujyan 7/18/16.
 */
public class CategoryDAOUnitTest {

   /* private DataSourceManager dataSourceManager;
    private CategoryDAO categoryDAO;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSourceManager = Mockito.mock(DataSourceManager.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSourceManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        categoryDAO = new CategoryDAOImpl(dataSourceManager);
    }

    @Test(expected = DAOException.class)
    public void addCategory_dbError() throws DAOException {
        categoryDAO.addCategory(new Category());
    }

    @Test(expected = DAOException.class)
    public void getAllCategories_dbError() throws DAOException {
        categoryDAO.getAllCategories();
    }

    @Test(expected = DAOException.class)
    public void getCategoryById_dbError() throws DAOException {
        categoryDAO.getCategoryById(1);
    }

    @Test(expected = DAOException.class)
    public void getCategoryByTitle_dbError() throws DAOException {
        categoryDAO.getCategoryByTitle("");
    }


    @Test(expected = DAOException.class)
    public void updateCategory_dbError() throws DAOException {
        categoryDAO.updateCategory(new Category());
    }

    @Test(expected = DAOException.class)
    public void deleteUser_dbError() throws DAOException {
        categoryDAO.deleteCategory(1);
    }

    @Test(expected = DAOException.class)
    public void deleteAllUsers_dbError() throws DAOException {
        categoryDAO.deleteAllCategories();
    }
*/
}
