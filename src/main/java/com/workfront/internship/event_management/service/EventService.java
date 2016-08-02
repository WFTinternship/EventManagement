package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.model.Event;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
interface EventService {

    //Create
    Event createEvent(Event event) throws DuplicateEntryException;

    //Read
    Event getEvent(int eventId);

    List<Event> getEventsByCategory(int categoryId);

    List<Event> getUserOrganizedEvents(int userId);

    List<Event> getUserParticipatedEvents(int userId);

    List<Event> getUserEventsByResponse(int userId, String userResponse);

    List<Event> getAllEvents();

    //Update
    void editEvent(Event event);

    //Delete
    void deleteEvent(int eventId);

    void deleteAllEvents();


}
