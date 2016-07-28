package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.*;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Event;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/20/16.
 */
public class EventServiceImpl implements EventService {

    private EventDAO eventDAO;

    public EventServiceImpl() throws OperationFailedException {
        try {
            eventDAO = new EventDAOImpl();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
    }

    @Override
    public int createEvent(Event event) throws OperationFailedException {

        int eventId = 0;

        //check if event object is valid
        if (isValidEvent(event)) {

            try {
                if (event.getEventRecurrences() == null || event.getEventRecurrences().isEmpty()) {
                    eventId = eventDAO.addEvent(event);
                } else {
                    eventId = eventDAO.addEventWithRecurrences(event);
                }

                //if event info is successfully inserted, insert also invitations
                if (eventId != 0 && event.getInvitations() != null && !event.getInvitations().isEmpty()) {
                    InvitationDAO invitationDAO = new InvitationDAOImpl();
                    invitationDAO.addInvitations(event.getInvitations());
                }
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        } else {
            throw new OperationFailedException("Invalid event!");
        }
        return eventId;
    }

    @Override
    public Event getEventById(int eventId) throws OperationFailedException {

        Event event;

        if (eventId > 0) {
            try {
                event = eventDAO.getEventById(eventId);
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        } else {
            throw new OperationFailedException("Invalid event id.");
        }
        return event;
    }

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

    private boolean isValidEvent(Event event) {

        boolean valid = false;
        if (event != null) {
            if (event.getTitle() != null
                    && event.getCategory() != null && event.getCategory().getId() > 0
                    && event.getStartDate() != null
                    && event.getEndDate() != null
                    && event.getCreationDate() != null) {
                valid = true;
            }
        }
        return false;
    }
}
