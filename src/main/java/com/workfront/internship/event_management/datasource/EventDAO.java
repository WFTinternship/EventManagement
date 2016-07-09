package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventInvitation;
import com.workfront.internship.event_management.model.EventMedia;

import java.util.List;

/**
 * Created by hermine on 7/2/16.
 */
public interface EventDAO {

    //CREATE
    public boolean createEvent(Event event, int creatorId);

    //READ
    public List<Event> getAllEvents();

    public Event getEventById(int eventId);

    public List<Event> getEventsByCategory(int categoryId);

    public List<Event> getEventsByOrganizerId(int organizerId);

    public List<Event> getInvitedEventsByUserId(int userId);

    public List<Event> getParticipatedEventsByUserId(int partId);

    public List<Event> getAcceptedEventsByUserId(int partId);

    //UPDATE
    public boolean updateEvent(Event event); //- optional lists

    //DELETE

    public boolean deleteEvent(int eventId);
}