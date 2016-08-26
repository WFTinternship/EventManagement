package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.JsonResponse;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.service.CategoryService;
import com.workfront.internship.event_management.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.workfront.internship.event_management.controller.util.PageParameters.*;

/**
 * Created by Hermine Turshujyan 8/22/16.
 */
@Controller
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/events")
    public String loadAllEvents(Model model, HttpServletRequest request) {

        List<Category> categoryList = categoryService.getAllCategories();
        List<Event> eventList = eventService.getAllEvents();

        model.addAttribute("events", eventList);
        model.addAttribute("categories", categoryList);

        return EVENT_DETAILS_VIEW;
    }

    @RequestMapping(value = "/events-ajax", produces = "application/json")
    @ResponseBody
    public JsonResponse loadEventsByCategory(Model model, HttpServletRequest request) {

        List<Event> eventList;
        String categoryIdStr = request.getParameter("categoryId");

        if (categoryIdStr == null) {
            eventList = eventService.getAllEvents();
        } else {
            eventList = eventService.getEventsByCategory(Integer.parseInt(categoryIdStr));
        }

        JsonResponse result = new JsonResponse();
        result.setStatus(ACTION_SUCCESS);
        result.setResult(eventList);

        return result;
    }

    @GetMapping(value = "/events/{eventId}")
    public String getEvent(@PathVariable("eventId") int id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);

        return EVENT_DETAILS_VIEW;
    }

    @RequestMapping(value = "/new-event")
    public ModelAndView createEvent(HttpServletRequest request, Model model) {
        //check if user is logged in
        // if (request.getSession().getAttribute("user") != null) {

        ModelAndView mov = new ModelAndView(EVENT_EDIT_VIEW);
        List<Category> categoryList = categoryService.getAllCategories();

        model.addAttribute("categories", categoryList);
        model.addAttribute("event", createEmptyEvent());

        return mov;
       /* } else {
            ModelAndView mov = new ModelAndView(DEFAULT_ERROR_VIEW);
            return mov;
        }*/
    }

    private Event createEmptyEvent() {
        Event event = new Event();
        event.setTitle("")
                .setShortDescription("")
                .setFullDescription("")
                .setLocation("");

        return event;
    }
}
