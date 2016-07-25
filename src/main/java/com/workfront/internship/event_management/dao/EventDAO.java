package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.Event;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/2/16.
 */
public interface EventDAO {

    //insert into db
    int addEvent(Event event) throws DAOException;

    int addEventWithRecurrences(Event event) throws DAOException;

    //read data from db
    List<Event> getAllEvents() throws DAOException;

    Event getEventById(int eventId) throws DAOException;

    List<Event> getEventsByCategory(int categoryId) throws DAOException;

    List<Event> getEventsByUserId(String userRole, int userId) throws DAOException;

    List<Event> getParticipatedEventsByUserId(int userId) throws DAOException;

    List<Event> getAcceptedEventsByUserId(int userId) throws DAOException;

    //update record in db
    boolean updateEvent(Event event) throws DAOException;

    //delete record from db
    boolean deleteEvent(int eventId) throws DAOException;

    boolean deleteAllEvents() throws DAOException;

}