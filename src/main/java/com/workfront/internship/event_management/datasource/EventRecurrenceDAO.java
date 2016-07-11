package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventRecurrence;

import java.sql.Connection;
import java.util.List;

/**
 * Created by hermine on 7/11/16.
 */
public interface EventRecurrenceDAO {

     boolean insertEventRecurrences(List<EventRecurrence> recurrences, Connection conn);
     boolean insertEventRecurrences(List<EventRecurrence> recurrences);
     boolean insertEventRecurrence(EventRecurrence recurrence, Connection conn);
     boolean insertEventRecurrence(EventRecurrence recurrence);
     List<EventRecurrence> getEventRecurrencesByEventId(int eventId);
     boolean updateEventRecurrence(EventRecurrence recurrence); //--------
     boolean deleteEventRecurrece(int id);
}
