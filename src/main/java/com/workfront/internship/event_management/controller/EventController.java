package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.controller.util.DateParser;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.CategoryService;
import com.workfront.internship.event_management.service.EventService;
import com.workfront.internship.event_management.service.InvitationService;
import com.workfront.internship.event_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
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
    private InvitationService invitationService;
    @Autowired
    private UserService userService;
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

        String title = request.getParameter("eventTitle");
        String shortDescription = request.getParameter("shortDesc");
        String fullDescription = request.getParameter("fullDesc");
        String location = request.getParameter("location");
        String invitationsString = request.getParameter("invitations");

        //get invitations list
        List<Invitation> invitations = new ArrayList<>();
        List<String> invitationEmails = Arrays.asList(invitationsString.split(","));
        for (String email : invitationEmails) {
            Invitation invitation = invitationService.createInvitationForEmail(email);
            invitations.add(invitation);
        }

        //get category
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        Category category = new Category().setId(categoryId);

        boolean publicAccessed = Boolean.parseBoolean(request.getParameter("publicAccessed"));
        boolean guestsAllowed = Boolean.parseBoolean(request.getParameter("guestsAllowed"));

        String startDateString = request.getParameter("startDate");
        String endDateString = request.getParameter("endDate");
        String startTimeString = request.getParameter("startTime");
        String endTimeString = request.getParameter("endTime");


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
                .setEndDate(endDate)
                .setInvitations(invitations);

        eventService.createEvent(event);

        return ALL_EVENTS_VIEW;
    }

    @RequestMapping(value = "/check-invitation-email")
    @ResponseBody
    public CustomResponse isRegisteredUser(HttpServletRequest request) {

        CustomResponse customResponse = new CustomResponse();

        String email = request.getParameter("email");

        if (email.isEmpty()) {
            customResponse.setStatus(ACTION_FAIL);
            customResponse.setMessage("Empty email");

            return customResponse;
        }

        List<User> users = userService.getUsersMatchingEmail(email);
        if (users != null && !users.isEmpty()) {
            customResponse.setStatus(ACTION_FOUND);
            customResponse.setResult(users);
        } else {
            customResponse.setStatus(ACTION_NOT_FOUND);
        }

        return customResponse;
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
