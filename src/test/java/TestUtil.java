import com.workfront.internship.event_management.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/10/16.
 */
public class TestUtil {

    public static User setUpTestUser() {
        User testUser = new User();
        testUser.setId(111111)
                .setFirstName("TestFirstName")
                .setLastName("TestLastName")
                .setUsername("TestUsername")
                .setPassword("TestPassword")
                .setEmail("test@test.com")
                .setPhoneNumber("1111111")
                .setAvatarPath("/users/test_user.jpg");
        return testUser;
    }

    public static EventInvitation setUpTestInvitation(User testUser, Event testEvent) {
        EventInvitation invitation = new EventInvitation();
        invitation.setEventId(testEvent.getId())
                .setUser(testUser)
                .setUserRole("Member");
        return invitation;
    }

    public static List<EventInvitation> setUpTestInvitationsList(User testUser1, User testUser2,Event testEvent) {
        List<EventInvitation> invitationsList = new ArrayList<EventInvitation>();
        EventInvitation invitation1 = new EventInvitation();
        EventInvitation invitation2 = new EventInvitation();
        invitation1.setEventId(testEvent.getId()).setUser(testUser1).setUserRole("Member");
        invitation2.setEventId(testEvent.getId()).setUser(testUser2).setUserRole("Organizer");
        invitationsList.add(invitation1);
        invitationsList.add(invitation2);
        return invitationsList;
    }


    public static EventCategory setUpTestCategory() {
        EventCategory category = new EventCategory();
        category.setId(111111)
                .setTitle("TestCategory")
                .setDescription("TestDescription");
        return category;
    }

    public static Event setUpTestEvent(EventCategory category) {
        Event testEvent = new Event();
        testEvent.setId(111111)
                .setTitle("Test title")
                .setShortDesc("Test short description")
                .setFullDesc("Test full description")
                .setLocation("Test location")
                .setLat(11111.1f)
                .setLng(11111.1f)
                .setFilePath("/events/test_event.doc")
                .setImagePath("events/test_event.jpg")
                .setCategory(category);
        return testEvent;
    }

    public static EventMedia setUpTestMedia(User testUser, Event testEvent) {
        EventMedia media = new EventMedia();
        media.setId(11111)
                .setEventId(testEvent.getId())
                .setUploaderId(testUser.getId())
                .setPath("/event/111/test_path.jpg")
                .setType("Image")
                .setDescription("Test description");
        return media;
    }
}
