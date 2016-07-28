package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.EventRecurrence;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
public interface EventRecurrenceDAO {

    //insert data into db
    int addEventRecurrence(EventRecurrence recurrence) throws DAOException;

    void addEventRecurrences(List<EventRecurrence> recurrenceList);

    //read data from db
    EventRecurrence getEventRecurrenceById(int id) throws ObjectNotFoundException, DAOException;

    List<EventRecurrence> getEventRecurrencesByEventId(int eventId) throws DAOException;

    List<EventRecurrence> getAllEventRecurrences() throws DAOException;

    //update db record
    void updateEventRecurrence(EventRecurrence recurrence);

    //delete data from db
    void deleteEventRecurrence(int id) throws ObjectNotFoundException, DAOException;

    void deleteAllEventRecurrences() throws DAOException;
}
