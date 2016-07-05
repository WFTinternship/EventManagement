package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventMedia;
import com.workfront.internship.event_management.model.EventParticipant;

import java.util.List;
import java.util.Map;

/**
 * Created by hermine on 7/2/16.
 */
public interface EventDAO {
    public Map<Integer, Event> getAllEvents();

    public List<Event> getEventsByCategory(int categoryId);

    public Event getEventById(int eventId);

    public void updateEvent(Event event);

    public void insertEvent(Event event);

    public void deleteEvent(int eventId);

    public List<EventMedia> getMediaByEventId(int eventId);

    public List<EventParticipant> getParticipantsByEventId(int eventId);

}