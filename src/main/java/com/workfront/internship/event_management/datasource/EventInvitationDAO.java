package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventInvitation;

import java.sql.Connection;
import java.util.List;

/**
 * Created by hermine on 7/8/16.
 */
public interface EventInvitationDAO {
     boolean insertInvitation(EventInvitation invitation, Connection conn);
     boolean insertInvitation(EventInvitation invitation);
     boolean insertInvitations(List<EventInvitation> invitations, Connection conn);
     boolean insertInvitations(List<EventInvitation> invitations);
     List<EventInvitation> getInvitationsByEventId(int eventId);
     boolean updateInvitation(EventInvitation invitation);
     boolean deleteInvitation(int eventId, int userId);
     boolean deleteInvitationsByEventId(int eventId);

}
