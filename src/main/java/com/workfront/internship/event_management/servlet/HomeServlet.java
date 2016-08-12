package com.workfront.internship.event_management.servlet;

import com.google.gson.Gson;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.service.EventService;
import com.workfront.internship.event_management.service.EventServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Hermine Turshujyan 8/10/16.
 */
public class HomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        EventService eventService = new EventServiceImpl();
        List<Event> eventList = eventService.getAllEvents();

        String json = new Gson().toJson(eventList);

        response.setContentType("application/json");
        response.getWriter().write(json);
    }

}
