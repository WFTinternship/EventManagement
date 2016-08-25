package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Hermine Turshujyan 8/22/16.
 */
@Controller
public class MainController {

    @Autowired
    private EventService eventService;

    @RequestMapping(value = {"/", "home"})
    public String loadUpcomingEvents(Model model) {
        List<Event> eventList = eventService.getAllEvents();
        model.addAttribute("events", eventList);

        return "index";
    }
}
