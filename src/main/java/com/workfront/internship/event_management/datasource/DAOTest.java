package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hermine Turshujyan on 6/29/16.
 */
public  class DAOTest {
    public static void main(String[] args) {
         EventDAO eventDAO = new EventDAOImpl();
        Event event = eventDAO.getEventById(3);
        List<DateRange> dates = new ArrayList<DateRange>();
        dates.add(new DateRange(event.getCreationDate(), event.getCreationDate()));
        event.setDates(dates);
        eventDAO.createEvent(event, 4);
//        invitation.setUserResponse("Maybe");
//        eventDAO.updateInvitation(invitation);
    }


}