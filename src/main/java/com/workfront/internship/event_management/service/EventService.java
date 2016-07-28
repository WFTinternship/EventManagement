package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Event;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
interface EventService {

    //CRUD operations with event
    int createEvent(Event event);

    Event getEventById(int eventId);

    List<Event> getEventsByCategory(int categoryId);

    List<Event> getUserOrganizedEvents(int userId);

    List<Event> getUserParticipatedEvents(int userId);

    List<Event> getUserEventsByResponse(int userId, String userResponse);

    List<Event> getAllEvents();

    void editEvent(Event event);

    void deleteEvent(int eventId);

    void deleteAllEvents();





}
