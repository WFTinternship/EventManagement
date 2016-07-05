package com.workfront.internship.event_management.model;

public class Participant extends User {

    private int participantsCount;
    private String response;
    private String role;
    private boolean checkedIn;

    public int getParticipantsCount() {
        return participantsCount;
    }

    public Participant setParticipantsCount(int participantsCount) {
        this.participantsCount = participantsCount;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public Participant setResponse(String response) {
        this.response = response;
        return this;
    }

    public boolean isCheckedIn() {
        return checkedIn;

    }

    public Participant setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
        return this;
    }

    public String getRole() {
        return role;
    }

    public Participant setRole(String role) {
        this.role = role;
        return this;
    }
}
