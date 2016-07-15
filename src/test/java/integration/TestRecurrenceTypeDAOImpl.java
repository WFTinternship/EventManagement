package integration;

/**
 * Created by hermine on 7/11/16.
 */
public class TestRecurrenceTypeDAOImpl {

  /*  private static RecurrenceTypeDAO recTypeDAO = null;
    RecurrenceType testRecurrenceType = null;


    @BeforeClass
    public static void setUpClass(){
        recTypeDAO = new RecurrenceTypeDAOImpl();
    }

    @Before
    public void setUp() {
        testRecurrenceType = integration.TestHelper.createTestRecurrenceType();
        int recTypeId = integration.TestHelper.insertTestRecurrenceTypeToDB(testRecurrenceType);
        testRecurrenceType.setId(recTypeId);
    }

    @After
    public void tearDown() {
        integration.TestHelper.deleteTestRecurrenceTypeFromDB(testRecurrenceType.getId());
    }

    @Test
    public void  testInsertRecurrenceType(){
        integration.TestHelper.deleteTestRecurrenceTypeFromDB(testRecurrenceType.getId());
        int newRecTypeId = recTypeDAO.insertRecurrenceType(testRecurrenceType);
        RecurrenceType actualRecType = getTestRecurrenceType(newRecTypeId);
        try {
            assertEquals(testRecurrenceType.getTitle(), testRecurrenceType.getTitle());
            assertEquals(testRecurrenceType.getIntervalUnit(), testRecurrenceType.getIntervalUnit());
            assertNotNull(testRecurrenceType.getRepeatOptions());
        } finally {
            integration.TestHelper.deleteTestRecurrenceTypeFromDB(newRecTypeId);
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
