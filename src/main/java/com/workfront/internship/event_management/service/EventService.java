package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.model.Event;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
public interface EventService {

    //Create
    Event createEvent(Event event) throws DuplicateEntryException;

    //Read
    Event getEventById(int eventId);

    List<Event> getEventsByCategory(int categoryId);

    List<Event> getUserOrganizedEvents(int userId);

    List<Event> getUserParticipatedEvents(int userId);

    List<Event> getUserEventsByResponse(int userId, String userResponse);

    List<Event> getAllEvents();

    //Update
    boolean editEvent(Event event);

    //Delete
    boolean deleteEvent(int eventId);

    void deleteAllEvents();


}
