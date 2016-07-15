package integration;

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
        testCategory = integration.TestHelper.createTestCategory();
        testEvent = integration.TestHelper.createTestEvent();
        testRecurrenceType = integration.TestHelper.createTestRecurrenceType();
        testEventRecurrence = integration.TestHelper.createTestEventRecurrence();

        //inserting test objects to db
        int categoryId = integration.TestHelper.insertTestCategoryToDB(testCategory);
        testCategory.setId(categoryId);

        testEvent.setCategory(testCategory);
        int eventId = integration.TestHelper.insertTestEventToDB(testEvent);
        testEvent.setId(eventId);

        testRecurrenceType = integration.TestHelper.createTestRecurrenceType();
        int recTypeId = integration.TestHelper.insertTestRecurrenceTypeToDB(testRecurrenceType);
        testRecurrenceType.setId(recTypeId);

        testEventRecurrence.setEventId(testEvent.getId()).setRecurrenceType(testRecurrenceType);
        int evRecId = integration.TestHelper.insertTestEventRecurrenceToDB(testEventRecurrence);
        testEventRecurrence.setId(evRecId);

    }

    @After
    public void tearDown() {
        integration.TestHelper.deleteTestEventRecurrenceFromDB(testEventRecurrence.getId());
        integration.TestHelper.deleteTestRecurrenceTypeFromDB(testRecurrenceType.getId());
        integration.TestHelper.deleteTestEventFromDB(testEvent.getId());
        integration.TestHelper.deleteTestCategoryFromDB(testCategory.getId());

        testRecurrenceType = null;
        testEventRecurrence = null;
        testEvent = null;
        testCategory = null;
    }

    @Test
    public void testInsertEventRecurrence(){
        integration.TestHelper.deleteTestEventRecurrenceFromDB(testEventRecurrence.getId());
        int newEvRecId = eventRecurrenceDAO.addEventRecurrence(testEventRecurrence);
        EventRecurrence actualRecurrence = getTestEventRecurrenceFromDB(newEvRecId);

        try {
            assertEquals(actualRecurrence.getEventId(), testEventRecurrence.getEventId());
            assertEquals(actualRecurrence.getRepeatOn(), testEventRecurrence.getRepeatOn());
            assertEquals(actualRecurrence.getRepeatInterval(), testEventRecurrence.getRepeatInterval());
            assertNotNull(actualRecurrence.getRepeatEndDate());
        } finally {
            integration.TestHelper.deleteTestEventRecurrenceFromDB(newEvRecId);
        }
    }

    @Test //---
    public void testInsertEventRecurrences(){
//        integration.TestHelper.deleteTestEventRecurrenceFromDB(testEventRecurrence.getId());
//        int newEvRecId = eventRecurrenceDAO.addEventRecurrence(testEventRecurrence);
//        EventRecurrence actualRecurrence = getTestEventRecurrenceFromDB(newEvRecId);
//
//        try {
//            assertEquals(actualRecurrence.getEventId(), testEventRecurrence.getEventId());
//            assertEquals(actualRecurrence.getRepeatOn(), testEventRecurrence.getRepeatOn());
//            assertEquals(actualRecurrence.getRepeatInterval(), testEventRecurrence.getRepeatInterval());
//            assertNotNull(actualRecurrence.getRepeatEndDate());
//        } finally {
//            integration.TestHelper.deleteTestEventRecurrenceFromDB(newEvRecId);
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
