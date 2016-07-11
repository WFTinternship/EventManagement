package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventInvitation;
import com.workfront.internship.event_management.model.EventRecurrence;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.model.datehelpers.RecurrenceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hermine Turshujyan on 6/29/16.
 */
public  class DAOTest {
    public static void main(String[] args) {
        User testUser = new User();
        testUser.setId(111369);
        User testUser1 = new User();
        testUser1.setId(111370);

        EventInvitation inv1 = new EventInvitation();
        inv1.setUser(testUser).setUserRole("Organizer").setUserResponse("Undefined");
        EventInvitation inv2 = new EventInvitation();
        inv2.setUser(testUser1).setUserRole("Member").setUserResponse("Undefined");
        List<EventInvitation> l  = new ArrayList<EventInvitation>();
        l.add(inv1);
        l.add(inv2);


        RecurrenceTypeDAO recTypeDAO = new RecurrenceTypeDAOImpl();
        RecurrenceType recType = recTypeDAO.getRecurrenceTypeById(2);

        EventRecurrence rec1 = new EventRecurrence();
        rec1.setRecurrenceType(recType).setRepeatInterval(2);
        EventRecurrence rec2 = new EventRecurrence();
        rec2.setRecurrenceType(recType).setRepeatInterval(1);
        List<EventRecurrence> list = new ArrayList<EventRecurrence>();
        list.add(rec1);
        list.add(rec2);


        Event event = new Event();
        event.setTitle("aakjhjh").setPublicAccessed(true).setGuestsAllowed(true).setCreationDate(new java.util.Date());
        event.setRecurrences(list).setInvitations(l);

        EventDAO eventDAO = new EventDAOImpl();
      //  eventDAO.insertEvent(event, 5);
//eventDAO.getEventsByUserId("Organizer", 3);
        eventDAO.getAcceptedEventsByUserId(3);

        EventRecurrenceDAO rec = new EventRecurrenceDAOImpl();
        rec.getEventRecurrencesByEventId(111230);
        EventRecurrenceDAO recDAO = new EventRecurrenceDAOImpl();
       // recDAO.deleteEventRecurrece(2);



        EventInvitationDAO invitationDAO = new EventInvitationDAOImpl();
      //  invitationDAO.getInvitationsByEventId(111212);*/

    }


}