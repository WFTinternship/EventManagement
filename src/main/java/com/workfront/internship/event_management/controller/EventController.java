package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.controller.util.DateParser;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.service.CategoryService;
import com.workfront.internship.event_management.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
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
    public String loadAllEventsAndCategories(Model model) {

        List<Category> categoryList = categoryService.getAllCategories();
        List<Event> eventList = eventService.getAllEvents();

        model.addAttribute("events", eventList);
        model.addAttribute("categories", categoryList);

        return ALL_EVENTS_VIEW;
    }

    @RequestMapping(value = "/events-ajax")
    @ResponseBody
    public CustomResponse loadEvents(HttpServletRequest request) {

        List<Event> eventList;
        String categoryIdStr = request.getParameter("categoryId");

        if (categoryIdStr == null) {
            eventList = eventService.getAllEvents();
        } else {
            eventList = eventService.getEventsByCategory(Integer.parseInt(categoryIdStr));
        }

        CustomResponse result = new CustomResponse();
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
    public String goToCreateEventPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        //check if user is logged in
        // if (session.getAttribute("user") != null) {

            List<Category> categoryList = categoryService.getAllCategories();

            model.addAttribute("categories", categoryList);
            model.addAttribute("event", createEmptyEvent());

            return EVENT_EDIT_VIEW;
       /* } else {
            model.addAttribute("message", "Access is denied!");
            return DEFAULT_ERROR_VIEW;
        }*/
    }

    @RequestMapping(value = "/add-event", method = RequestMethod.POST)
    public String addEvent(HttpServletRequest request, Model model) {
        Event event = new Event();

        String title = request.getParameter("event_title");
        String shortDescription = request.getParameter("short_desc");
        String fullDescription = request.getParameter("full_desc");
        String location = request.getParameter("location");

        int categoryId = Integer.parseInt(request.getParameter("category_id"));
        Category category = new Category().setId(categoryId);

        boolean publicAccessed = Boolean.parseBoolean(request.getParameter("public_accessed"));
        boolean guestsAllowed = Boolean.parseBoolean(request.getParameter("guests_allowed"));

        String startDateString = request.getParameter("start_date");
        String endDateString = request.getParameter("end_date");
        String startTimeString = request.getParameter("start_time");
        String endTimeString = request.getParameter("end_time");

        Date startDate = DateParser.parseStringToDate(startDateString, startTimeString);
        Date endDate = DateParser.parseStringToDate(endDateString, endTimeString);

        event.setTitle(title)
                .setShortDescription(shortDescription)
                .setFullDescription(fullDescription)
                .setLocation(location)
                .setPublicAccessed(publicAccessed)
                .setGuestsAllowed(guestsAllowed)
                .setCategory(category)
                .setStartDate(startDate)
                .setEndDate(endDate);

        eventService.createEvent(event);

        return ALL_EVENTS_VIEW;
    }



    @RequestMapping(value = "/edit-event/{eventId}")
    public String goToEditEventPage(@PathVariable("eventId") int id, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();

        //check if user is logged in
        if (session.getAttribute("user") != null) {

            Event event = eventService.getEventById(id);
            List<Category> categoryList = categoryService.getAllCategories();

            model.addAttribute("event", event);
            model.addAttribute("categories", categoryList);

            return EVENT_EDIT_VIEW;
        } else {
            model.addAttribute("message", "Access is denied!");
            return DEFAULT_ERROR_VIEW;
        }
    }


    private Event createEmptyEvent() {
        Event event = new Event();
        event.setTitle("")
                .setShortDescription("")
                .setFullDescription("")
                .setLocation("")
                .setPublicAccessed(true);

        return event;
    }
}
