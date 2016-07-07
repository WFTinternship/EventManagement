package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventInvitation;

import java.util.List;

/**
 * Created by hermine on 7/8/16.
 */
public interface EventInvitationDAO {
    public boolean insertInvitation(EventInvitation invitation);
    public boolean insertInvitationsList(List<EventInvitation> invitations);
    public boolean updateInvitation(EventInvitation invitation);
    public boolean deleteInvitation(int eventId, int userId);
}
