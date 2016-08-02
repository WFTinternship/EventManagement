package com.workfront.internship.event_management;

import com.workfront.internship.event_management.model.*;
import junit.framework.TestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class AssertionHelper {
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

    public static void assertEqualCategories(Category expectedCategory, Category actualCategory) {
        assertEquals(expectedCategory.getId(), actualCategory.getId());
        assertEquals(expectedCategory.getTitle(), actualCategory.getTitle());
        assertEquals(expectedCategory.getDescription(), actualCategory.getDescription());
        assertNotNull(expectedCategory.getCreationDate());
    }

    public static void assertEqualRecurrenceOptions(RecurrenceOption actualRecurrenceOption, RecurrenceOption expectedRecurrenceOption) {
        TestCase.assertEquals(actualRecurrenceOption.getId(), expectedRecurrenceOption.getId());
        TestCase.assertEquals(actualRecurrenceOption.getRecurrenceTypeId(), expectedRecurrenceOption.getRecurrenceTypeId());
        TestCase.assertEquals(actualRecurrenceOption.getAbbreviation(), expectedRecurrenceOption.getAbbreviation());
        TestCase.assertEquals(actualRecurrenceOption.getTitle(), expectedRecurrenceOption.getTitle());

    }

    public static void assertEqualMediaTypes(MediaType expectedMediaType, MediaType actualMediaType) {
        assertEquals(expectedMediaType.getId(), actualMediaType.getId());
        assertEquals(expectedMediaType.getTitle(), actualMediaType.getTitle());
    }

    public static void assertEqualMedia(Media expectedMedia, Media actualMedia) {
        assertEquals(actualMedia.getId(), expectedMedia.getId());
        assertEquals(actualMedia.getEventId(), expectedMedia.getEventId());
        assertEquals(actualMedia.getPath(), expectedMedia.getPath());
        assertEquals(actualMedia.getType().getId(), expectedMedia.getType().getId());
        assertEquals(actualMedia.getType().getTitle(), expectedMedia.getType().getTitle());
        assertEquals(actualMedia.getDescription(), expectedMedia.getDescription());
        assertEquals(actualMedia.getUploaderId(), expectedMedia.getUploaderId());
        assertNotNull(actualMedia.getUploadDate());
    }

    public static void assertEqualRecurrenceTypesWithOptions(RecurrenceType actualRecurrenceType, RecurrenceType expectedRecurrenceType) {

        assertEqualRecurrenceTypes(actualRecurrenceType, expectedRecurrenceType);

        assertNotNull(actualRecurrenceType.getRecurrenceOptions());
        assertEquals(actualRecurrenceType.getRecurrenceOptions().size(), expectedRecurrenceType.getRecurrenceOptions().size());
    }

    public static void assertEqualRecurrenceTypes(RecurrenceType actualRecurrenceType, RecurrenceType expectedRecurrenceType) {

        assertEquals(actualRecurrenceType.getTitle(), expectedRecurrenceType.getTitle());
        assertEquals(actualRecurrenceType.getIntervalUnit(), expectedRecurrenceType.getIntervalUnit());
    }

    public static void assertEqualRecurrences(Recurrence actualEventRecurrence, Recurrence expectedEventRecurrence) {
        assertEquals(actualEventRecurrence.getEventId(), expectedEventRecurrence.getEventId());
        assertEquals(actualEventRecurrence.getRecurrenceType().getId(), expectedEventRecurrence.getRecurrenceType().getId());

        if (actualEventRecurrence.getRecurrenceOption() != null)
            assertEqualRecurrenceOptions(actualEventRecurrence.getRecurrenceOption(), expectedEventRecurrence.getRecurrenceOption());

        assertEquals(actualEventRecurrence.getRepeatInterval(), expectedEventRecurrence.getRepeatInterval());
        assertNotNull(actualEventRecurrence.getRepeatEndDate());
    }

    public static void assertEqualInvitations(Invitation expectedInvitation, Invitation actualInvitation) {
        TestCase.assertEquals(actualInvitation.getId(), expectedInvitation.getId());
        TestCase.assertEquals(actualInvitation.getEventId(), expectedInvitation.getEventId());
        TestCase.assertEquals(actualInvitation.getUser().getId(), expectedInvitation.getUser().getId());
        TestCase.assertEquals(actualInvitation.getUserRole(), expectedInvitation.getUserRole());
        TestCase.assertEquals(actualInvitation.getUserResponse(), expectedInvitation.getUserResponse());
        TestCase.assertEquals(actualInvitation.getAttendeesCount(), expectedInvitation.getAttendeesCount());
        TestCase.assertEquals(actualInvitation.isParticipated(), expectedInvitation.isParticipated());
    }

    public static void assertEqualEvents(Event actualEvent, Event expectedEvent) {
        assertEquals(actualEvent.getId(), expectedEvent.getId());
        assertEquals(actualEvent.getTitle(), expectedEvent.getTitle());
        assertEquals(actualEvent.getCategory().getId(), expectedEvent.getCategory().getId());
        assertEquals(actualEvent.getShortDescription(), expectedEvent.getShortDescription());
        assertEquals(actualEvent.getFullDescription(), expectedEvent.getFullDescription());
        assertEquals(actualEvent.getLocation(), expectedEvent.getLocation());
        assertEquals(actualEvent.getLat(), expectedEvent.getLat(), 0);
        assertEquals(actualEvent.getLng(), expectedEvent.getLng(), 0);
        assertEquals(actualEvent.getFilePath(), expectedEvent.getFilePath());
        assertEquals(actualEvent.getImagePath(), expectedEvent.getImagePath());
        assertNotNull(actualEvent.getCreationDate());
        //assertEquals(actualEvent.getLastModifiedDate(), expectedEvent.getLastModifiedDate());
        assertEquals(actualEvent.isPublicAccessed(), expectedEvent.isPublicAccessed());
        assertEquals(actualEvent.isGuestsAllowed(), expectedEvent.isGuestsAllowed());
    }
}
