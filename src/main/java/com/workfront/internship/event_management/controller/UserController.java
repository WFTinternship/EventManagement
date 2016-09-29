package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.EventService;
import com.workfront.internship.event_management.service.FileService;
import com.workfront.internship.event_management.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.workfront.internship.event_management.controller.util.CongrollerConstants.*;
import static com.workfront.internship.event_management.service.util.Validator.isEmptyString;

/**
 * Created by Hermine Turshujyan 8/22/16.
 */
@Controller
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CustomResponse login(HttpServletRequest request) {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        CustomResponse result = new CustomResponse();

        try {
            User user = userService.login(email, password);

            //Save user object in session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            result.setStatus(ACTION_SUCCESS);

        } catch (InvalidObjectException | OperationFailedException e) {
            result.setStatus(ACTION_FAIL);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute("user", null);
        session.invalidate();

        return HOME_VIEW_REDIRECT;
    }

    @RequestMapping(value = "/registration")
    public String goToRegistrationPage(HttpServletRequest request) {

        if(request.getSession().getAttribute("user") != null ) {
            return HOME_VIEW_REDIRECT;
        }

        return REGISTRATION_VIEW;
    }

    @RequestMapping(value = "/my-events")
    public String goToMyEventsPage(HttpServletRequest request) {
        HttpSession session = request.getSession();

        User sessionUser = (User) session.getAttribute("user");

        if(sessionUser != null ){
            //load user related events from db and save in session
            List<Event> userOrganizedEvents = eventService.getUserOrganizedEvents(sessionUser.getId());
            request.setAttribute("userOrganizedEvents", userOrganizedEvents);

            List<Event> userInvitedEvents = eventService.getUserInvitedEvents(sessionUser.getId());
            request.setAttribute("userInvitedEvents", userInvitedEvents);

            List<Event> userAcceptedEvents = eventService.getUserEventsByResponse(sessionUser.getId(), "Yes");
            request.setAttribute("userAcceptedEvents", userAcceptedEvents);

            List<Event> userPendingEvents = eventService.getUserEventsByResponse(sessionUser.getId(), "Waiting for response");
            request.setAttribute("userPendingEvents", userPendingEvents);

            List<Event> userParticipatedEvents = eventService.getUserParticipatedEvents(sessionUser.getId());
            request.setAttribute("userParticipatedEvents", userParticipatedEvents);

            return MY_EVENTS_VIEW;
        } else {
            return HOME_VIEW_REDIRECT;
        }
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CustomResponse register(HttpServletRequest request,
                                   @RequestParam(value = "avatar", required = false) MultipartFile image) {

        CustomResponse result = new CustomResponse();
        User user = new User();

        //save uploaded file if avatar is uploaded
        if (!image.isEmpty()) {

            if (!fileService.isValidImage(image)) {
                String message = "Invalid image type!";
                logger.info(message);

                result.setStatus(ACTION_FAIL);
                result.setMessage(message);
                return result;
            }

            try {
                ServletContext servletContext = request.getSession().getServletContext();
                String webContentRoot = servletContext.getRealPath("/resources/uploads");
                String avatarPath = fileService.saveAvatar(webContentRoot, image);

                //save avatar path to user obj
                user.setAvatarPath(avatarPath);
            } catch (IOException e) {
                result.setStatus(ACTION_FAIL);
                result.setMessage("Unable to upload a file!");
                return result;
            }
        }

        //set request parameters to user
        setRequestParametersToUser(user, request);

        try {
            //insert user into db
            userService.addAccount(user);

            result.setStatus(ACTION_SUCCESS);
            result.setMessage("You are successfully registered!");
        } catch (OperationFailedException e) {
            result.setStatus(ACTION_FAIL);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/check-email", method = RequestMethod.POST)
    @ResponseBody
    public String isEmailFree(HttpServletRequest request) {

        String email = request.getParameter("email");
        User user = userService.getUserByEmail(email);

        if (user != null) {
            //already registered
            return RESPONSE_FALSE;
        } else {
            return RESPONSE_TRUE;
        }
    }

    @RequestMapping(value = "/check-invitation-email")
    @ResponseBody
    public CustomResponse findUserByEmail(HttpServletRequest request) {

        CustomResponse customResponse = new CustomResponse();

        String email = request.getParameter("email");

        if (isEmptyString(email)) {
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

    //helper methods
    private void setRequestParametersToUser(User user, HttpServletRequest request) {

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phone);
    }


}
