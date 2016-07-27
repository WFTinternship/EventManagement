package com.workfront.internship.event_management;

import com.workfront.internship.event_management.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Hermine Turshujyan 7/10/16.
 */
public class TestHelper {

    public static final String NON_EXISTING_USERNAME = "nonExistingUsername";
    public static final String NON_EXISTING_EMAIL = "nonExistingEmail@test.com";
    public static final String NON_EXISTING_MEDIA_TYPE = "Video";
    public static final String NON_EXISTING_TITLE = "nonExistingTitle";
    public static final String INVALID_EMAIL = "nonExistingTitle";

    public static int NON_EXISTING_ID = 1;
    public static int INVALID_ID = -1;



    //test object creation
    public static User createTestUser() {
        User testUser = new User();
        testUser.setFirstName("Test FirstName")
                .setLastName("Test LastName")
                .setPassword("testPassword")
                .setEmail("test" + getRandomNumber() + "@test.com")
                .setPhoneNumber("1111111")
                .setAvatarPath("/users/test_user_av" + getRandomNumber() + ".jpg")
                .setVerified(false)
                .setRegistrationDate(new Date());
        return testUser;
    }

    public static Category createTestCategory() {
        Category category = new Category();
        category.setTitle("Test Category" + getRandomNumber())
                .setDescription("Test Description")
                .setCreationDate(new Date());
        return category;
    }

    public static Event createTestEvent() {
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
                .setGuestsAllowed(true)
                .setStartDate(new Date())
                .setEndDate(new Date());

        return testEvent;
    }


    public static Event createTestEventWithRecurrences(RecurrenceType recurrenceType) {
        Event testEvent = createTestEvent();

        //create recurrence list
        EventRecurrence recurrence1 = createTestEventRecurrence();
        EventRecurrence recurrence2 = createTestEventRecurrence();
        recurrence1.setRecurrenceType(recurrenceType);
        recurrence2.setRecurrenceType(recurrenceType);

        List<EventRecurrence> recurrenceList = new ArrayList<>();
        recurrenceList.add(recurrence1);

        //set to event
        testEvent.setEventRecurrences(recurrenceList);

        return testEvent;
    }


    public static Invitation createTestInvitation() {
        Invitation invitation = new Invitation();
        invitation.setUserRole("Member")
                .setAttendeesCount(1)
                .setParticipated(false)
                .setUserResponse(new UserResponse(1, "Yes"));
        return invitation;
    }

    public static Media createTestMedia() {
        Media media = new Media();
        media.setPath("/event/111/test_path" + getRandomNumber() + ".jpg")
                .setType(new MediaType(0, "Image"))
                .setDescription("Test description")
                .setUploadDate(new Date());
        return media;
    }

    public static MediaType createTestMediaType() {
        MediaType mediaType = new MediaType();
        mediaType.setTitle("Image" + getRandomNumber());

        return mediaType;
    }


    public static RecurrenceOption createTestRecurrenceOption() {
        RecurrenceOption option = new RecurrenceOption();
        option.setTitle("Test repeat option" + getRandomNumber())
                .setAbbreviation("RO");
        return option;
    }

    public static RecurrenceType createTestRecurrenceTypeWithOptions() {

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

    public static RecurrenceType createTestRecurrenceType() {
        RecurrenceType recurrenceType = new RecurrenceType();
        recurrenceType.setTitle("Test recurrence type " + getRandomNumber())
                .setIntervalUnit("test unit");
        return recurrenceType;
    }

    public static EventRecurrence createTestEventRecurrence() {
        EventRecurrence eventRecurrence = new EventRecurrence();
        eventRecurrence.setRepeatInterval(2)
                .setRepeatEndDate(new Date());

        return eventRecurrence;
    }

    //assertion methods
    public static void assertEqualUsers(User expectedUser, User actualUser) {
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.getPhoneNumber(), actualUser.getPhoneNumber());
        assertEquals(expectedUser.getAvatarPath(), actualUser.getAvatarPath());
        assertEquals(expectedUser.isVerified(), actualUser.isVerified());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertNotNull(expectedUser.getRegistrationDate()); // TODO: 7/27/16 check date type
    }


    //helper methods
    private static int getRandomNumber() {
        Random rand = new Random();
        int n = rand.nextInt(100000) + 1;
        return n;
    }


}
