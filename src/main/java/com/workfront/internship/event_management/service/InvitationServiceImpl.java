package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.InvitationDAO;
import com.workfront.internship.event_management.dao.InvitationDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Invitation;
import org.apache.log4j.Logger;

import java.util.List;

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
            //insert category into db
            int invitationId = invitationDAO.addInvitation(invitation);

            //set generated it to category
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
    public void addInvitations(List<Invitation> invitation) {
    }

    @Override
    public Invitation getInvitationById(int invitationId) {
        return null;
    }

    @Override
    public List<Invitation> getAllInvitations() {
        return null;
    }

    @Override
    public List<Invitation> getInvitationsByEventId(int eventId) {
        return null;
    }

    @Override
    public List<Invitation> getInvitationsByUserId(int userId) {
        return null;
    }

    @Override
    public void updateInvitation(Invitation invitation) {

    }

    @Override
    public void updateInvitations(List<Invitation> invitations) {

    }

    @Override
    public void deleteInvitation(int invitationId) {
    }

    @Override
    public void deleteInvitationsByEventId(int eventId) {
    }

    @Override
    public void deleteInvitationsByUserId(int userId) {
    }

    @Override
    public void deleteAllInvitations() {
    }
}
