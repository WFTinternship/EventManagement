package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.EventDAO;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.UserRole;
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
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private EmailService emailService;

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

        if(eventId == 0){
            throw new OperationFailedException("Unable to add event!");
        }

        if (!isEmptyCollection(event.getInvitations())) {
            //set event id to all invitations
            List<Invitation> invitations = event.getInvitations();
            for (int i = 0; i < invitations.size(); i++) {
                Invitation invitation = invitations.get(i);
                invitation.setEventId(eventId);
            }
            //insert into db
            invitationService.addInvitations(event.getInvitations());

            //send invitations to invitees
            emailService.sendInvitations(event);
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
    public Event getFullEventById(int eventId) {
        //get event main info
        Event event = getEventById(eventId);

        //get event invitations
        List<Invitation> invitations = invitationService.getInvitationsByEvent(eventId);
        event.setInvitations(invitations);

        return event;
    }

    @Override
    public boolean editEvent(Event event) throws OperationFailedException {
        if (!isValidEvent(event)) {
            throw new InvalidObjectException("Invalid event");
        }
        //add modification date
        event.setLastModifiedDate(new Date());

        //if image deleted in edit page, delete from file system
        if(isEmptyString(event.getFileName())) {
            Event eventBeforeUpdate = getEventById(event.getId());
            String prevImageName = eventBeforeUpdate.getImageName();

            if (!isEmptyString(prevImageName)) {
                // TODO: 9/26/16 delete image from fs
            }
        }

        //update event main info
        boolean success = eventDAO.updateEvent(event);

        Category updatedCategory = categoryService.getCategoryById(event.getCategory().getId());

        //read updated main data from db (included category title and event creation date)
        event.setCategory(updatedCategory);

        if (success) {
            //update event recurrences
            recurrenceService.editRecurrenceList(event.getId(), event.getEventRecurrences());

            //update event invitations
            invitationService.editInvitationList(event);
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
    public List<Event> getPublicEvents() {
        return eventDAO.getPublicEvents();
    }

    @Override
    public List<Event> getAllUpcomingEvents() {
        return eventDAO.getAllUpcomingEvents();
    }

    @Override
    public List<Event> getPublicUpcomingEvents() {
        return eventDAO.getPublicUpcomingEvents();
    }

    @Override
    public List<Event> getAllPastEvents() {
        return eventDAO.getAllPastEvents();
    }

    @Override
    public List<Event> getPublicPastEvents() {
        return eventDAO.getPublicPastEvents();
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

    @Override
    public Event createEmptyEvent() {
        Event event = new Event();
        event.setTitle("")
                .setShortDescription("")
                .setFullDescription("")
                .setLocation("")
                .setPublicAccessed(true);

        return event;
    }

    //helper methods
    private void setDefaultFields(Event event) {
        event.setCreationDate(new Date());
    }
}
