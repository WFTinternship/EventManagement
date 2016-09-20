package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.controller.util.DateParser;
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
    public String getEventDetails(@PathVariable("eventId") int id, Model model) {
        Event event = eventService.getFullEventById(id);
        model.addAttribute("event", event);

        return EVENT_DETAILS_VIEW;
    }

    @GetMapping(value = "/events/{eventId}/invitation-respond")
    public String goToRespondToEventPage(@PathVariable("eventId") int eventId, Model model, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            // TODO: 9/16/16 redirect to login page
            return null;
        } else {
            int userId = Integer.parseInt(request.getParameter("user"));
            if (sessionUser.getId() != userId) {
                throw new UnauthorizedAccessException("Unauthorized access");
                // TODO: 9/16/16 implement
            }

            //if comming from email
            int responseId = Integer.parseInt(request.getParameter("response"));
            // TODO: 9/19/16 check if is integer

            //update invitation response in db
            invitationService.respondToInvitation(eventId, userId, responseId);

            //load event with invitations
            Event event = eventService.getEventById(eventId);
            List<Invitation> invitations = invitationService.getInvitationsByEvent(eventId);
            if (!isEmptyCollection(invitations)) {
                event.setInvitations(invitations);
            }

            model.addAttribute("event", event);
            model.addAttribute("action", "invitation-respond");
            return EVENT_DETAILS_VIEW;

        }
    }

    @GetMapping(value = "/events/{eventId}/invitation-respond/{userId}")
    public String respondToEvent(@PathVariable("eventId") int eventId, @PathVariable("userId") int userId,
                                 Model model, HttpServletRequest request) {
        // TODO: 9/16/16 implement 
        return null;

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
    @ResponseBody
    public CustomResponse addEvent(HttpServletRequest request,
                                   @RequestParam(value = "eventImage", required = false) MultipartFile image,
                                   @RequestParam(value = "eventFile", required = false) MultipartFile file) {

        CustomResponse result = new CustomResponse();

        //check if user is logged in
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            String message = "You are not authorized to per-form this action!";
            logger.info(message);

            result.setStatus(ACTION_FAIL);
            result.setMessage(message);
            return result; // TODO: 9/13/16 throw exception 
        }

        Event event = new Event();

        String title = request.getParameter("eventTitle");
        String shortDescription = request.getParameter("shortDesc");
        String fullDescription = request.getParameter("fullDesc");
        String location = request.getParameter("location");
        String invitationsString = request.getParameter("invitations");

        List<Invitation> invitations = new ArrayList<>();
        if(invitationsString != null) {

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
