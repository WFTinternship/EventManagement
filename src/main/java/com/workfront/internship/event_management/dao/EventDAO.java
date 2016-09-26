package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
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
    Event getEventById(int eventId) throws DAOException, ObjectNotFoundException;

    List<Event> getUserOrganizedEvents(int userId) throws DAOException;

    List<Event> getUserParticipatedEvents(int userId) throws DAOException;

    List<Event> getUserEventsByResponse(int userId, String userResponse) throws DAOException;

    List<Event> getAllEvents() throws DAOException;

    List<Event> getPublicEvents() throws DAOException;

    List<Event> getAllUpcomingEvents() throws DAOException;

    List<Event> getPublicUpcomingEvents() throws DAOException;

    List<Event> getAllPastEvents() throws DAOException;

    List<Event> getPublicPastEvents() throws DAOException;

    List<Event> getPublicEventsByCategory(int categoryId);

    List<Event> getAllEventsByCategory(int categoryId);

    List<Event> getEventsByKeyword(String keyword);

    //update record in db
    boolean updateEvent(Event event) throws DAOException, ObjectNotFoundException;

    //delete record from db
    boolean deleteEvent(int eventId) throws DAOException, ObjectNotFoundException;

    void deleteAllEvents() throws DAOException;


}