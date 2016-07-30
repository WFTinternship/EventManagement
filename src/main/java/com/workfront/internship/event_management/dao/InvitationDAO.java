package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Invitation;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/8/16.
 */
public interface InvitationDAO {

    //insert invitation into db
    int addInvitation(Invitation invitation) throws DAOException, DuplicateEntryException;

    void addInvitations(List<Invitation> invitation) throws DAOException;

    //get records from db
    Invitation getInvitationById(int invitationId) throws ObjectNotFoundException, DAOException;

    List<Invitation> getAllInvitations() throws DAOException;

    List<Invitation> getInvitationsByEventId(int eventId) throws DAOException;

    List<Invitation> getInvitationsByUserId(int userId) throws DAOException;

    //update record in db
    void updateInvitation(Invitation invitation) throws ObjectNotFoundException;

    //delete records from db
    void deleteInvitation(int invitationId) throws DAOException, ObjectNotFoundException;

    void deleteInvitationsByEventId(int eventId) throws DAOException, ObjectNotFoundException;

    void deleteInvitationsByUserId(int userId) throws DAOException, ObjectNotFoundException;

    void deleteAllInvitations() throws DAOException;

}
