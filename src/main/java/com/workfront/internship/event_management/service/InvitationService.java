package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Invitation;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/26/16.
 */
public interface InvitationService {

    Invitation createInvitationForMember(String email);

    Invitation createInvitationForOrganizer(String email);


    Invitation addInvitation(Invitation invitation);

    void addInvitations(List<Invitation> invitationList);

    //get records from db
    Invitation getInvitationById(int invitationId);

    List<Invitation> getAllInvitations();

    List<Invitation> getInvitationsByEvent(int eventId);

    List<Invitation> getInvitationsByUser(int userId);


    //update record in db
    boolean editInvitation(Invitation invitation);

    void editInvitationList(int eventId, List<Invitation> invitationList);

    //delete records from db
    boolean deleteInvitation(int invitationId);

    void deleteInvitationsByEvent(int eventId);

    void deleteInvitationsByUser(int userId);

    void deleteAllInvitations();
}
