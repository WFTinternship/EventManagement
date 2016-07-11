package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.Event;

import java.util.List;

/**
 * Created by hermine on 7/2/16.
 */
public interface EventDAO {

    //CREATE
    boolean insertEvent(Event event, int organizerId);

    //READ
    List<Event> getAllEvents(); //only main info

    Event getEventById(int eventId);

    List<Event> getEventsByCategory(int categoryId);

    List<Event> getEventsByUserId(String userRole, int userId);

     List<Event> getParticipatedEventsByUserId(int userId);

     List<Event> getAcceptedEventsByUserId(int userId);

    //UPDATE
     boolean updateEvent(Event event); //- ???

    //DELETE

     boolean deleteEvent(int eventId);
}