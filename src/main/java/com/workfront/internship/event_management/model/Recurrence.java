package com.workfront.internship.event_management.model;

import java.util.Date;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class Recurrence {

    public Recurrence(Recurrence recurrence) {
        this.id = recurrence.id;
        this.recurrenceType = recurrence.recurrenceType;
        this.eventId = recurrence.eventId;
        this.repeatInterval = recurrence.repeatInterval;
        this.recurrenceOption = recurrence.recurrenceOption;
        this.repeatEndDate = recurrence.repeatEndDate;
    }

    public Recurrence() {
    }

    private int id;
    private RecurrenceType recurrenceType;
    private int eventId;
    private int repeatInterval;
    private RecurrenceOption recurrenceOption;
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

    public RecurrenceOption getRecurrenceOption() {
        return recurrenceOption;
    }

    public Recurrence setRecurrenceOption(RecurrenceOption recurrenceOption) {
        this.recurrenceOption = recurrenceOption;
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
