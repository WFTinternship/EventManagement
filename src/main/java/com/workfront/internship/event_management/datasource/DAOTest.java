package com.workfront.internship.event_management.datasource;

import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventCategory;
import com.workfront.internship.event_management.model.User;

import java.util.Date;

/**
 * Created by Hermine Turshujyan on 6/29/16.
 */
public  class DAOTest {
    public static void main(String[] args) {
        //        java.util.Date date = new Date();
        //        Object param = new java.sql.Timestamp(date.getTime());
        //       // java.sql.Timestamp ts = new java.sql.Timestamp(timeNow);
        //
//        System.out.println(new Date().getTime());
//                UserDAO userDAO = new UserDAOImpl();
//        User user = userDAO.getUserById(21);
//        user.setPassword("22222222222");
//        userDAO.updateUser(user);
       // userDAO.deleteUser(21);
        //
        //        User user = new User();
        //        user.setFirstName("Anna")
        //                .setLastName("Asmangulyan")
        //                .setUsername("asmangulyan")
        //                .setPassword("password")
        //                .setEmail("aa@gmail.com");
        //
        //        userDAO.insertUser(user);
//eventDAO.getEventsByCategory(6);
//        eventDAO.getAllEvents();
//      //  EventCategoryDAO catDAO = new EventCategoryDAOImpl();
//       // catDAO.deleteCategory(6);
//
//        EventMediaDAO mediaDAO = new EventMediaDAOImpl();
//        // mediaDAO.deleteMedia(6);
//        mediaDAO.getAllMedia();

        EventDAO eventDAO = new EventDAOImpl();
        Event event = eventDAO.getEventById(3);

        Event ev = new Event();
        ev.setTitle("kuku");
     //   eventDAO.insertEvent(ev);
    }


}