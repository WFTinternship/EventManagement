package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.OperationFailedException;
import com.workfront.internship.event_management.model.Event;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
interface EventService {

    //CRUD operations with event
    int createEvent(Event event) throws OperationFailedException;

    Event getEventById(int eventId) throws OperationFailedException;

    boolean editEvent(Event event) throws OperationFailedException;

    boolean deleteEvent(int eventId) throws OperationFailedException;

    // get event list
    List<Event> getEventsByCategory(int categoryId) throws OperationFailedException;

    List<Event> getUserOrganizedEvents(int userId) throws OperationFailedException;

    List<Event> getUserParticipatedEvents(int userId) throws OperationFailedException;

    List<Event> getUserEventsByResponse(int userId, String userResponse) throws OperationFailedException;

    //operations with all events
    List<Event> getAllEvents() throws OperationFailedException;

    boolean deleteAllEvents() throws OperationFailedException;





}
