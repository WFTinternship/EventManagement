package com.workfront.internship.event_management.model;

public class EventParticipant extends User {

    private int participantsCount;
    private String response;
    private String role;
    private boolean checkedIn;

    public int getParticipantsCount() {
        return participantsCount;
    }

    public EventParticipant setParticipantsCount(int participantsCount) {
        this.participantsCount = participantsCount;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public EventParticipant setResponse(String response) {
        this.response = response;
        return this;
    }

    public boolean isCheckedIn() {
        return checkedIn;

    }

    public EventParticipant setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
        return this;
    }

    public String getRole() {
        return role;
    }

    public EventParticipant setRole(String role) {
        this.role = role;
        return this;
    }
}
