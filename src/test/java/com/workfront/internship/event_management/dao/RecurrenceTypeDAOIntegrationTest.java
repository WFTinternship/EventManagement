package com.workfront.internship.event_management.dao;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
public class RecurrenceTypeDAOIntegrationTest {
/*
    private static RecurrenceTypeDAO recurrenceTypeDAO = null;
    private RecurrenceType testRecurrenceType = null;

    @BeforeClass
    public static void setUpClass(){
        recurrenceTypeDAO = new RecurrenceTypeDAOImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        recurrenceTypeDAO = null;
    }

    @Before
    public void setUp() {
        //create test recurrence type
        testRecurrenceType = TestObjectCreator.createTestRecurrenceTypeWithOptions();

        //insert test record into db and get generated id
        int recurrenceTypeId = recurrenceTypeDAO.addRecurrenceType(testRecurrenceType);
        testRecurrenceType.setId(recurrenceTypeId);
    }

    @After
    public void tearDown() throws DAOException {
        //delete test record from db
        recurrenceTypeDAO.deleteAllRecurrenceTypes();

        //delete test recurrenceType object
        testRecurrenceType = null;
    }

    @Test
    public void addRecurrenceTypeWithOptions_Success() {
        //test record already inserted in setup, read record by id
        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(testRecurrenceType.getId());

        assertNotNull(recurrenceType);
        assertEqualRecurrenceTypesWithOptions(recurrenceType, testRecurrenceType);
    }

    @Test
    public void addRecurrenceTypeWithoutOptions_Success() {
        //create recurrence type without options
        RecurrenceType newTestRecurrenceType = TestObjectCreator.createTestRecurrenceType();
        int id = recurrenceTypeDAO.addRecurrenceType(newTestRecurrenceType);
        newTestRecurrenceType.setId(id);

        //read inserted record by id
        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(newTestRecurrenceType.getId());

        assertNotNull(recurrenceType);
        assertEqualRecurrenceTypes(recurrenceType, newTestRecurrenceType);
    }

    @Test(expected = RuntimeException.class)
    public void addRecurrenceType_Dublicate_Entry() {
        //test recurrence type inserted in setup, insert dublicate entry
        recurrenceTypeDAO.addRecurrenceType(testRecurrenceType);
    }


    @Test
    public void getAllRecurrenceTypes_Found() {
        //test method
        List<RecurrenceType> recurrenceTypeList = recurrenceTypeDAO.getAllRecurrenceTypes();

        assertNotNull(recurrenceTypeList);
        assertFalse(recurrenceTypeList.isEmpty());
        assertEquals(recurrenceTypeList.size(), 1);

        assertEqualRecurrenceTypes(recurrenceTypeList.get(0), testRecurrenceType);
    }

    @Test
    public void getAllRecurrenceTypes_Empty_List() throws DAOException {
        //delete inserted record from db
        recurrenceTypeDAO.deleteRecurrenceType(testRecurrenceType.getId());

        //test method
        List<RecurrenceType> recurrenceTypeList = recurrenceTypeDAO.getAllRecurrenceTypes();

        assertNotNull(recurrenceTypeList);
        assertTrue(recurrenceTypeList.isEmpty());
    }

    @Test
    public void getRecurrenceTypeById_Found() {
        //test method (test record already inserted in seUp())
        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(testRecurrenceType.getId());

        assertNotNull(recurrenceType);
        assertEqualRecurrenceTypesWithOptions(recurrenceType, testRecurrenceType);
    }

    @Test
    public void getRecurrenceTypeById_Not_Found() {
        //test method
        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(TestObjectCreator.NON_EXISTING_ID);

        assertNull(recurrenceType);
    }

    @Test
    public void updateRecurrenceType_Found() {
        //update recurrence type info
        testRecurrenceType.setTitle("updated title")
                .setIntervalUnit("updated unit");

        //test method
        boolean updated = recurrenceTypeDAO.updateRecurrenceType(testRecurrenceType);

        //read updated record from db
        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(testRecurrenceType.getId());

        assertNotNull(recurrenceType);
        assertEqualRecurrenceTypes(recurrenceType, testRecurrenceType);
    }

    @Test
    public void updateRecurrenceType_Not_Found() {
        //create new recurrence type with non-existing id
        RecurrenceType recurrenceType = TestObjectCreator.createTestRecurrenceType();

        //test method
        boolean updated = recurrenceTypeDAO.updateRecurrenceType(recurrenceType);

        assertFalse(updated);
    }

    @Test
    public void deleteRecurrenceType_Found() throws DAOException {
        //testing method
        boolean deleted = recurrenceTypeDAO.deleteRecurrenceType(testRecurrenceType.getId());

        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(testRecurrenceType.getId());

        assertTrue(deleted);
        assertNull(recurrenceType);
    }

    @Test
    public void deleteRecurrenceType_Not_Found() throws DAOException {
        //testing method
        boolean deleted = recurrenceTypeDAO.deleteRecurrenceType(TestObjectCreator.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteAllRecurrenceTypes_Found() throws DAOException {
        //testing method
        boolean deleted = recurrenceTypeDAO.deleteAllRecurrenceTypes();

        List<RecurrenceType> recurrenceTypeList = recurrenceTypeDAO.getAllRecurrenceTypes();

        assertNotNull(recurrenceTypeList);
        assertTrue(recurrenceTypeList.isEmpty());
        assertTrue(deleted);
    }

    @Test
    public void deleteAllRecurrenceTypes_Not_Found() throws DAOException {
        //delete inserted test record
        recurrenceTypeDAO.deleteRecurrenceType(testRecurrenceType.getId());

        //testing method
        boolean deleted = recurrenceTypeDAO.deleteAllRecurrenceTypes();

        assertFalse(deleted);
    }


    //helper methods
    private void assertEqualRecurrenceTypesWithOptions(RecurrenceType actualRecurrenceType, RecurrenceType expectedRecurrenceType) {

        assertEqualRecurrenceTypes(actualRecurrenceType, expectedRecurrenceType);

        assertNotNull(actualRecurrenceType.getRecurrenceOptions());
        assertEquals(actualRecurrenceType.getRecurrenceOptions().size(), expectedRecurrenceType.getRecurrenceOptions().size());
    }

    private void assertEqualRecurrenceTypes(RecurrenceType actualRecurrenceType, RecurrenceType expectedRecurrenceType) {

        assertEquals(actualRecurrenceType.getTitle(), expectedRecurrenceType.getTitle());
        assertEquals(actualRecurrenceType.getIntervalUnit(), expectedRecurrenceType.getIntervalUnit());

        // assertNull(actualRecurrenceType.getRecurrenceOptions());
    }
*/

}
