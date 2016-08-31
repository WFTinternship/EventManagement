package com.workfront.internship.event_management;

import com.workfront.internship.event_management.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Hermine Turshujyan 7/10/16.
 */
public class TestObjectCreator {

    public static final String NON_EXISTING_EMAIL = "nonExistingEmail@nonExisting.email";
    public static final String NON_EXISTING_MEDIA_TYPE = "Video";
    public static final String NON_EXISTING_TITLE = "nonExistingTitle";
    public static final String INVALID_EMAIL = "nonExistingTitle";
    public static final String WRONG_PASSWORD = "wrongPassword";
    public static final String VALID_EMAIL = "turshujyan@gmail.com";
    public static final String VALID_PASSWORD = "turshujyan";

    public static int NON_EXISTING_ID = 1;
    public static int INVALID_ID = -1;
    public static int VALID_ID = 10;

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
                .setEndDate(new Date())
                .setCategory(createTestCategory());

        return testEvent;
    }


    public static Event createTestEventWithRecurrences(RecurrenceType recurrenceType) {
        Event testEvent = createTestEvent();

        //create recurrence list
        Recurrence recurrence1 = createTestRecurrence();
        Recurrence recurrence2 = createTestRecurrence();
        recurrence1.setRecurrenceType(recurrenceType);
        recurrence2.setRecurrenceType(recurrenceType);

        List<Recurrence> recurrenceList = new ArrayList<>();
        recurrenceList.add(recurrence1);

        //set to event
        testEvent.setEventRecurrences(recurrenceList);

        return testEvent;
    }


    public static Invitation createTestInvitation() {
        User user = createTestUser();
        user.setId(1);

        Invitation invitation = new Invitation();
        invitation.setUserRole("Member")
                .setAttendeesCount(1)
                .setParticipated(false)
                .setUserResponse("Yes")
                .setEventId(1)
                .setUser(user);

        return invitation;
    }

    public static Media createTestMedia() {
        Media media = new Media();
        media.setPath("/event/111/test_path" + getRandomNumber() + ".jpg")
                .setType(new MediaType(0, "Image"))
                .setDescription("Test description")
                .setUploadDate(new Date())
                .setEventId(1)
                .setUploaderId(1);
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
                .setAbbreviation("RO")
                .setRecurrenceTypeId(VALID_ID);
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

    public static Recurrence createTestRecurrence() {
        RecurrenceType recurrenceType = createTestRecurrenceType();

        Recurrence eventRecurrence = new Recurrence();
        eventRecurrence.setRepeatInterval(2)
                .setRepeatEndDate(new Date())
                .setRecurrenceType(recurrenceType)
                .setEventId(VALID_ID);

        return eventRecurrence;
    }

    //helper methods
    private static int getRandomNumber() {
        Random rand = new Random();
        int n = rand.nextInt(100000) + 1;
        return n;
    }
}
