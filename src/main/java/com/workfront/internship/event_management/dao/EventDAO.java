package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.User;

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

    List<Event> getEventsByCategory(int categoryId) throws DAOException;

    User getEventOrganizer(int eventId) throws DAOException, ObjectNotFoundException;

    List<Event> getUserOrganizedEvents(int userId) throws DAOException;

    List<Event> getUserParticipatedEvents(int userId) throws DAOException;

    List<Event> getUserEventsByResponse(int userId, String userResponse) throws DAOException;

    List<Event> getAllEvents() throws DAOException;

    List<Event> getUpcomingEvents() throws DAOException;

    List<Event> getPastEvents() throws DAOException;



    //update record in db
    boolean updateEvent(Event event) throws DAOException, ObjectNotFoundException;

    //delete record from db
    boolean deleteEvent(int eventId) throws DAOException, ObjectNotFoundException;

    void deleteAllEvents() throws DAOException;

}