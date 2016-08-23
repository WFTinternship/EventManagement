package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.service.CategoryService;
import com.workfront.internship.event_management.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

        return "events";
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
        // String eventsJson = new Gson().toJson(eventList);
        // return eventsJson;


        JsonResponse result = new JsonResponse();

        result.setStatus("SUCCESS");

        result.setResult(eventList);

        return result;

        //  List<Category> categoryList = categoryService.getAllCategories();
        //String categoriesJson = new Gson().toJson(categoryList);
        //  String json = "[" + eventsJson+ "," + categoriesJson + "]";


        //return new JsonObject();
    }


    @RequestMapping(value = "/create-event")
    public String createEvent() {
        return "create-event";
    }
}
