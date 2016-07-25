package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.EventDAO;
import com.workfront.internship.event_management.dao.EventDAOImpl;
import com.workfront.internship.event_management.dao.InvitationDAO;
import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.exception.OperationFailedException;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventRecurrence;
import com.workfront.internship.event_management.model.Invitation;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/20/16.
 */
public class EventServiceImpl implements EventService {

    private EventDAO eventDAO;
    private InvitationDAO invitationDAO;
    private EventRecurrence recurrenceDAO;

    public EventServiceImpl() throws OperationFailedException {
        try {
            eventDAO = new EventDAOImpl();
        } catch (DAOException e) {
            throw new OperationFailedException("Database error!");
        }
    }

    @Override
    public boolean addEvent(Event event) throws OperationFailedException {

        int eventId = 0;
        //check if event object is valid
        if (isValid(event)) {

            try {

                if (event.getEventRecurrences() == null || event.getEventRecurrences().isEmpty()) {
                    eventId = eventDAO.addEvent(event);
                } else {
                    eventId = eventDAO.addEventWithRecurrences(event);
                }

                //if event info is successfully inserted, insert also invitations
                // TODO: 7/25/16
                if (eventId != 0 && event.getInvitations() != null && !event.getInvitations().isEmpty()) {
                    for (Invitation invitation : event.getInvitations()) {
                        invitationDAO.addInvitation(invitation);
                    }
                }
            } catch (DAOException e) {
                throw new OperationFailedException("Database error!");
            }
        }
        return eventId != 0;
    }

    @Override
    public Event getEventById(int eventId) {
        return null;
    }

    @Override
    public boolean updateEvent(Event event) {
        return false;
    }

    @Override
    public boolean deleteEvent(int eventId) {
        return false;
    }

    @Override
    public List<Event> getAllEvents() {
        return null;
    }

    @Override
    public boolean deleteAllEvents() {
        return false;
    }

    @Override
    public List<Event> getEventsByCategory(int categoryId) {
        return null;
    }

    @Override
    public List<Event> getEventsByUserId(String userRole, int userId) {
        return null;
    }

    @Override
    public List<Event> getParticipatedEventsByUserId(int userId) {
        return null;
    }

    @Override
    public List<Event> getAcceptedEventsByUserId(int userId) {
        return null;
    }

    private boolean isValid(Event event) {
        //todo implement
        return false;
    }
}
