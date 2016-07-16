package com.workfront.internship.event_management.model;

import java.util.Date;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class Recurrence {
    private int id;
    private RecurrenceType recurrenceType;
    private int eventId;
    private int repeatInterval;
    private String repeatOn;
    private Date repeatEndDate;

    public int getId() {
        return id;
    }

    public Recurrence setId(int id) {
        this.id = id;
        return this;
    }

    public RecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    public Recurrence setRecurrenceType(RecurrenceType recurrenceType) {
        this.recurrenceType = recurrenceType;
        return this;
    }

    public int getEventId() {
        return eventId;
    }

    public Recurrence setEventId(int eventId) {
        this.eventId = eventId;
        return this;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public Recurrence setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
        return this;
    }

    public String getRepeatOn() {
        return repeatOn;
    }

    public Recurrence setRepeatOn(String repeatOn) {
        this.repeatOn = repeatOn;
        return this;
    }

    public Date getRepeatEndDate() {
        return repeatEndDate;
    }

    public Recurrence setRepeatEndDate(Date repeatEndDate) {
        this.repeatEndDate = repeatEndDate;
        return this;
    }


}
