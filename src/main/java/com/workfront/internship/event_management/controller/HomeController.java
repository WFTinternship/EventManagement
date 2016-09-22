package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.workfront.internship.event_management.controller.util.PageParameters.HOME_VIEW;

/**
 * Created by Hermine Turshujyan 8/22/16.
 */
@Controller
public class HomeController {

    @Autowired
    private EventService eventService;

    @RequestMapping(value = {"/", "home"})
    public String loadUpcomingEventsForHomePage(HttpServletRequest request, Model model) {
        User sessionUser = (User) request.getSession().getAttribute("user");

        List<Event> eventList = null;

        if (sessionUser == null) {
            //load only public events
            eventList = eventService.getPublicUpcomingEvents();
        } else {
            //load all upcoming events
            eventList = eventService.getAllUpcomingEvents();
        }
        model.addAttribute("events", eventList);

        return HOME_VIEW;
    }


}
