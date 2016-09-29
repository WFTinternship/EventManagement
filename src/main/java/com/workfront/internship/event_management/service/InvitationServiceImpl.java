package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.InvitationDAO;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.*;

/**
 * Created by Hermine Turshujyan 7/26/16.
 */

@Component
public class InvitationServiceImpl implements InvitationService {

    private static final Logger logger = Logger.getLogger(InvitationServiceImpl.class);

    @Autowired
    private InvitationDAO invitationDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EventService eventService;

    @Override
    public Invitation createInvitation(String email) {

        if (!isValidEmailAddressForm(email)) {
            String message = "Invalid email address form";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        User user = userService.getUserByEmail(email);

        if (user == null) {
            String message = "User with this email not found!";
            logger.error(message);
            throw new ObjectNotFoundException(message);
        } else {

            Invitation invitation = new Invitation();
            invitation.setUser(user)
                    .setAttendeesCount(1)
                    .setParticipated(false)
                    .setUserResponse(new UserResponse(5, "Waiting for response"))
                    .setCreationDate(new Date());

            return invitation;
        }
    }

    @Override
    public Invitation addInvitation(Invitation invitation) {

        if (!isValidInvitation(invitation)) {
            String message = "Invalid invitation";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        try {
            //insert invitation into db
            int invitationId = invitationDAO.addInvitation(invitation);

            if (invitationId == 0){
                String message = "Could not add invitation";
                logger.error(message);
                throw new OperationFailedException(message);
            }
            //set generated it to invitation
            invitation.setId(invitationId);
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException(String.format("Invitation for user with id %d and event with id already exists!",
                    invitation.getUser().getId(), invitation.getEventId()), e);
        }
        return invitation;
    }

    @Override
    public void addInvitations(List<Invitation> invitationList) {

        if (isEmptyCollection(invitationList)) {
            String message = "Empty invitation list";
            logger.error(message);
            throw new InvalidObjectException(message);
        }

        for(Invitation invitation : invitationList){
            if (!isValidInvitation(invitation)) {
                String message = "Invalid invitation";
                logger.error(message);
                throw new InvalidObjectException(message);
            }
        }

        try {

            //insert invitation list into db
            invitationDAO.addInvitations(invitationList);
        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException("Invitation already exists!", e);
        }
    }

    @Override
    public Invitation getInvitationById(int invitationId) {
        if (invitationId < 1) {
            throw new InvalidObjectException("Invalid invitation id");
        }

        Invitation invitation = invitationDAO.getInvitationById(invitationId);
        ;
        if (invitation == null) {
            throw new ObjectNotFoundException("Invitation not found");
        }

        return invitation;
    }

    @Override
    public List<Invitation> getAllInvitations() {
        return invitationDAO.getAllInvitations();
    }

    @Override
    public List<Invitation> getInvitationsByEvent(int eventId) {
        if (eventId < 1) {
            throw new InvalidObjectException("Invalid event id");
        }

        return invitationDAO.getInvitationsByEventId(eventId);
    }

    @Override
    public List<Invitation> getInvitationsByUser(int userId) {
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }
        return invitationDAO.getInvitationsByUserId(userId);
    }

    @Override
    public boolean editInvitation(Invitation invitation) {
        if (!isValidInvitation(invitation)) {
            throw new InvalidObjectException("Invalid invitation");
        }

        boolean success;
        try {
            //insert invitation into db
            success = invitationDAO.updateInvitation(invitation);

        } catch (DuplicateEntryException e) {
            logger.error(e.getMessage(), e);
            throw new OperationFailedException(String.format("Invitation for user with id %d and event with id already exists!",
                    invitation.getUser().getId(), invitation.getEventId()), e);
        }

        if (!success) {
            throw new ObjectNotFoundException("Invitation not found!");
        }

        return success;
    }

    @Override
    public void editInvitationList(Event event) {

        List<Invitation> invitationList = event.getInvitations();
        int eventId = event.getId();

        if (isEmptyCollection(invitationList)) {
           // delete all invitations
            deleteInvitationsByEvent(eventId);
        } else {

            List<Invitation> dbInvitationList = getInvitationsByEvent(eventId);

            if (isEmptyCollection(dbInvitationList)) {
                addInvitations(invitationList);
                emailService.sendInvitations(event);
            } else {

                for (Invitation dbInvitation : dbInvitationList) {
                    int userId = dbInvitation.getUser().getId();

                    if (getInvitationWithUserIdFromList(invitationList, userId) == null) {
                        deleteInvitation(dbInvitation.getId());
                    }
                }

                for (Invitation invitation : invitationList) {
                    int userId = invitation.getUser().getId();

                    if (getInvitationWithUserIdFromList(dbInvitationList, userId) == null) {
                        addInvitation(invitation);
                        emailService.sendInvitation(event, invitation);
                    }
                }
            }
        }
    }

    @Override
    public boolean respondToInvitation(int eventId, int userId, int responseId) {
        if (eventId < 1) {
            throw new InvalidObjectException("Invalid event id");
        }
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }
        if (responseId < 1) {
            throw new InvalidObjectException("Invalid response id");
        }

        boolean success = invitationDAO.updateInvitationResponse(eventId, userId, responseId);
        if (success) {
            Event event = eventService.getFullEventById(eventId);

            Invitation respondedInvitation = null;
            List<Invitation> invitations = event.getInvitations();
            if(!isEmptyCollection(invitations)) {

                for (Invitation invitation : invitations) {
                    if(invitation.getUser().getId() == userId){
                        respondedInvitation = invitation;
                    }
                }
            }
            emailService.sendRespondNotificationToOrganizer(event, respondedInvitation);
        }
        return success;
    }

    @Override
    public boolean deleteInvitation(int invitationId) {
        if (invitationId < 1) {
            throw new InvalidObjectException("Invalid invitation id");
        }

        return invitationDAO.deleteInvitation(invitationId);
    }

    @Override
    public void deleteInvitationsByEvent(int eventId) {
        if (eventId < 1) {
            throw new InvalidObjectException("Invalid event id");
        }

        invitationDAO.deleteInvitationsByEventId(eventId);
    }


    @Override
    public void deleteInvitationsByUser(int userId) {
        if (userId < 1) {
            throw new InvalidObjectException("Invalid user id");
        }

        invitationDAO.deleteInvitationsByUserId(userId);
    }

    @Override
    public void deleteAllInvitations() {
        invitationDAO.deleteAllInvitations();
    }

    //helper methods
    private Invitation getInvitationWithUserIdFromList(List<Invitation> invitationList, int id) {
        for (Invitation invitation : invitationList) {
            if (invitation.getUser().getId() == id) {
                return invitation;
            }
        }
        return null;
    }

}
