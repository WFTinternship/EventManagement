package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.InvitationDAO;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.model.UserResponse;
import com.workfront.internship.event_management.model.UserRole;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection;
import static com.workfront.internship.event_management.service.util.Validator.isValidEmailAddressForm;
import static com.workfront.internship.event_management.service.util.Validator.isValidInvitation;

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

    @Override
    public Invitation createInvitationForEmail(String email) {
        if (!isValidEmailAddressForm(email)) {
            throw new InvalidObjectException("Invalid email address form");
        }

        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new ObjectNotFoundException("User not found!");
        } else {
            Invitation invitation = new Invitation();
            invitation.setUser(user)
                    .setUserRole(UserRole.MEMBER)
                    .setAttendeesCount(1)
                    .setParticipated(false)
                    .setUserResponse(new UserResponse(1, "Undefined")); // TODO: 9/6/16 check

            return invitation;
        }
    }

    @Override
    public Invitation addInvitation(Invitation invitation) {
        if (!isValidInvitation(invitation)) {
            throw new InvalidObjectException("Invalid invitation");
        }

        try {
            //insert invitation into db
            int invitationId = invitationDAO.addInvitation(invitation);

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
            throw new InvalidObjectException("Empty invitation list");
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
    public void editInvitationList(int eventId, List<Invitation> invitationList) {

        if (isEmptyCollection(invitationList)) {
            deleteInvitationsByEvent(eventId);
        } else {

            List<Invitation> dbInvitationList = getInvitationsByEvent(eventId);

            if (isEmptyCollection(dbInvitationList)) {
                addInvitations(invitationList);
            } else {

                for (Invitation dbInvitation : dbInvitationList) {
                    int invitationId = dbInvitation.getId();

                    if (getInvitationWithIdFromList(invitationList, invitationId) == null) {
                        deleteInvitation(invitationId);
                    } else if (dbInvitation != getInvitationWithIdFromList(invitationList, invitationId)) {
                        editInvitation(getInvitationWithIdFromList(invitationList, invitationId));
                    }
                }

                for (Invitation invitation : invitationList) {
                    int invitationId = invitation.getId();

                    if (getInvitationWithIdFromList(dbInvitationList, invitationId) == null) {
                        addInvitation(invitation);
                    }
                }
            }
        }
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
    private Invitation getInvitationWithIdFromList(List<Invitation> invitationList, int id) {
        for (Invitation invitation : invitationList) {
            if (invitation.getId() == id) {
                return invitation;
            }
        }
        return null;
    }
}
