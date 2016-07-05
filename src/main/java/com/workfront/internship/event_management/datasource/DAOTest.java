package com.workfront.internship.event_management.datasource;

/**
 * Created by Hermine Turshujyan on 6/29/16.
 */
public  class DAOTest {
    public static void main(String[] args) {
        //        java.util.Date date = new Date();
        //        Object param = new java.sql.Timestamp(date.getTime());
        //       // java.sql.Timestamp ts = new java.sql.Timestamp(timeNow);
        //
        //        UserDAO userDAO = new UserDAOImpl();
        //        userDAO.getAllUsers();
        //
        //        User user = new User();
        //        user.setFirstName("Anna")
        //                .setLastName("Asmangulyan")
        //                .setUsername("asmangulyan")
        //                .setPassword("password")
        //                .setEmail("aa@gmail.com");
        //
        //        userDAO.insertUser(user);
        //        EventDAO eventDAO = new EventDAOImpl();
        //        eventDAO.getParticipantsByEventId(3);


        EventMediaDAO mediaDAO = new EventMediaDAOImpl();
        // mediaDAO.deleteMedia(6);
        mediaDAO.getAllMedia();
    }


}