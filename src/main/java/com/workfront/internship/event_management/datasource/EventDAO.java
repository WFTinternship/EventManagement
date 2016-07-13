package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.Event;

import java.util.List;

/**
 * Created by hermine on 7/2/16.
 */
public interface EventDAO {

    //Create
    boolean insertEvent(Event event, int organizerId);

    //Read
    List<Event> getAllEvents();
    Event getEventById(int eventId);
    List<Event> getEventsByCategory(int categoryId);
    List<Event> getEventsByUserId(String userRole, int userId);
    List<Event> getParticipatedEventsByUserId(int userId);
    List<Event> getAcceptedEventsByUserId(int userId);

    //Update
    boolean updateEvent(Event event);

    //Delete
    boolean deleteEvent(int eventId);
}