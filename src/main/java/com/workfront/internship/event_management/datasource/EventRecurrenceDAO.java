package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventRecurrence;

import java.sql.Connection;
import java.util.List;

/**
 * Created by hermine on 7/11/16.
 */
public interface EventRecurrenceDAO {

     //CREATE
     boolean addEventRecurrences(List<EventRecurrence> recurrences, Connection conn);

     boolean addEventRecurrences(List<EventRecurrence> recurrences);

     int addEventRecurrence(EventRecurrence recurrence, Connection conn);

     int addEventRecurrence(EventRecurrence recurrence);

     //READ
     List<EventRecurrence> getEventRecurrencesByEventId(int eventId);

     //UPDATE
     boolean updateEventRecurrence(EventRecurrence recurrence);

     //DELETE
     boolean deleteEventRecurrece(int id);
}
