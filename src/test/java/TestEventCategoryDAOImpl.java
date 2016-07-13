/**
 * Created by hermine on 7/9/16.
 */
public class TestEventCategoryDAOImpl {

  /*  private static EventCategoryDAO categoryDAO;
    private EventCategory testCategory;

    @BeforeClass
    public static void setUpClass() {
        categoryDAO = new EventCategoryDAOImpl();
    }

    @Before
    public void setUp() {
        testCategory = TestHelper.createTestCategory();
        int categoryId = categoryDAO.insertCategory(testCategory);
        testCategory.setId(categoryId);
    }

    @After
    public void tearDown() {
        categoryDAO.deleteCategory(testCategory.getId());
        testCategory = null;
    }

    @Test
    public void testInsertCategory(){
        TestHelper.deleteTestCategoryFromDB(testCategory.getId());
        int newCatId = categoryDAO.insertCategory(testCategory);
        EventCategory actualCategory = getTestCategory(newCatId);
        try {
            assertEquals(actualCategory.getTitle(), testCategory.getTitle());
            assertEquals(actualCategory.getDescription(), testCategory.getDescription());
        } finally {
            TestHelper.deleteTestCategoryFromDB(newCatId);
        }
    }

    @Test
    public void testGetAllCategories(){
        List<EventCategory> expectedCategories = getAllCategories();
        List<EventCategory> actualCategories = categoryDAO.getAllCategories();

        assertEquals(actualCategories.size(), expectedCategories.size());
        for (int i = 0; i < actualCategories.size(); i++) {
            assertEquals(actualCategories.get(i).getId(), expectedCategories.get(i).getId());
            assertEquals(actualCategories.get(i).getTitle(), expectedCategories.get(i).getTitle());
            assertEquals(actualCategories.get(i).getDescription(), expectedCategories.get(i).getDescription());
            assertEquals(actualCategories.get(i).getCreationDate(), expectedCategories.get(i).getCreationDate());
        }
    }

    @Test
    public void testGetCategoryById() {
        EventCategory actualCategory = categoryDAO.getCategoryById(testCategory.getId());

        assertEquals(actualCategory.getId(), testCategory.getId());
        assertEquals(actualCategory.getTitle(), testCategory.getTitle());
        assertEquals(actualCategory.getDescription(), testCategory.getDescription());
    }

    @Test
    public void testUpdateCategory()  {
        testCategory.setDescription("New test description");
        categoryDAO.updateCategory(testCategory);
        EventCategory actualCategory = getTestCategory(testCategory.getId());

        assertEquals(actualCategory.getId(), testCategory.getId());
        assertEquals(actualCategory.getTitle(), testCategory.getTitle());
        assertEquals(actualCategory.getDescription(), testCategory.getDescription());
    }

    @Test
    public void testDeleteCategory() {
        //test method
        categoryDAO.deleteCategory(testCategory.getId());

        assertNull(getTestCategory(testCategory.getId()));
    }
*/

}
