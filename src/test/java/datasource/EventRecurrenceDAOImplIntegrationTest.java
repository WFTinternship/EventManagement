package datasource;

/**
 * Created by hermine on 7/13/16.
 */
public class EventRecurrenceDAOImplIntegrationTest {

  /*  private static EventRecurrenceDAO eventRecurrenceDAO;
    private EventCategory testCategory;
    private Event testEvent;
    private RecurrenceType testRecurrenceType;
    private EventRecurrence testEventRecurrence;

    @BeforeClass
    public static void setUpClass() {
        eventRecurrenceDAO = new EventRecurrenceDAOImpl();
    }

    @Before
    public void setUp() {
        //creating test objects
        testCategory = datasource.TestHelper.createTestCategory();
        testEvent = datasource.TestHelper.createTestEvent();
        testRecurrenceType = datasource.TestHelper.createTestRecurrenceType();
        testEventRecurrence = datasource.TestHelper.createTestEventRecurrence();

        //inserting test objects to db
        int categoryId = datasource.TestHelper.insertTestCategoryToDB(testCategory);
        testCategory.setId(categoryId);

        testEvent.setCategory(testCategory);
        int eventId = datasource.TestHelper.insertTestEventToDB(testEvent);
        testEvent.setId(eventId);

        testRecurrenceType = datasource.TestHelper.createTestRecurrenceType();
        int recTypeId = datasource.TestHelper.insertTestRecurrenceTypeToDB(testRecurrenceType);
        testRecurrenceType.setId(recTypeId);

        testEventRecurrence.setEventId(testEvent.getId()).setRecurrenceType(testRecurrenceType);
        int evRecId = datasource.TestHelper.insertTestEventRecurrenceToDB(testEventRecurrence);
        testEventRecurrence.setId(evRecId);

    }

    @After
    public void tearDown() {
        datasource.TestHelper.deleteTestEventRecurrenceFromDB(testEventRecurrence.getId());
        datasource.TestHelper.deleteTestRecurrenceTypeFromDB(testRecurrenceType.getId());
        datasource.TestHelper.deleteTestEventFromDB(testEvent.getId());
        datasource.TestHelper.deleteTestCategoryFromDB(testCategory.getId());

        testRecurrenceType = null;
        testEventRecurrence = null;
        testEvent = null;
        testCategory = null;
    }

    @Test
    public void testInsertEventRecurrence(){
        datasource.TestHelper.deleteTestEventRecurrenceFromDB(testEventRecurrence.getId());
        int newEvRecId = eventRecurrenceDAO.addEventRecurrence(testEventRecurrence);
        EventRecurrence actualRecurrence = getTestEventRecurrenceFromDB(newEvRecId);

        try {
            assertEquals(actualRecurrence.getEventId(), testEventRecurrence.getEventId());
            assertEquals(actualRecurrence.getRepeatOn(), testEventRecurrence.getRepeatOn());
            assertEquals(actualRecurrence.getRepeatInterval(), testEventRecurrence.getRepeatInterval());
            assertNotNull(actualRecurrence.getRepeatEndDate());
        } finally {
            datasource.TestHelper.deleteTestEventRecurrenceFromDB(newEvRecId);
        }
    }

    @Test //---
    public void testInsertEventRecurrences(){
//        datasource.TestHelper.deleteTestEventRecurrenceFromDB(testEventRecurrence.getId());
//        int newEvRecId = eventRecurrenceDAO.addEventRecurrence(testEventRecurrence);
//        EventRecurrence actualRecurrence = getTestEventRecurrenceFromDB(newEvRecId);
//
//        try {
//            assertEquals(actualRecurrence.getEventId(), testEventRecurrence.getEventId());
//            assertEquals(actualRecurrence.getRepeatOn(), testEventRecurrence.getRepeatOn());
//            assertEquals(actualRecurrence.getRepeatInterval(), testEventRecurrence.getRepeatInterval());
//            assertNotNull(actualRecurrence.getRepeatEndDate());
//        } finally {
//            datasource.TestHelper.deleteTestEventRecurrenceFromDB(newEvRecId);
//        }
    }

    @Test //--
    public void testGetEventRecurrencesByEventId(){

    }

    @Test
    public void testUpdateEventRecurrence(){
        testEventRecurrence.setRepeatInterval(7).setRepeatOn("New test repeat on string");
        eventRecurrenceDAO.updateEventRecurrence(testEventRecurrence);
        EventRecurrence actualRecurrence = getTestEventRecurrencesFromDB("id", testEventRecurrence.getId()).get(0);

        assertEquals(actualRecurrence.getEventId(), testEventRecurrence.getEventId());
        assertEquals(actualRecurrence.getRepeatOn(), testEventRecurrence.getRepeatOn());
        assertEquals(actualRecurrence.getRepeatInterval(), testEventRecurrence.getRepeatInterval());
    }

    @Test //---
    public void testDeleteEventRecurrece(){
        eventRecurrenceDAO.deleteEventRecurrece(testEventRecurrence.getId());
        getTestEventRecurrencesFromDB("id", testEventRecurrence.getId());
    }
*/
}
