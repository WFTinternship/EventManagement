package com.workfront.internship.event_management.servlet;

import com.google.gson.Gson;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.service.EventService;
import com.workfront.internship.event_management.spring.EventManagementApplication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Hermine Turshujyan 8/10/16.
 */
public class EventController extends HttpServlet {

    private EventService eventService;

    @Override
    public void init() throws ServletException {
        super.init();
        eventService = EventManagementApplication.getApplicationContext(getServletContext()).getBean(EventService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        List<Event> eventList;

        String categoryIdStr = request.getParameter("categoryId");
        if (categoryIdStr == null) {
            eventList = eventService.getAllEvents();
        } else {
            eventList = eventService.getEventsByCategory(Integer.parseInt(categoryIdStr));
        }

        String json = new Gson().toJson(eventList);

        response.setContentType("application/json");
        response.getWriter().write(json);
    }

}
