package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
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

import static com.workfront.internship.event_management.controller.util.PageParameters.*;

/**
 * Created by Hermine Turshujyan 8/22/16.
 */
@Controller
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class);

    // location to store file uploaded
    private static final String UPLOAD_DIRECTORY = "uploads/avatar";

    @Autowired
    private UserService userService;
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

        return "forward:/home";
    }

    @RequestMapping(value = "/registration")
    public String goToRegistrationPage() {
        return "registration";
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
                String uploadPath = servletContext.getRealPath("") + UPLOAD_DIRECTORY;
                String avatarPath = fileService.saveFile(uploadPath, image);

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
