package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Invitation;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/26/16.
 */
public interface InvitationService {

    Invitation addInvitation(Invitation invitation);

    void addInvitations(List<Invitation> invitations);

    //get records from db
    Invitation getInvitationById(int invitationId);

    List<Invitation> getAllInvitations();

    List<Invitation> getInvitationsByEventId(int eventId);

    List<Invitation> getInvitationsByUserId(int userId);


    //update record in db
    void updateInvitation(Invitation invitation);

    void updateInvitations(List<Invitation> invitations);

    //delete records from db
    void deleteInvitation(int invitationId);

    void deleteInvitationsByEventId(int eventId);

    void deleteInvitationsByUserId(int userId);

    void deleteAllInvitations();

}
