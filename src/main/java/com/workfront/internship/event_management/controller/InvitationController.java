package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.exception.service.UnauthorizedAccessException;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.workfront.internship.event_management.controller.util.PageParameters.*;
import static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection;

/**
 * Created by hermineturshujyan on 9/27/16.
 */
@Controller
public class InvitationController {

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
    @GetMapping(value = "/events/{eventId}/invitation-respond")
    public String goToRespondToEventPage(@PathVariable("eventId") int eventId, Model model, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("user");

        if (sessionUser == null) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS_MESSAGE);
        }

        int userId = Integer.parseInt(request.getParameter("user"));
        if (sessionUser.getId() != userId) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS_MESSAGE);
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
    @ResponseBody
    public CustomResponse respondToEvent(@PathVariable("eventId") int eventId, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS_MESSAGE);
        }

        //if coming from email
        int responseId = Integer.parseInt(request.getParameter("response"));

        //update invitation response in db
        boolean updated = invitationService.respondToInvitation(eventId, sessionUser.getId(), responseId);

        CustomResponse response = new CustomResponse();
        if (updated) {
            response.setStatus(ACTION_SUCCESS);
        } else {
            response.setStatus(ACTION_FAIL);
            response.setMessage("Could not update invitation response!");
        }

        return response;
    }
}
