package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.EventDAO;
import com.workfront.internship.event_management.dao.EventDAOImpl;
import com.workfront.internship.event_management.dao.InvitationDAO;
import com.workfront.internship.event_management.dao.InvitationDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Event;
import org.apache.log4j.Logger;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection;
import static com.workfront.internship.event_management.service.util.Validator.isValidEvent;

/**
 * Created by Hermine Turshujyan 7/20/16.
 */
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = Logger.getLogger(EventServiceImpl.class);

    private EventDAO eventDAO;

    public EventServiceImpl() throws OperationFailedException {
        try {
            eventDAO = new EventDAOImpl();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public Event createEvent(Event event) {
        //check if event object is valid
        if (isValidEvent(event)) {
            throw new OperationFailedException("Invalid event");
        }

        try {
            int eventId;

            if (isEmptyCollection(event.getEventRecurrences())) {
                eventId = eventDAO.addEvent(event);
            } else {
                eventId = eventDAO.addEventWithRecurrences(event);
            }

            //set generated id to event
            event.setId(eventId);

            //if event info is successfully inserted, insert also invitations
            if (eventId != 0 && !isEmptyCollection(event.getInvitations())) {
                InvitationDAO invitationDAO = new InvitationDAOImpl();
                invitationDAO.addInvitations(event.getInvitations());
            }
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
        return event;
    }

    @Override
    public Event getEventById(int eventId) {
        if (eventId < 1) {
            throw new OperationFailedException("Invalid event id");
        }

        try {
            //get category from db
            return eventDAO.getEventById(eventId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Category not found", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    ////////--------
    @Override
    public void editEvent(Event event) throws OperationFailedException {

        boolean success;

        if (isValidEvent(event)) {
            try {
                try {
                    eventDAO.updateEvent(event);
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                }

                EventRecurrenceService recurrenceService = new EventRecurrenceServiceImpl();
                InvitationService invitationService = new InvitationServiceImpl();
                MediaService mediaService = new MediaServiceImpl();
                // TODO: 7/26/16 add update list methods


            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        } else {
            throw new OperationFailedException("Invalid event.");
        }
    }

    @Override
    public void deleteEvent(int eventId) throws OperationFailedException {

        boolean success;

        if (eventId > 0) {
            try {
                eventDAO.deleteEvent(eventId);
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            throw new OperationFailedException("Invalid event id.");
        }
    }

    @Override
    public List<Event> getEventsByCategory(int categoryId) throws OperationFailedException {

        List<Event> eventList;

        if (categoryId > 0) {
            try {
                eventList = eventDAO.getEventsByCategory(categoryId);
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        } else {
            throw new OperationFailedException("Invalid category id.");
        }
        return eventList;
    }

    @Override
    public List<Event> getUserOrganizedEvents(int userId) throws OperationFailedException {

        List<Event> eventList;

        if (userId > 0) {
            try {
                eventList = eventDAO.getUserOrganizedEvents(userId);
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        } else {
            throw new OperationFailedException("Invalid user id.");
        }
        return eventList;
    }

    @Override
    public List<Event> getUserParticipatedEvents(int userId) throws OperationFailedException {

        List<Event> eventList;

        if (userId > 0) {
            try {
                eventList = eventDAO.getUserParticipatedEvents(userId);
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");

            }
        } else {
            throw new OperationFailedException("Invalid user id.");
        }
        return eventList;
    }

    @Override
    public List<Event> getUserEventsByResponse(int userId, String userResponse) throws OperationFailedException {

        List<Event> eventList;

        if (userId > 0) {
            try {
                eventList = eventDAO.getUserEventsByResponse(userId, userResponse);
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        } else {
            throw new OperationFailedException("Invalid user id.");
        }
        return eventList;
    }

    @Override
    public List<Event> getAllEvents() throws OperationFailedException {

        List<Event> eventList;
        try {
            eventList = eventDAO.getAllEvents();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
        return eventList;
    }

    @Override
    public void deleteAllEvents() throws OperationFailedException {

        boolean success;
        try {
            eventDAO.deleteAllEvents();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
    }

    //helper methods


}
