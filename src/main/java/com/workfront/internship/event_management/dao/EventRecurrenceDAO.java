package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.EventRecurrence;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
public interface EventRecurrenceDAO {

    //insert data into db
    int addEventRecurrence(EventRecurrence recurrence);

    //read data from db
    EventRecurrence getEventRecurrenceById(int id);
    List<EventRecurrence> getEventRecurrencesByEventId(int eventId);
    List<EventRecurrence> getAllEventRecurrences();

    //update db record
    boolean updateEventRecurrence(EventRecurrence recurrence);

    //delete data from db
    boolean deleteEventRecurrence(int id) throws DAOException;

    boolean deleteAllEventRecurrences() throws DAOException;
}
