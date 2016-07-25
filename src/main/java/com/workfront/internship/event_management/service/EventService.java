package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.OperationFailedException;
import com.workfront.internship.event_management.model.Event;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
interface EventService {

    //CRUD operations with event
    boolean addEvent(Event event) throws OperationFailedException; //+

    Event getEventById(int eventId);

    boolean updateEvent(Event event);

    boolean deleteEvent(int eventId);

    // get event list
    List<Event> getEventsByCategory(int categoryId);

    List<Event> getEventsByUserId(String userRole, int userId);

    List<Event> getParticipatedEventsByUserId(int userId);

    List<Event> getAcceptedEventsByUserId(int userId);

    //operations with all events
    List<Event> getAllEvents();

    boolean deleteAllEvents();





}
