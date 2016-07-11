package com.workfront.internship.event_management.model;

import com.workfront.internship.event_management.model.datehelpers.RecurrenceType;

import java.util.Date;


/**
 * Created by hermine on 7/10/16.
 */
public class EventRecurrence {

    private RecurrenceType recurrenceType;
    private int eventId;
    private int repeatInterval;
    private String repeatOn;
    private Date repeatEndDate;

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

    public String getRepeatOn() {
        return repeatOn;
    }

    public EventRecurrence setRepeatOn(String repeatOn) {
        this.repeatOn = repeatOn;
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
