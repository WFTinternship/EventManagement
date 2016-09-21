package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.common.DateParser;
import com.workfront.internship.event_management.exception.service.UnauthorizedAccessException;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.workfront.internship.event_management.controller.util.PageParameters.*;
import static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection;
import static com.workfront.internship.event_management.service.util.Validator.isEmptyString;

/**
 * Created by Hermine Turshujyan 8/22/16.
 */
@Controller
public class EventController {

    private static final Logger logger = Logger.getLogger(EventController.class);

    private static final String EVENT_FILE_UPLOAD_DIRECTORY = "uploads/events/files";
    private static final String EVENT_IMAGE_UPLOAD_DIRECTORY = "uploads/events/images";

    @Autowired
    private EventService eventService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;


    @RequestMapping(value = "/events")
    public String loadAllEventsAndCategories(HttpServletRequest request, Model model) {
        List<Category> categoryList = categoryService.getAllCategories();

        User sessionUser = (User) request.getSession().getAttribute("user");
        List<Event> eventList;
        if (sessionUser == null){
            eventList = eventService.getPublicEvents();
        } else {
            eventList = eventService.getAllEvents();
        }

        model.addAttribute("events", eventList);
        model.addAttribute("categories", categoryList);
        model.addAttribute("list-name", ALL_EVENTS_HEADER);

        return EVENTS_LIST_VIEW;
    }

    @RequestMapping(value = "/events-ajax")
    @ResponseBody
    public CustomResponse loadEventsByCategory(HttpServletRequest request) {

        String categoryIdStr = request.getParameter("categoryId");
        List<Event> eventList = eventService.getEventsByCategory(Integer.parseInt(categoryIdStr));

        CustomResponse result = new CustomResponse();
        result.setStatus(ACTION_SUCCESS);
        result.setResult(eventList);

        return result;
    }

    @RequestMapping(value = "/past-events")
    public String loadPastEvents(HttpServletRequest request, Model model) {

        List<Event> eventList;
        User sessionUser = (User) request.getSession().getAttribute("user");

        if (sessionUser == null){
            eventList = eventService.getPublicPastEvents();
        } else {
            eventList = eventService.getAllPastEvents();
        }

        model.addAttribute("events", eventList);
        model.addAttribute("list-name", PAST_EVENTS_HEADER);

        return EVENTS_LIST_VIEW;
    }

    @RequestMapping(value = "/upcoming-events")
    public String loadUpcomingEvents(HttpServletRequest request, Model model) {
        List<Event> eventList;
        User sessionUser = (User) request.getSession().getAttribute("user");

        if (sessionUser == null){
            eventList = eventService.getPublicUpcomingEvents();
        } else {
            eventList = eventService.getAllUpcomingEvents();
        }
        model.addAttribute("events", eventList);
        model.addAttribute("list-name", UPCOMING_EVENTS_HEADER);

        return EVENTS_LIST_VIEW;
    }

    @GetMapping(value = "/events/{eventId}")
    public String getEventDetails(@PathVariable("eventId") int id, Model model) {
        Event event = eventService.getFullEventById(id);
        model.addAttribute("event", event);

        return EVENT_DETAILS_VIEW;
    }

    @GetMapping(value = "/events/{eventId}/invitation-respond")
    public String goToRespondToEventPage(@PathVariable("eventId") int eventId, Model model, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS_MESSAGE);
        }

        int userId = Integer.parseInt(request.getParameter("user"));
        if (sessionUser.getId() != userId) {
// TODO: 9/21/16 redirect to login

        }

        //if coming from email
        int responseId = Integer.parseInt(request.getParameter("response"));

        //update invitation response in db
        boolean updated = invitationService.respondToInvitation(eventId, userId, responseId);

        //load event with invitations
        Event event = eventService.getEventById(eventId);
        List<Invitation> invitations = invitationService.getInvitationsByEvent(eventId);
        if (!isEmptyCollection(invitations)) {
            event.setInvitations(invitations);
        }

        model.addAttribute("event", event);
        model.addAttribute("action", "invitation-responded");
        return EVENT_DETAILS_VIEW;
    }

    @GetMapping(value = "/events/{eventId}/respond")
    public String respondToEvent(@PathVariable("eventId") int eventId, @PathVariable("userId") int userId,
                                 Model model, HttpServletRequest request) {
        // TODO: 9/16/16 implement 
        return null;

    }

    @RequestMapping(value = "/new-event")
    public String goToCreateEventPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        //check if user is logged in
        if (session.getAttribute("user") == null) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS_MESSAGE);
        }

        List<Category> categoryList = categoryService.getAllCategories();

        model.addAttribute("categories", categoryList);
        model.addAttribute("event", createEmptyEvent());

        return EVENT_EDIT_VIEW;
    }

    @RequestMapping(value = "/events/{eventId}/edit")
    public String goToEditEventPage(HttpServletRequest request, Model model, @PathVariable("eventId") int eventId) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        //check if user is logged in
        if (sessionUser == null) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS_MESSAGE);
        }

        Event event = eventService.getFullEventById(eventId);
        if(event.getOrganizer().getId() != sessionUser.getId()){
            throw new UnauthorizedAccessException(OPERATION_NOT_ALLOWED_MESSAGE);
        }

        List<Category> categoryList = categoryService.getAllCategories();
        model.addAttribute("categories", categoryList);
        model.addAttribute("event", event);

        return EVENT_EDIT_VIEW;
    }

    @RequestMapping(value = "/events/{eventId}/delete")
    @ResponseBody
    public CustomResponse deleteEvent(HttpServletRequest request, Model model, @PathVariable("eventId") int eventId) {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        //check if user is logged in
        if (sessionUser == null) {
            throw new UnauthorizedAccessException(OPERATION_NOT_ALLOWED_MESSAGE);
        }

        Event event = eventService.getEventById(eventId);
        if(event.getOrganizer().getId() != sessionUser.getId()){
            throw new UnauthorizedAccessException(OPERATION_NOT_ALLOWED_MESSAGE);
        }

        CustomResponse response = new CustomResponse();
        boolean success = eventService.deleteEvent(eventId);
        if(success){
            List<Event> updatedOrganizedEventsList = eventService.getUserOrganizedEvents(sessionUser.getId());
            session.setAttribute("userOrganizedEvents", updatedOrganizedEventsList);
            response.setStatus(ACTION_SUCCESS);
        } else {
            logger.warn(OPERATION_FAILED_MESSAGE);

            response.setStatus(ACTION_FAIL);
            response.setMessage(OPERATION_FAILED_MESSAGE);
        }

        return response;
    }

    @RequestMapping(value = "/add-event", method = RequestMethod.POST)
    @ResponseBody
    public CustomResponse addEvent(HttpServletRequest request,
                                   @RequestParam(value = "eventImage", required = false) MultipartFile image,
                                   @RequestParam(value = "eventFile", required = false) MultipartFile file) {

        CustomResponse result = new CustomResponse();

        //check if user is logged in
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            logger.warn(UNAUTHORIZED_ACCESS_MESSAGE);

            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS_MESSAGE);
        }

        Event event = new Event();

        String title = request.getParameter("eventTitle");
        String shortDescription = request.getParameter("shortDesc");
        String fullDescription = request.getParameter("fullDesc");
        String location = request.getParameter("location");
        String invitationsString = request.getParameter("invitations");

        List<Invitation> invitations = new ArrayList<>();
        if(!isEmptyString(invitationsString)) {

            //get invitees email list
            List<String> invitationEmails = Arrays.asList(invitationsString.split(","));

            for (String email : invitationEmails) {
                Invitation invitation = invitationService.createInvitationForMember(email);
                invitations.add(invitation);
            }
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
                .setOrganizer(sessionUser)
                .setLocation(location)
                .setPublicAccessed(publicAccessed)
                .setGuestsAllowed(guestsAllowed)
                .setCategory(category)
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setInvitations(invitations);

        //saving event image (if uploaded)
        if (!image.isEmpty()) {

            if (!fileService.isValidImage(image)) {
                String message = "Invalid image type!";
                logger.info(message);

                result.setStatus(ACTION_FAIL);
                result.setMessage(message);
                return result;
            }

            try {
                ServletContext context = request.getSession().getServletContext();
                String webContentRoot = context.getRealPath("/resources/uploads");
                String imagePath = fileService.saveEventImage(webContentRoot, image);

                event.setImageName(imagePath);
            } catch (IOException e) {
                result.setStatus(ACTION_FAIL);
                result.setMessage("Unable to upload an image!");
                return result;
            }
        }

        //saving event file (if uploaded)
        if (!file.isEmpty()) {

            if (!fileService.isValidFile(file)) {
                String message = "Invalid file type!";
                logger.info(message);

                result.setStatus(ACTION_FAIL);
                result.setMessage(message);
                return result;
            }

            try {
                ServletContext context = request.getSession().getServletContext();
                String webContentRoot = context.getRealPath("/resources/uploads");
                String filePath = fileService.saveEventFile(webContentRoot, file);
                event.setFileName(filePath);
            } catch (IOException e) {
                result.setStatus(ACTION_FAIL);
                result.setMessage("Unable to upload a file!");
                return result;
            }
        }

        eventService.createEvent(event);

        //update logged in user's organized events in session
        List<Event> userOrganizedEvents = (List<Event>) session.getAttribute("userOrganizedEvents");
        userOrganizedEvents.add(event);
        session.setAttribute("userOrganizedEvents", userOrganizedEvents); // TODO: 9/21/16

        emailService.sendInvitations(event);

        return result;
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

    // TODO: 9/21/16 move to service
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
