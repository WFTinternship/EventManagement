package datasource;

import com.workfront.internship.event_management.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by hermine on 7/10/16.
 */
public class TestHelper {

    public static final String NON_EXISTING_USERNAME = "nonExistingUsername";
    public static final String NON_EXISTING_EMAIL = "nonExistingEmail@test.com";
    public static final String NON_EXISTING_MEDIA_TYPE = "Video";

    public static int NON_EXISTING_ID = 12345;


    //test object creation
    public static User createTestUser() {
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

    public static EventCategory createTestCategory() {
        EventCategory category = new EventCategory();
        category.setTitle("Test Category" + uuid())
                .setDescription("Test Description")
                .setCreationDate(new Date());
        return category;
    }

    public static Event createTestEvent() {
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

    public static EventInvitation createTestInvitation() {
        EventInvitation invitation = new EventInvitation();
        invitation.setUserRole("Member")
                .setAttendeesCount(1)
                .setParticipated(false)
                .setUserResponse("Yes");
        return invitation;
    }

    public static EventMedia createTestMedia() {
        EventMedia media = new EventMedia();
        media.setPath("/event/111/test_path" + uuid() + ".jpg")
                .setType("Image")
                .setDescription("Test description")
                .setUploadDate(new Date());
        return media;
    }

    public static RecurrenceType createTestRecurrenceType() {
        List<String> repeatOn = new ArrayList<String>();
        repeatOn.add("Test reoeat on 1");
        repeatOn.add("Test reoeat on 2");

        RecurrenceType recType = new RecurrenceType();
       // recType.setTitle("Test recurrence type").setIntervalUnit("test unit").setRepeatOptions(repeatOn);
        return recType;
    }

    public static EventRecurrence createTestEventRecurrence() {
        EventRecurrence eventRecurrence = new EventRecurrence();
        eventRecurrence.setRepeatInterval(2).setRepeatOn("Test RepeatOn string").setRepeatEndDate(new Date());
        return eventRecurrence;
    }

    //helper methods
    private static String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
