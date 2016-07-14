package com.workfront.internship.event_management.model;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class EventInvitation{

    private int id;
    private int eventId;
    private User user;
    private String userRole;
    private String userResponse;
    private int attendeesCount;
    private boolean participated;

    public int getId() {
        return id;
    }

    public EventInvitation setId(int id) {
        this.id = id;
        return this;
    }

    public int getEventId() {
        return eventId;
    }

    public EventInvitation setEventId(int eventId) {
        this.eventId = eventId;
        return this;
    }

    public int getAttendeesCount() {
        return attendeesCount;
    }

    public EventInvitation setAttendeesCount(int attendeesCount) {
        this.attendeesCount = attendeesCount;
        return this;
    }

    public String getUserResponse() {
        return userResponse;
    }

    public EventInvitation setUserResponse(String userResponse) {
        this.userResponse = userResponse;
        return this;
    }

    public boolean isParticipated() {
        return participated;

    }

    public EventInvitation setParticipated(boolean participated) {
        this.participated = participated;
        return this;
    }

    public String getUserRole() {
        return userRole;
    }

    public EventInvitation setUserRole(String userRole) {
        this.userRole = userRole;
        return this;
    }


    public User getUser() {
        return user;
    }

    public EventInvitation setUser(User user) {
        this.user = user;
        return this;
    }
}
