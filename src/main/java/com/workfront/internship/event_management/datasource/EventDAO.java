package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventInvitation;
import com.workfront.internship.event_management.model.EventMedia;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by hermine on 7/2/16.
 */
public interface EventDAO {

    //CREATE
    public boolean insertInvitation(EventInvitation invitation);

    public boolean insertInvitationsList(List<EventInvitation> invitations);

    public boolean createEvent(Event event, int creatorId); //- optional lists + in

    public boolean updateEvent(Event event); //- optional lists

    //READ
    public List<Event> getAllEvents(); //- organizer info

    public Event getEventById(int eventId); // - organizer info

    public List<Event> getEventsByCategory(int categoryId); //- organizer info

    public List<Event> getEventsByOrganizerId(int organizerId); //- organizer info

    public List<Event> getInvitedEventsByUserId(int userId); //- organizer info

    public List<Event> getParticipatedEventsByUserId(int partId); //- organizer info

    public List<Event> getAcceptedEventsByUserId(int partId); //- organizer info

    public Event getEventFullInfoById(int eventId); // - multiple queries

    public void getEventAdditionalInfo(Event event); // - separate methods???

    public List<EventMedia> getMediaByEventId(int eventId);

    public List<EventInvitation> getInvitationsByEventId(int eventId);

    //UPDATE
    public boolean updateInvitation(EventInvitation invitation);

    //DELETE
    public boolean deleteInvitation(int eventId, int userId);

    public boolean deleteEvent(int eventId);
}