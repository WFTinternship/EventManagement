package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.CategoryService;
import com.workfront.internship.event_management.service.CategoryServiceImpl;
import com.workfront.internship.event_management.service.EventService;
import com.workfront.internship.event_management.service.EventServiceImpl;
import org.junit.*;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.TestObjectCreator.*;
import static com.workfront.internship.event_management.controller.util.PageParameters.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 8/30/16.
 */
public class EventControllerUnitTest {

    private static EventController eventController;

    private EventService eventService;
    private CategoryService categoryService;

    private List<Category> testCategoryList;
    private List<Event> testEventList;
    private Event testEvent;

    private Model testModel;
    private HttpServletRequest testRequest;
    private HttpSession testSession;

    @BeforeClass
    public static void setUpClass() {
        eventController = new EventController();
    }

    @AfterClass
    public static void tearDownClass() {
        eventController = null;
    }


    @Before
    public void setUp() {

        //create test objects
        createTestObjects();

        //creating mocks
        testModel = mock(Model.class);
        eventService = mock(EventServiceImpl.class);
        categoryService = mock(CategoryServiceImpl.class);
        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);

        //injecting mocks
        Whitebox.setInternalState(eventController, "eventService", eventService);
        Whitebox.setInternalState(eventController, "categoryService", categoryService);

        when(testRequest.getSession()).thenReturn(testSession);
    }

    @After
    public void tearDown() {
        //delete test objects
        testEventList = null;
        testCategoryList = null;
    }

    @Test
    public void loadAllEventsAndCategories_Success() {
        when(categoryService.getAllCategories()).thenReturn(testCategoryList);
        when(eventService.getAllEvents()).thenReturn(testEventList);

        String pageName = eventController.loadAllEventsAndCategories(testRequest, testModel);

        verify(testModel).addAttribute("events", testEventList);
        assertEquals("Incorrect redirect page", pageName, EVENTS_LIST_VIEW);
    }

    @Test
    public void getEvent_Success() {

        when(eventService.getEventById(VALID_ID)).thenReturn(testEvent);

        //method under test
        String pageName = eventController.getEventDetails(VALID_ID, testModel);
        verify(eventService).getEventById(VALID_ID);

        verify(testModel).addAttribute("event", testEvent);
        assertEquals("Incorrect redirect page", pageName, EVENT_DETAILS_VIEW);
    }

    @Test
    public void goToCreateEventPage_Success() {
        when(testSession.getAttribute("user")).thenReturn(new User());
        when(categoryService.getAllCategories()).thenReturn(testCategoryList);

        //method under test
        String pageName = eventController.goToCreateEventPage(testRequest, testModel);

        verify(categoryService).getAllCategories();
        verify(testModel).addAttribute("categories", testCategoryList);
        verify(testModel).addAttribute(eq("event"), any(Event.class));
        assertEquals("Incorrect redirect page", pageName, EVENT_EDIT_VIEW);
    }

    @Test
    public void goToEditEventPage_Fail_Unauthorized() {
        when(testSession.getAttribute("user")).thenReturn(null);

        //method under test
        String pageName = eventController.goToEditEventPage(testRequest, testModel, VALID_ID);

        assertEquals("Incorrect redirect page", pageName, DEFAULT_ERROR_VIEW);
    }

    @Test
    public void goToEditEventPage_Success() {
        when(testSession.getAttribute("user")).thenReturn(new User());
        when(eventService.getEventById(VALID_ID)).thenReturn(testEvent);

        when(categoryService.getAllCategories()).thenReturn(testCategoryList);

        //method under test
        String pageName = eventController.goToEditEventPage(testRequest, testModel, VALID_ID);

        verify(categoryService).getAllCategories();
        verify(testModel).addAttribute("categories", testCategoryList);
        verify(testModel).addAttribute("event", testEvent);
        assertEquals("Incorrect redirect page", pageName, EVENT_EDIT_VIEW);
    }

    @Test
    public void goToCreateEventPage_Fail_Unauthorized() {
        when(testSession.getAttribute("user")).thenReturn(null);

        //method under test
        String pageName = eventController.goToCreateEventPage(testRequest, testModel);

        assertEquals("Incorrect redirect page", pageName, DEFAULT_ERROR_VIEW);
    }


    @Test
    public void loadEvents_ByCategory() {
        when(testRequest.getParameter("categoryId")).thenReturn(String.valueOf(VALID_ID));

        //method under test
        CustomResponse result = eventController.loadEventsByCategory(testRequest);

        verify(eventService).getEventsByCategory(VALID_ID);
        assertEquals("Status is incorrect", result.getStatus(), ACTION_SUCCESS);
    }

    @Test
    public void loadEvents_All() {
        when(testRequest.getParameter("categoryId")).thenReturn(null);

        //method under test
        CustomResponse result = eventController.loadEventsByCategory(testRequest);

        verify(eventService).getAllEvents();
        assertEquals("Status is incorrect", result.getStatus(), ACTION_SUCCESS);
    }


    //helper methods
    private void createTestObjects() {
        testCategoryList = new ArrayList<>();
        Category testCategory = createTestCategory();
        testCategoryList.add(testCategory);

        testEventList = new ArrayList<>();
        testEvent = createTestEvent();
        testEventList.add(testEvent);
    }

}
