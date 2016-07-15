package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.EventInvitation;

import java.sql.Connection;
import java.util.List;

/**
 * Created by hermine on 7/8/16.
 */
public interface EventInvitationDAO {

     //insert invitation into db
     int addInvitation(EventInvitation invitation, Connection conn);

     int addInvitation(EventInvitation invitation);

     //get records from db
     EventInvitation getInvitationById(int invitationId);
     List<EventInvitation> getInvitationsByEventId(int eventId);

     List<EventInvitation> getInvitationsByUserId(int userId);


     //update record in db
     boolean updateInvitation(EventInvitation invitation);

     //delete records from db
     boolean deleteInvitation(int invitationId);
     boolean deleteInvitationsByEventId(int eventId);

     boolean deleteInvitationsByUserId(int userId);

     boolean deleteAllInvitations();


}
