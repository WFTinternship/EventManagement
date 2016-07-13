package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventRecurrence;

import java.sql.Connection;
import java.util.List;

/**
 * Created by hermine on 7/11/16.
 */
public interface EventRecurrenceDAO {

     //CREATE
     boolean insertEventRecurrences(List<EventRecurrence> recurrences, Connection conn);
     boolean insertEventRecurrences(List<EventRecurrence> recurrences);
     int insertEventRecurrence(EventRecurrence recurrence, Connection conn);
     int insertEventRecurrence(EventRecurrence recurrence);

     //READ
     List<EventRecurrence> getEventRecurrencesByEventId(int eventId);

     //UPDATE
     boolean updateEventRecurrence(EventRecurrence recurrence);

     //DELETE
     boolean deleteEventRecurrece(int id);
}
