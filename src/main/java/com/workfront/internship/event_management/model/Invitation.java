package com.workfront.internship.event_management.model;

import java.util.Date;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class Invitation {

    private int id;
    private int eventId;
    private User user;
    private UserResponse userResponse;
    private int attendeesCount;
    private boolean participated;
    private Date creationDate;

    public int getId() {
        return id;
    }

    public Invitation setId(int id) {
        this.id = id;
        return this;
    }

    public Invitation(Invitation invitation) {
        this.id = invitation.id;
        this.eventId = invitation.eventId;
        this.user = invitation.user;
        this.userResponse = invitation.userResponse;
        this.attendeesCount = invitation.attendeesCount;
        this.participated = invitation.participated;
    }

    public Invitation() {
    }

    public int getEventId() {
        return eventId;
    }

    public Invitation setEventId(int eventId) {
        this.eventId = eventId;
        return this;
    }

    public int getAttendeesCount() {
        return attendeesCount;
    }

    public Invitation setAttendeesCount(int attendeesCount) {
        this.attendeesCount = attendeesCount;
        return this;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public Invitation setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
        return this;
    }

    public boolean isParticipated() {
        return participated;

    }

    public Invitation setParticipated(boolean participated) {
        this.participated = participated;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Invitation setUser(User user) {
        this.user = user;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Invitation setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

}
