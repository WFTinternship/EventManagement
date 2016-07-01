package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.Event;

import java.util.List;

/**
 * Created by hermine on 7/2/16.
 */
public interface EventDAO {
    public List<Event> getAllEvents();
    public List<Event> getEventsByCategory(int categoryId);
    public Event getEventById(int eventId);
    public void updateEvent(Event event);
    public void insertEvent(Event event);
    public void deleteEvent(int eventId);
}
