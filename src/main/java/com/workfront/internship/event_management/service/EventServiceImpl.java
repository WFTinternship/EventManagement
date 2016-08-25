package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.EventDAO;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Event;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.*;

/**
 * Created by Hermine Turshujyan 7/20/16.
 */

@Component
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = Logger.getLogger(EventServiceImpl.class);

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private InvitationService invitationService;


    @Override
    public Event createEvent(Event event) throws DuplicateEntryException {
        //check if event object is valid
        if (!isValidEvent(event)) {
            throw new OperationFailedException("Invalid event");
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
        return eventDAO.getEventById(eventId);
    }

    @Override
    public void editEvent(Event event) throws OperationFailedException {
        if (!isValidEvent(event)) {
            throw new InvalidObjectException("Invalid event");
        }

        try {
            //update event main info
            eventDAO.updateEvent(event);

            //update event recurrences
            RecurrenceService recurrenceService = new RecurrenceServiceImpl();
            recurrenceService.editRecurrenceList(event.getId(), event.getEventRecurrences());

            //update event invitations
            InvitationService invitationService = new InvitationServiceImpl();
            invitationService.editInvitationList(event.getId(), event.getInvitations());

        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Event not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Event> getEventsByCategory(int categoryId) throws OperationFailedException {
        if (categoryId < 1) {
            throw new InvalidObjectException("Invalid category id");
        }

        try {
            return eventDAO.getEventsByCategory(categoryId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Event> getUserOrganizedEvents(int userId) throws OperationFailedException {
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }

        try {
            return eventDAO.getUserOrganizedEvents(userId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Event> getUserParticipatedEvents(int userId) throws OperationFailedException {
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }

        try {
            return eventDAO.getUserParticipatedEvents(userId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Event> getUserEventsByResponse(int userId, String userResponse) throws OperationFailedException {
        if (userId < 1) {
            throw new OperationFailedException("Invalid user id");
        }

        if (isEmptyString(userResponse)) {
            throw new OperationFailedException("Invalid user response");
        }

        try {
            return eventDAO.getUserEventsByResponse(userId, userResponse);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Event> getAllEvents() {
        try {
            return eventDAO.getAllEvents();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteEvent(int eventId) {
        if (eventId < 1) {
            throw new OperationFailedException("Invalid event id");
        }

        try {
            eventDAO.deleteEvent(eventId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Event not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllEvents() {
        try {
            eventDAO.deleteAllEvents();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }
}
