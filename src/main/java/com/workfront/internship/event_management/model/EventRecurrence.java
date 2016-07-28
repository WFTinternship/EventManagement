package com.workfront.internship.event_management.model;

import java.util.Date;

/**
 * Created by Hermine Turshujyan 7/1/16.
 */
public class EventRecurrence {
    private int id;
    private RecurrenceType recurrenceType;
    private int eventId;
    private int repeatInterval;
    private RecurrenceOption recurrenceOption;
    private Date repeatEndDate;

    public int getId() {
        return id;
    }

    public EventRecurrence setId(int id) {
        this.id = id;
        return this;
    }

    public RecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    public EventRecurrence setRecurrenceType(RecurrenceType recurrenceType) {
        this.recurrenceType = recurrenceType;
        return this;
    }

    public int getEventId() {
        return eventId;
    }

    public EventRecurrence setEventId(int eventId) {
        this.eventId = eventId;
        return this;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public EventRecurrence setRepeatInterval(int repeatInterval) {
        this.repeatInterval = repeatInterval;
        return this;
    }

    public RecurrenceOption getRecurrenceOption() {
        return recurrenceOption;
    }

    public EventRecurrence setRecurrenceOptionId(RecurrenceOption recurrenceOption) {
        this.recurrenceOption = recurrenceOption;
        return this;
    }

    public Date getRepeatEndDate() {
        return repeatEndDate;
    }

    public EventRecurrence setRepeatEndDate(Date repeatEndDate) {
        this.repeatEndDate = repeatEndDate;
        return this;
    }


}
