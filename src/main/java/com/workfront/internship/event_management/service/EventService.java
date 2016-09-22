package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Event;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
public interface EventService {

    //Create
    Event createEvent(Event event);

    Event createEmptyEvent();

    //Read
    Event getEventById(int eventId);

    Event getFullEventById(int eventId);

    List<Event> getEventsByCategory(int categoryId);

    List<Event> getUserOrganizedEvents(int userId);

    List<Event> getUserParticipatedEvents(int userId);

    List<Event> getUserEventsByResponse(int userId, String userResponse);

    List<Event> getAllEvents();

    List<Event> getPublicEvents();

    List<Event> getPublicUpcomingEvents();

    List<Event> getPublicPastEvents();

    List<Event> getAllUpcomingEvents();

    List<Event> getAllPastEvents();

    //Update
    boolean editEvent(Event event);

    //Delete
    boolean deleteEvent(int eventId);

    void deleteAllEvents();


}
