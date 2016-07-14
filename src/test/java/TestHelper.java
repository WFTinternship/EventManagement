import com.workfront.internship.event_management.model.*;
import com.workfront.internship.event_management.model.RecurrenceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/10/16.
 */
public class TestHelper {

    //test object creation
    public static User createTestUser() {
        User testUser = new User();
        java.util.Date currentDate = new java.util.Date();
        testUser.setFirstName("Test FirstName")
                .setLastName("Test LastName")
                .setUsername("Test Username")
                .setPassword("Test Password")
                .setEmail("test-test@test.com")
                .setPhoneNumber("1111111")
                .setAvatarPath("/users/test_user_av.jpg")
                .setVerified(false)
                .setRegistrationDate(currentDate);
        return testUser;
    }

    public static EventCategory createTestCategory() {
        EventCategory category = new EventCategory();
        category.setTitle("Test Category")
                .setDescription("Test Description");
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
        media.setPath("/event/111/test_path.jpg")
                .setType("Image")
                .setDescription("Test description")
                .setUploadDate(new java.util.Date());
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
        eventRecurrence.setRepeatInterval(2).setRepeatOn("Test RepeatOn string").setRepeatEndDate(new java.util.Date());
        return eventRecurrence;
    }

}
