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
                .setShortDesc("Test short description")
                .setFullDesc("Test full description")
                .setLocation("Test location")
                .setLat(11111.1f)
                .setLng(11111.1f)
                .setFilePath("/events/test_event.doc")
                .setImagePath("events/test_event.jpg");
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

    static RecurrenceType createTestRecurrenceType() {
        RecurrenceType recType = new RecurrenceType();
        recType.setTitle("Test recurrence type " + uuid())
                .setIntervalUnit("test unit");
        return recType;
    }

    static Recurrence createTestEventRecurrence() {
        Recurrence eventRecurrence = new Recurrence();
        eventRecurrence.setRepeatInterval(2).setRepeatOn("Test RepeatOn string").setRepeatEndDate(new Date());
        return eventRecurrence;
    }

    //helper methods
    private static String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
