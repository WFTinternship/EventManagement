package datasource;

/**
 * Created by hermine on 7/11/16.
 */
public class RecurrenceTypeDAOImplIntegrationTest {

  /*  private static RecurrenceTypeDAO recTypeDAO = null;
    RecurrenceType testRecurrenceType = null;


    @BeforeClass
    public static void setUpClass(){
        recTypeDAO = new RecurrenceTypeDAOImpl();
    }

    @Before
    public void setUp() {
        testRecurrenceType = datasource.TestHelper.createTestRecurrenceType();
        int recTypeId = datasource.TestHelper.insertTestRecurrenceTypeToDB(testRecurrenceType);
        testRecurrenceType.setId(recTypeId);
    }

    @After
    public void tearDown() {
        datasource.TestHelper.deleteTestRecurrenceTypeFromDB(testRecurrenceType.getId());
    }

    @Test
    public void  testInsertRecurrenceType(){
        datasource.TestHelper.deleteTestRecurrenceTypeFromDB(testRecurrenceType.getId());
        int newRecTypeId = recTypeDAO.addRecurrenceType(testRecurrenceType);
        RecurrenceType actualRecType = getTestRecurrenceType(newRecTypeId);
        try {
            assertEquals(testRecurrenceType.getTitle(), testRecurrenceType.getTitle());
            assertEquals(testRecurrenceType.getIntervalUnit(), testRecurrenceType.getIntervalUnit());
            assertNotNull(testRecurrenceType.getRepeatOptions());
        } finally {
            datasource.TestHelper.deleteTestRecurrenceTypeFromDB(newRecTypeId);
        }
    }

    @Test
    public void testGetAllRecurrenceTypes(){
        List<RecurrenceType> expectedRecTypes = getAllRecTypes();
        List<RecurrenceType> actualRecTypes = recTypeDAO.getAllRecurrenceTypes();

        assertEquals(actualRecTypes.size(), expectedRecTypes.size());
        for (int i = 0; i < actualRecTypes.size(); i++) {
            assertEquals(testRecurrenceType.getTitle(), testRecurrenceType.getTitle());
            assertEquals(testRecurrenceType.getIntervalUnit(), testRecurrenceType.getIntervalUnit());
        }
    }

    @Test
    public void  testGetRecurrenceTypeById(){
        RecurrenceType actualRecType = recTypeDAO.getRecurrenceTypeById(testRecurrenceType.getId());
        assertEquals(testRecurrenceType.getTitle(), testRecurrenceType.getTitle());
        assertEquals(testRecurrenceType.getIntervalUnit(), testRecurrenceType.getIntervalUnit());
        assertNotNull(testRecurrenceType.getRepeatOptions());
    }

    @Test
    public void  testDeleteRecurrenceType(){
        recTypeDAO.deleteRecurrenceType(testRecurrenceType.getId());
        assertNull(getTestRecurrenceType(testRecurrenceType.getId()));

    }*/
}
