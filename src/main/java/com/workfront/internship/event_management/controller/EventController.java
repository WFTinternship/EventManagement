package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.common.DateParser;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.exception.service.UnauthorizedAccessException;
import com.workfront.internship.event_management.model.*;
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
import static com.workfront.internship.event_management.service.util.Validator.isEmptyString;

/**
 * Created by Hermine Turshujyan 8/22/16.
 */
@Controller
public class EventController {

    private static final Logger logger = Logger.getLogger(EventController.class);

    @Autowired
    private EventService eventService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private MediaService mediaService;


    @RequestMapping(value = "/events")
    public String loadEventsByCategory(Model model, HttpServletRequest request) {

        User sessionUser = (User) request.getSession().getAttribute("user");
        String categoryIdStr = request.getParameter("categoryId");

        List<Event> eventList;

        if(isEmptyString(categoryIdStr)) { //load all events
            model.addAttribute("listHeader", ALL_EVENTS_HEADER);

            if (sessionUser == null){
                eventList = eventService.getPublicEvents();
            } else {
                eventList = eventService.getAllEvents();
            }
        } else {
            //load events by category
            model.addAttribute("categoryId", categoryIdStr);

            int categoryId = Integer.parseInt(categoryIdStr);
            if (sessionUser == null){
                eventList = eventService.getPublicEventsByCategory(categoryId);
            } else {
                eventList = eventService.getAllEventsByCategory(categoryId);
            }
        }

        //load all categories for left side menu
        List<Category> categoryList = categoryService.getAllCategories();

        model.addAttribute("events", eventList);
        model.addAttribute("categories", categoryList);
        return EVENTS_LIST_VIEW;
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
        model.addAttribute("listHeader", PAST_EVENTS_HEADER);

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
        model.addAttribute("listHeader", UPCOMING_EVENTS_HEADER);

        return EVENTS_LIST_VIEW;
    }

    @GetMapping(value = "/events/{eventId}")
    public String getEventDetails(@PathVariable("eventId") int id, Model model, HttpServletRequest request) {

        Event event = eventService.getFullEventById(id);

        //check if user is not authorized to view this event, redirect to home page
        User sessionUser = (User) request.getSession().getAttribute("user");
        if(!event.isPublicAccessed() && sessionUser == null) {
            return HOME_VIEW;
        }

        model.addAttribute("event", event);

        return EVENT_DETAILS_VIEW;
    }


    @RequestMapping(value = "/new-event")
    public String goToCreateEventPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        //check if user is logged in
        if (session.getAttribute("user") == null) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS_MESSAGE);
        }

        List<Category> categoryList = categoryService.getAllCategories();

        model.addAttribute("action", "create-event");
        model.addAttribute("categories", categoryList);
        model.addAttribute("event", eventService.createEmptyEvent());

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

        model.addAttribute("action", "edit-event");
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


    @RequestMapping(value = "/save-event", method = RequestMethod.POST)
    @ResponseBody
    public CustomResponse saveEvent(HttpServletRequest request,
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
        String action = request.getParameter("action");

        //if action is ADD EVENT, eventId would be 0
        int eventId = Integer.parseInt(request.getParameter("eventId"));
        event.setId(eventId);

        String title = request.getParameter("eventTitle");
        String shortDescription = request.getParameter("shortDesc");
        String fullDescription = request.getParameter("fullDesc");

        String location = request.getParameter("location");
        double longitude = Double.parseDouble(request.getParameter("lng"));
        double latitude = Double.parseDouble(request.getParameter("lat"));
        String invitationsString = request.getParameter("invitations");

        List<Invitation> invitations = new ArrayList<>();
        if(!isEmptyString(invitationsString)) {
            //get invitees email list
            List<String> invitationEmails = Arrays.asList(invitationsString.split(","));

            for (String email : invitationEmails) {
                Invitation invitation = invitationService.createInvitation(email);
                invitation.setEventId(eventId);
                invitations.add(invitation);
            }
        }

        //get category
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        Category category = new Category().setId(categoryId);

        boolean publicAccessed = (request.getParameter("publicAccessed")).equals("1");
        boolean guestsAllowed = (request.getParameter("guestsAllowed")).equals("1");

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
                .setLat(latitude)
                .setLng(longitude)
                .setPublicAccessed(publicAccessed)
                .setGuestsAllowed(guestsAllowed)
                .setCategory(category)
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setInvitations(invitations);

        //saving event image (if uploaded)
        if (image!= null && !image.isEmpty()) {

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

        if(action.equals("edit")) {
            String imageName = request.getParameter("imageName");

            //save same image name in db if image is not changed in edit page
            if (imageName != null) {
                event.setImageName(imageName);
            }
        }

        //saving event file (if uploaded)
        if (file!= null && !file.isEmpty()) {

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

        if (action.equals("create")) {
            eventService.createEvent(event);
        } else if (action.equals("edit")) {
            eventService.editEvent(event);
        } else {
            throw new OperationFailedException("Unknown action");
        }

        //update logged in user's organized events in session
        List<Event> userOrganizedEvents = eventService.getUserOrganizedEvents(sessionUser.getId());
        session.setAttribute("userOrganizedEvents", userOrganizedEvents);

        result.setStatus(ACTION_SUCCESS);
        return result;
    }

    @RequestMapping(value="/upload-photos", method=RequestMethod.POST)
    @ResponseBody
    public CustomResponse handleFileUpload(
            @RequestParam("eventImages") MultipartFile[] images,
            HttpServletRequest request) {

        CustomResponse response = new CustomResponse();

        User sessionUser = (User) request.getSession().getAttribute("user");

        if(sessionUser == null){
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS_MESSAGE);
        }

        int userId = sessionUser.getId();
        int eventId = Integer.parseInt(request.getParameter("eventId"));

        ServletContext context = request.getSession().getServletContext();
        String webContentRoot = context.getRealPath("/resources/uploads");
            try {
                List<String> photoNames = fileService.saveEventPhotos(webContentRoot, images, eventId, userId);

                List<Media> mediaList = new ArrayList<>();
                for(String photoName: photoNames){

                    Media media = new Media();
                    media.setEventId(eventId)
                            .setUploader(sessionUser)
                            .setType(new MediaType(1, "image"))
                            .setName(photoName)
                            .setUploadDate(new Date());

                    mediaList.add(media);
                }

                mediaService.addMediaList(mediaList);
                response.setMessage(ACTION_SUCCESS);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return response;
    }



}
