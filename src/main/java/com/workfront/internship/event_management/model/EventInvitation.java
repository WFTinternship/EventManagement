package com.workfront.internship.event_management.model;

public class EventInvitation{

    private User user;
    private int eventId;
    private String userRole;
    private String userResponse;
    private int attendeesCount;
    private boolean realParticipation;

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

    public boolean isRealParticipation() {
        return realParticipation;

    }

    public EventInvitation setRealParticipation(boolean realParticipation) {
        this.realParticipation = realParticipation;
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
