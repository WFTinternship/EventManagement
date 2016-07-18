package datasource;

import com.workfront.internship.event_management.model.*;

import java.util.*;

/**
 * Created by Hermine Turshujyan 7/10/16.
 */
class TestHelper {

    static final String NON_EXISTING_USERNAME = "nonExistingUsername";
    static final String NON_EXISTING_EMAIL = "nonExistingEmail@test.com";
    static final String NON_EXISTING_MEDIA_TYPE = "Video";

    static int NON_EXISTING_ID = 1;


    //test object creation
    static User createTestUser() {
        User testUser = new User();
        testUser.setFirstName("Test FirstName")
                .setLastName("Test LastName")
                .setUsername("testUsername" + getRandomNumber())
                .setPassword("testPassword")
                .setEmail("test" + getRandomNumber() + "@test.com")
                .setPhoneNumber("1111111")
                .setAvatarPath("/users/test_user_av" + getRandomNumber() + ".jpg")
                .setVerified(false)
                .setRegistrationDate(new Date());
        return testUser;
    }

    static Category createTestCategory() {
        Category category = new Category();
        category.setTitle("Test Category" + getRandomNumber())
                .setDescription("Test Description")
                .setCreationDate(new Date());
        return category;
    }

    static Event createTestEvent() {
        Event testEvent = new Event();
        testEvent.setTitle("Test title")
                .setShortDescription("Test short description")
                .setFullDescription("Test full description")
                .setLocation("Test location")
                .setLat(11111.0)
                .setLng(11111.0)
                .setFilePath("/events/test_event.doc")
                .setImagePath("events/test_event.jpg")
                .setCreationDate(new Date())
                .setPublicAccessed(true)
                .setGuestsAllowed(true);

        return testEvent;
    }

    static Event createTestEventWithRecurrencesAndInvitations(User user1, User user2, RecurrenceType recurrenceType) {
        Event testEvent = new Event();
        testEvent.setTitle("Test title")
                .setShortDescription("Test short description")
                .setFullDescription("Test full description")
                .setLocation("Test location")
                .setLat(11111.0)
                .setLng(11111.0)
                .setFilePath("/events/test_event.doc")
                .setImagePath("events/test_event.jpg")
                .setCreationDate(new Date())
                .setPublicAccessed(true)
                .setGuestsAllowed(true);


        //create invitation list
        Invitation invitation1 = createTestInvitation();
        Invitation invitation2 = createTestInvitation();
        invitation1.setUser(user1);
        invitation2.setUser(user2);

        List<Invitation> invitationList = new ArrayList<>();
        invitationList.add(invitation1);
        invitationList.add(invitation2);

        //create recurrence list
        EventRecurrence recurrence1 = createTestEventRecurrence();
        EventRecurrence recurrence2 = createTestEventRecurrence();
        recurrence1.setRecurrenceType(recurrenceType);
        recurrence2.setRecurrenceType(recurrenceType);

        List<EventRecurrence> recurrenceList = new ArrayList<>();
        recurrenceList.add(recurrence1);

        //set to event
        testEvent.setEventRecurrences(recurrenceList);
        testEvent.setInvitations(invitationList);

        return testEvent;
    }


    static Invitation createTestInvitation() {
        Invitation invitation = new Invitation();
        invitation.setUserRole("Member")
                .setAttendeesCount(1)
                .setParticipated(false)
                .setUserResponse("Yes");
        return invitation;
    }

    static Media createTestMedia() {
        Media media = new Media();
        media.setPath("/event/111/test_path" + getRandomNumber() + ".jpg")
                .setType("Image")
                .setDescription("Test description")
                .setUploadDate(new Date());
        return media;
    }

    static RecurrenceOption createTestRecurrenceOption() {
        RecurrenceOption option = new RecurrenceOption();
        option.setTitle("Test repeat option" + getRandomNumber())
                .setAbbreviation("RO");
        return option;
    }

    static RecurrenceType createTestRecurrenceTypeWithOptions() {

        //create recurrence type option list
        RecurrenceOption option1 = createTestRecurrenceOption();
        RecurrenceOption option2 = createTestRecurrenceOption();
        List<RecurrenceOption> recurrenceOptionList = new ArrayList<>();
        recurrenceOptionList.add(option1);
        recurrenceOptionList.add(option2);

        //create recurrence type
        RecurrenceType recurrenceType = new RecurrenceType();
        recurrenceType.setTitle("Test recurrence type " + getRandomNumber())
                .setIntervalUnit("test unit")
                .setRecurrenceOptions(recurrenceOptionList);

        return recurrenceType;
    }

    static RecurrenceType createTestRecurrenceType() {
        RecurrenceType recurrenceType = new RecurrenceType();
        recurrenceType.setTitle("Test recurrence type " + getRandomNumber())
                .setIntervalUnit("test unit");
        return recurrenceType;
    }

    static EventRecurrence createTestEventRecurrence() {
        EventRecurrence eventRecurrence = new EventRecurrence();
        eventRecurrence.setRepeatInterval(2)
                .setRepeatOn("Test RepeatOn string")
                .setRepeatEndDate(new Date());

        return eventRecurrence;
    }

    //helper methods
    private static int getRandomNumber() {
        Random rand = new Random();
        int n = rand.nextInt(100000) + 1;
        return n;
    }

}
