package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.workfront.internship.event_management.controller.util.PageParameters.HOME_VIEW;
import static com.workfront.internship.event_management.controller.util.PageParameters.SEARCH_RESULTS_VIEW;

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

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(HttpServletRequest request, Model model) {
        String keyword = request.getParameter("keyword");
        User sessionUser = (User) request.getSession().getAttribute("user");

        List<Event> eventList = null;

        if (sessionUser == null) {
            //load only public events
            eventList = eventService.getPublicEventsByKeyword(keyword);
        } else {
            //load all upcoming events
            eventList = eventService.getAllEventsByKeyword(keyword);
        }

        model.addAttribute("events", eventList);
        model.addAttribute("keyword", keyword);

        return SEARCH_RESULTS_VIEW;
    }

}
