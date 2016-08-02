package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.InvitationDAO;
import com.workfront.internship.event_management.dao.InvitationDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Invitation;
import org.apache.log4j.Logger;

import java.util.List;

import static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection;
import static com.workfront.internship.event_management.service.util.Validator.isValidInvitation;

/**
 * Created by Hermine Turshujyan 7/26/16.
 */
public class InvitationServiceImpl implements InvitationService {

    private static final Logger LOGGER = Logger.getLogger(InvitationServiceImpl.class);
    private InvitationDAO invitationDAO;

    public InvitationServiceImpl() {
        try {
            invitationDAO = new InvitationDAOImpl();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public Invitation addInvitation(Invitation invitation) {
        if (!isValidInvitation(invitation)) {
            throw new OperationFailedException("Invalid invitation");
        }

        try {
            //insert invitation into db
            int invitationId = invitationDAO.addInvitation(invitation);

            //set generated it to invitation
            invitation.setId(invitationId);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Invitation for user with id "
                    + invitation.getUser().getId() + " and event with id " + invitation.getEventId() + " already exists!", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
        return invitation;
    }

    @Override
    public void addInvitations(List<Invitation> invitationList) {
        if (isEmptyCollection(invitationList)) {
            throw new OperationFailedException("Empty invitation list");
        }

        try {
            //insert invitation list into db
            invitationDAO.addInvitations(invitationList);

        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Invitation already exists!", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public Invitation getInvitation(int invitationId) {
        if (invitationId < 1) {
            throw new OperationFailedException("Invalid invitation id");
        }

        try {
            return invitationDAO.getInvitationById(invitationId);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Recurrence type not found");
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Invitation> getAllInvitations() {
        try {
            return invitationDAO.getAllInvitations();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Invitation> getInvitationsByEvent(int eventId) {
        if (eventId < 1) {
            throw new OperationFailedException("Invalid event id");
        }

        try {
            return invitationDAO.getInvitationsByEventId(eventId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public List<Invitation> getInvitationsByUser(int userId) {
        if (userId < 1) {
            throw new OperationFailedException("Invalid user id");
        }
        try {
            return invitationDAO.getInvitationsByUserId(userId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void editInvitation(Invitation invitation) {
        if (!isValidInvitation(invitation)) {
            throw new OperationFailedException("Invalid invitation");
        }

        try {
            //insert invitation into db
            invitationDAO.updateInvitation(invitation);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Invitation not found", e);
        } catch (DuplicateEntryException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Invitation for user with id "
                    + invitation.getUser().getId() + " and event with id " + invitation.getEventId() + " already exists!", e);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
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
    public void deleteInvitation(int invitationId) {
        if (invitationId < 1) {
            throw new OperationFailedException("Invalid invitation id");
        }

        try {
            invitationDAO.deleteInvitation(invitationId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException("Invitation not found", e);
        }
    }

    @Override
    public void deleteInvitationsByEvent(int eventId) {
        if (eventId < 1) {
            throw new OperationFailedException("Invalid event id");
        }

        try {
            invitationDAO.deleteInvitationsByEventId(eventId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteInvitationsByUser(int userId) {
        if (userId < 1) {
            throw new OperationFailedException("Invalid user id");
        }

        try {
            invitationDAO.deleteInvitationsByUserId(userId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllInvitations() {
        try {
            invitationDAO.deleteAllInvitations();
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
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
