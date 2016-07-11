package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventRecurrence;

import java.util.List;

/**
 * Created by hermine on 7/11/16.
 */
public interface EventRecurrenceDAO {

    public boolean insertEventRecurrence(EventRecurrence recurrence);
    public List<EventRecurrence> getEventRecurrencesByEventId(int eventId);
    public boolean updateEventRecurrence(EventRecurrence recurrence);
    public boolean deleteEventRecurrece(int id);
}
