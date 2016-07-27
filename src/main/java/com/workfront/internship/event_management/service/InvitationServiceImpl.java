package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Invitation;

import java.util.List;

/**
 * Created by Hermine Turshujyan 7/26/16.
 */
public class InvitationServiceImpl implements InvitationService {
    @Override
    public int addInvitation(Invitation invitation) {
        return 0;
    }

    @Override
    public int addInvitations(List<Invitation> invitation) {
        return 0;
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
    public boolean updateInvitation(Invitation invitation) {
        return false;
    }

    @Override
    public boolean deleteInvitation(int invitationId) {
        return false;
    }

    @Override
    public boolean deleteInvitationsByEventId(int eventId) {
        return false;
    }

    @Override
    public boolean deleteInvitationsByUserId(int userId) {
        return false;
    }

    @Override
    public boolean deleteAllInvitations() {
        return false;
    }
}
