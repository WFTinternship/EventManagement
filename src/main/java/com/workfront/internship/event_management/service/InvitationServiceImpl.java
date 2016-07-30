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

        for (Invitation invitation : invitationList) {
            if (!isValidInvitation(invitation)) {
                throw new OperationFailedException("Invalid invitation");
            }
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
        if (invitationId < 0) {
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
    public List<Invitation> getInvitationsByEventId(int eventId) {
        if (eventId < 0) {
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
    public List<Invitation> getInvitationsByUserId(int userId) {
        if (userId < 0) {
            throw new OperationFailedException("Invalid user id");
        }
        try {
            return invitationDAO.getInvitationsByEventId(userId);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new OperationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public void updateInvitation(Invitation invitation) {
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
    public void updateInvitations(int eventId, List<Invitation> invitationList) {
        if (!isEmptyCollection(invitationList)) {
            for (Invitation invitation : invitationList) {
                if (!isValidInvitation(invitation)) {
                    throw new OperationFailedException("Invalid invitation");
                }
            }
        }

        //if invitation's list is empty, delete existing invitations from db
        if (isEmptyCollection(invitationList)) {
            deleteInvitationsByEventId(eventId);
        } else {
            //get current invitations from db
            List<Invitation> dbInvitationList = getInvitationsByEventId(eventId);

            //if there is no invitations in db, insert new invitation list
            if (isEmptyCollection(dbInvitationList)) {
                addInvitations(invitationList);
            } else {
                // compare invitation's list from db with new invitations's list
                for (Invitation dbInvitation : dbInvitationList) {
                    if (!invitationList.contains(dbInvitation)) {
                        deleteInvitation(dbInvitation.getId());
                    } else if (dbInvitation != invitationList.get(dbInvitation.getId())) {
                        updateInvitation(dbInvitation);
                    }
                }

                for (Invitation invitation : invitationList) {
                    if (!dbInvitationList.contains(invitation)) {
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
    public void deleteInvitationsByEventId(int eventId) {
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
    public void deleteInvitationsByUserId(int userId) {
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
}
