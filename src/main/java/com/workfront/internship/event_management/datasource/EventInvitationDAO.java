package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventInvitation;

import java.sql.Connection;
import java.util.List;

/**
 * Created by hermine on 7/8/16.
 */
public interface EventInvitationDAO {

     //Create
     int insertInvitation(EventInvitation invitation, Connection conn);
     int insertInvitation(EventInvitation invitation);

     //Read
     EventInvitation getInvitationById(int invitationId);
     List<EventInvitation> getInvitationsByEventId(int eventId);

     //Update
     boolean updateInvitation(EventInvitation invitation);

     //Delete
     boolean deleteInvitation(int invId);
     boolean deleteInvitationsByEventId(int eventId);

}
