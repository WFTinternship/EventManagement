package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.EventDAO;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Event;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.*;

/**
 * Created by Hermine Turshujyan 7/20/16.
 */

@Component
public class EventServiceImpl implements EventService {

    private static final Logger logger = Logger.getLogger(EventServiceImpl.class);

    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private InvitationService invitationService;

    @Override
    public Event createEvent(Event event) {
        //set creation date
        setDefaultFields(event);

        //check if event object is valid
        if (!isValidEvent(event)) {
            throw new InvalidObjectException("Invalid event");
        }

        int eventId;

        if (isEmptyCollection(event.getEventRecurrences())) {
            eventId = eventDAO.addEvent(event);
        } else {
            eventId = eventDAO.addEventWithRecurrences(event);
        }

        //set generated id to event
        event.setId(eventId);

        //if event info is successfully inserted into db, insert also invitations
        if (eventId != 0 && !isEmptyCollection(event.getInvitations())) {
            invitationService.addInvitations(event.getInvitations());
        }

        return event;
    }

    @Override
    public Event getEventById(int eventId) {
        if (eventId < 1) {
            throw new InvalidObjectException("Invalid event id");
        }

        //get event from db
        Event event = eventDAO.getEventById(eventId);
        if (event == null) {
            throw new ObjectNotFoundException("Event not found");
        }
        return event;
    }

    @Override
    public boolean editEvent(Event event) throws OperationFailedException {
        if (!isValidEvent(event)) {
            throw new InvalidObjectException("Invalid event");
        }
        //add modification date
        event.setLastModifiedDate(new Date());

        //update event main info
        boolean success = eventDAO.updateEvent(event);
        if (success) {
            //update event recurrences
            recurrenceService.editRecurrenceList(event.getId(), event.getEventRecurrences());

            //update event invitations
            invitationService.editInvitationList(event.getId(), event.getInvitations());
        } else {
            throw new ObjectNotFoundException("Event not found");
        }

        return success;
    }

    @Override
    public List<Event> getEventsByCategory(int categoryId) throws OperationFailedException {
        if (categoryId < 1) {
            throw new InvalidObjectException("Invalid category id");
        }

        return eventDAO.getEventsByCategory(categoryId);
    }

    @Override
    public List<Event> getUserOrganizedEvents(int userId) throws OperationFailedException {
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }

        return eventDAO.getUserOrganizedEvents(userId);
    }

    @Override
    public List<Event> getUserParticipatedEvents(int userId) throws OperationFailedException {
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }

        return eventDAO.getUserParticipatedEvents(userId);
    }

    @Override
    public List<Event> getUserEventsByResponse(int userId, String userResponse) throws OperationFailedException {
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }

        if (isEmptyString(userResponse)) {
            throw new InvalidObjectException("Invalid user response");
        }

        return eventDAO.getUserEventsByResponse(userId, userResponse);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventDAO.getAllEvents();
    }

    @Override
    public boolean deleteEvent(int eventId) {
        if (eventId < 1) {
            throw new InvalidObjectException("Invalid event id");
        }

        return eventDAO.deleteEvent(eventId);
    }

    @Override
    public void deleteAllEvents() {
        eventDAO.deleteAllEvents();
    }

    //helper methods
    private void setDefaultFields(Event event) {
        event.setCreationDate(new Date());
    }
}
