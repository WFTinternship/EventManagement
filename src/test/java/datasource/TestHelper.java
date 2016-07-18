package datasource;

import com.workfront.internship.event_management.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Hermine Turshujyan 7/10/16.
 */
class TestHelper {

    static final String NON_EXISTING_USERNAME = "nonExistingUsername";
    static final String NON_EXISTING_EMAIL = "nonExistingEmail@test.com";
    static final String NON_EXISTING_MEDIA_TYPE = "Video";

    static int NON_EXISTING_ID = 12345;


    //test object creation
    static User createTestUser() {
        User testUser = new User();
        testUser.setFirstName("Test FirstName")
                .setLastName("Test LastName")
                .setUsername("testUsername" + uuid())
                .setPassword("testPassword")
                .setEmail("test" + uuid() + "@test.com")
                .setPhoneNumber("1111111")
                .setAvatarPath("/users/test_user_av" + uuid() + ".jpg")
                .setVerified(false)
                .setRegistrationDate(new Date());
        return testUser;
    }

    static Category createTestCategory() {
        Category category = new Category();
        category.setTitle("Test Category" + uuid())
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

    static Event createTestEventWithRecurrencesAndInvitations() {
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

        List<EventRecurrence> recurrenceList = new ArrayList<>();
        recurrenceList.add(createTestEventRecurrence());
        recurrenceList.add(createTestEventRecurrence());

        Event event = createTestEvent();
        User user1 = createTestUser();
        User user2 = createTestUser();
        Invitation invitation1 = createTestInvitation();
        Invitation invitation2 = createTestInvitation();
        invitation1.setUser(user1);
        invitation2.setUser(user2);

        List<Invitation> invitationList = new ArrayList<>();
        //  invitationList.add();
        invitationList.add(createTestInvitation());

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
        media.setPath("/event/111/test_path" + uuid() + ".jpg")
                .setType("Image")
                .setDescription("Test description")
                .setUploadDate(new Date());
        return media;
    }

    static RecurrenceOption createTestRecurrenceOption() {
        RecurrenceOption option = new RecurrenceOption();
        option.setTitle("Test repeat option" + uuid())
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
        recurrenceType.setTitle("Test recurrence type " + uuid())
                .setIntervalUnit("test unit")
                .setRecurrenceOptions(recurrenceOptionList);

        return recurrenceType;
    }

    static RecurrenceType createTestRecurrenceType() {
        RecurrenceType recurrenceType = new RecurrenceType();
        recurrenceType.setTitle("Test recurrence type " + uuid())
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
    private static String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
