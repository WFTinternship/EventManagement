package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Hermine Turshujyan 8/22/16.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/login", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse login(Model model, HttpServletRequest request) {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        //JsonObject result = new JsonObject();
        JsonResponse result = new JsonResponse();

        try {
            User user = userService.login(email, password);

            //Save user object in session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            result.setStatus("SUCCESS");
        } catch (OperationFailedException e) {
            result.setStatus("FAIL");
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute("user", null);
        session.invalidate();

        return "forward:/home";
    }

    @RequestMapping(value = "/registration")
    public String registration() {
        return "registration";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, HttpServletRequest request) {


        return "index";
    }

    @RequestMapping(value = "/check-email", method = RequestMethod.POST)
    @ResponseBody
    public String checkEmail(Model model, HttpServletRequest request) {

        JsonResponse result = new JsonResponse();

        String email = request.getParameter("email");
        User user = userService.getUserByEmail(email);

        if (user != null) {
            //already registered
            return "false";
        } else {
            return "true";
        }
    }
}
