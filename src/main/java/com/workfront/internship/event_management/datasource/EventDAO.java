package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.Event;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/2/16.
 */
public interface EventDAO {

    //insert into db
    int addEvent(Event event);

    //read data from db
    List<Event> getAllEvents();
    Event getEventById(int eventId);
    List<Event> getEventsByCategory(int categoryId);
    List<Event> getEventsByUserId(String userRole, int userId);
    List<Event> getParticipatedEventsByUserId(int userId);
    List<Event> getAcceptedEventsByUserId(int userId);

    //update record in db
    boolean updateEvent(Event event);

    //delete record from db
    boolean deleteEvent(int eventId);

    boolean deleteAllEvents();

}