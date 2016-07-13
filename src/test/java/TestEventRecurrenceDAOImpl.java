/**
 * Created by hermine on 7/13/16.
 */
public class TestEventRecurrenceDAOImpl {

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
        testCategory = TestHelper.createTestCategory();
        testEvent = TestHelper.createTestEvent();
        testRecurrenceType = TestHelper.createTestRecurrenceType();
        testEventRecurrence = TestHelper.createTestEventRecurrence();

        //inserting test objects to db
        int categoryId = TestHelper.insertTestCategoryToDB(testCategory);
        testCategory.setId(categoryId);

        testEvent.setCategory(testCategory);
        int eventId = TestHelper.insertTestEventToDB(testEvent);
        testEvent.setId(eventId);

        testRecurrenceType = TestHelper.createTestRecurrenceType();
        int recTypeId = TestHelper.insertTestRecurrenceTypeToDB(testRecurrenceType);
        testRecurrenceType.setId(recTypeId);

        testEventRecurrence.setEventId(testEvent.getId()).setRecurrenceType(testRecurrenceType);
        int evRecId = TestHelper.insertTestEventRecurrenceToDB(testEventRecurrence);
        testEventRecurrence.setId(evRecId);

    }

    @After
    public void tearDown() {
        TestHelper.deleteTestEventRecurrenceFromDB(testEventRecurrence.getId());
        TestHelper.deleteTestRecurrenceTypeFromDB(testRecurrenceType.getId());
        TestHelper.deleteTestEventFromDB(testEvent.getId());
        TestHelper.deleteTestCategoryFromDB(testCategory.getId());

        testRecurrenceType = null;
        testEventRecurrence = null;
        testEvent = null;
        testCategory = null;
    }

    @Test
    public void testInsertEventRecurrence(){
        TestHelper.deleteTestEventRecurrenceFromDB(testEventRecurrence.getId());
        int newEvRecId = eventRecurrenceDAO.insertEventRecurrence(testEventRecurrence);
        EventRecurrence actualRecurrence = getTestEventRecurrenceFromDB(newEvRecId);

        try {
            assertEquals(actualRecurrence.getEventId(), testEventRecurrence.getEventId());
            assertEquals(actualRecurrence.getRepeatOn(), testEventRecurrence.getRepeatOn());
            assertEquals(actualRecurrence.getRepeatInterval(), testEventRecurrence.getRepeatInterval());
            assertNotNull(actualRecurrence.getRepeatEndDate());
        } finally {
            TestHelper.deleteTestEventRecurrenceFromDB(newEvRecId);
        }
    }

    @Test //---
    public void testInsertEventRecurrences(){
//        TestHelper.deleteTestEventRecurrenceFromDB(testEventRecurrence.getId());
//        int newEvRecId = eventRecurrenceDAO.insertEventRecurrence(testEventRecurrence);
//        EventRecurrence actualRecurrence = getTestEventRecurrenceFromDB(newEvRecId);
//
//        try {
//            assertEquals(actualRecurrence.getEventId(), testEventRecurrence.getEventId());
//            assertEquals(actualRecurrence.getRepeatOn(), testEventRecurrence.getRepeatOn());
//            assertEquals(actualRecurrence.getRepeatInterval(), testEventRecurrence.getRepeatInterval());
//            assertNotNull(actualRecurrence.getRepeatEndDate());
//        } finally {
//            TestHelper.deleteTestEventRecurrenceFromDB(newEvRecId);
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
