package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.DateRange;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventMedia;
import com.workfront.internship.event_management.model.EventParticipant;

import java.util.List;
import java.util.Map;

/**
 * Created by hermine on 7/2/16.
 */
public interface EventDAO {
    public List<Event> getAllEvents(); //+
    public List<Event> getEventsByCategory(int categoryId); //+
    public List<Event> getEventsByOrganizerId(int organizerId); //-
    public List<Event> getEventsByDateRange(DateRange range); //-

    public Event getEventById(int eventId); // +

    public boolean updateEvent(Event event); //+
    public boolean updateParticipantsList(); //??????

    public boolean insertEvent(Event event); //+

    public boolean deleteEvent(int eventId);//+

    public List<EventMedia> getMediaByEventId(int eventId); //+

    public List<EventParticipant> getParticipantsByEventId(int eventId); //+
}