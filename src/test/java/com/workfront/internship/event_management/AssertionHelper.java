package com.workfront.internship.event_management;

import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.User;

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
}
