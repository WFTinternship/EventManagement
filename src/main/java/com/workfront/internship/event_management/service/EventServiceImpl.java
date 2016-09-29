package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.EventDAO;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.Media;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    private CategoryService categoryService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private FileService fileService;

    @Override
    public Event createEvent(Event event) {

        event.setCreationDate(new Date());

        //check if event object is valid
        if (!isValidEvent(event)) {
            String message = "Invalid event";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        int eventId;
        if (isEmptyCollection(event.getEventRecurrences())) {
            eventId = eventDAO.addEvent(event);
        } else {
            eventId = eventDAO.addEventWithRecurrences(event);
        }

        if (eventId == 0){
            String message = "Unable to add event";
            logger.error(message);
            throw new OperationFailedException(message);
        }

        //set generated id to event
        event.setId(eventId);

        if (!isEmptyCollection(event.getInvitations())) {

            //set event id to all invitations
            List<Invitation> invitations = event.getInvitations();
            for (int i = 0; i < invitations.size(); i++) {
                Invitation invitation = invitations.get(i);
                invitation.setEventId(eventId);
            }

            try {
                //insert into db
                invitationService.addInvitations(event.getInvitations());
            } catch (InvalidObjectException | OperationFailedException e) {
                String message = "Could not save invitations (" + e.getMessage() +
                        "). Event successfully added.";
                logger.error(message);
                throw new OperationFailedException(message);
            }

            try {
                //read category info(title) from db for email template
                Category category = categoryService.getCategoryById(event.getCategory().getId());
                event.setCategory(category);

                //send invitations to invitees
                emailService.sendInvitations(event);
            } catch (MailException e) {
                String message = "Could not send email invitations. " +
                        "Event with invitations successfully added.";
                logger.error(message);
                throw new OperationFailedException(message);
            }
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

        List<Media> media = mediaService.getMediaByEvent(eventId);
        event.setMedia(media);

        return event;
    }

    @Override
    public List<Event> getAllEventsByKeyword(String keyword) {
        if(isEmptyString(keyword)){
            throw new InvalidObjectException("Empty keyword");
        }

        return eventDAO.getAllEventsByKeyword(keyword);
    }

    @Override
    public List<Event> getPublicEventsByKeyword(String keyword) {
        if(isEmptyString(keyword)){
            throw new InvalidObjectException("Empty keyword");
        }

        return eventDAO.getPublicEventsByKeyword(keyword);
    }

    @Override
    public List<Event> getPublicEventsByCategory(int categoryId) {
        if (categoryId < 1) {
            throw new InvalidObjectException("Invalid category id");
        }

        return eventDAO.getPublicEventsByCategory(categoryId);
    }

    @Override
    public List<Event> getAllEventsByCategory(int categoryId) {
        if (categoryId < 1) {
            throw new InvalidObjectException("Invalid category id");
        }

        return eventDAO.getAllEventsByCategory(categoryId);
    }

    @Override
    public boolean editEvent(Event event) throws OperationFailedException {

        if (!isValidEvent(event)) {
            throw new InvalidObjectException("Invalid event");
        }

        //get event's image name before update
        Event oldEvent = getEventById(event.getId());
        String oldImageName = oldEvent.getImageName();

        //set modification date
        event.setLastModifiedDate(new Date());

        //update event main info
        boolean success = eventDAO.updateEvent(event);
        if (!success) {
            String message = "Could not update event information";
            logger.error(message);
            throw new OperationFailedException(message);
        }

        //if event is successfully updated and image is changed/deleted in edit page,
        //delete also previous from fs
        if (!isEmptyString(oldImageName)) {
            String newFileName = event.getFileName();
            if (isEmptyString(newFileName) || !newFileName.equals(oldImageName)) {
                try {
                    fileService.deleteFile(oldImageName);
                } catch (IOException e) {
                    logger.warn("Could not delete old image from file system");
                }
                // TODO: 9/26/16 delete image from fs
            }
        }

        //read updated main data from db for invitation template (included category title and event creation date)
        Category updatedCategory = categoryService.getCategoryById(event.getCategory().getId());
        event.setCategory(updatedCategory);

        try {
            //update event invitations
            invitationService.editInvitationList(event);

        } catch (InvalidObjectException | OperationFailedException e) {
            String message = "Could not edit invitations (" + e.getMessage() +
                    "). Event main info successfully edited.";
            logger.error(message);
            throw new OperationFailedException(message);
        } catch (MailException e){
            String message = "Could not send email invitations. " +
                    "Event with invitations successfully edited.";
            logger.error(message);
            throw new OperationFailedException(message);
        }
        return success;
    }

    @Override
    public List<Event> getUserOrganizedEvents(int userId) throws OperationFailedException {
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }

        return eventDAO.getUserOrganizedEvents(userId);
    }

    @Override
    public List<Event> getUserInvitedEvents(int userId) {
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }

        return eventDAO.getUserAllEvents(userId);
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
}
