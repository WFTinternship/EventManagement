package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Invitation;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/26/16.
 */
public interface InvitationService {

    int addInvitation(Invitation invitation);

    int addInvitations(List<Invitation> invitation);


    //get records from db
    Invitation getInvitationById(int invitationId);

    List<Invitation> getAllInvitations();

    List<Invitation> getInvitationsByEventId(int eventId);

    List<Invitation> getInvitationsByUserId(int userId);


    //update record in db
    boolean updateInvitation(Invitation invitation);

    //delete records from db
    boolean deleteInvitation(int invitationId);

    boolean deleteInvitationsByEventId(int eventId);

    boolean deleteInvitationsByUserId(int userId);

    boolean deleteAllInvitations();

}
