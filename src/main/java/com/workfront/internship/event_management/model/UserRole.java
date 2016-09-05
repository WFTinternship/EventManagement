package com.workfront.internship.event_management.model;

/**
 * Created by Hermine Turshujyan 9/5/16.
 */
public enum UserRole {
    ORGANIZER, MEMBER;

    public static UserRole findByName(String name) {
        UserRole[] roles = values();
        for (UserRole userRole : roles) {
            if (userRole.name().equals(name)) {
                return userRole;
            }
        }
        return null;
    }
}
