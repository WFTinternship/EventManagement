package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.exception.service.UnauthorizedAccessException;
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
import static com.workfront.internship.event_management.controller.util.CongrollerConstants.*;
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
    private User testUser;

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
        testUser = createTestUser();
        testEvent = createTestEvent()
                .setOrganizer(testUser);

        //create test objects
//        createTestObjects();

        //creating mocks
        eventService = mock(EventServiceImpl.class);
        categoryService = mock(CategoryServiceImpl.class);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);
        when(testRequest.getSession()).thenReturn(testSession);

        //injecting mocks
        Whitebox.setInternalState(eventController, "eventService", eventService);
        Whitebox.setInternalState(eventController, "categoryService", categoryService);
    }

    @After
    public void tearDown() {
        //delete test objects
        testEventList = null;
        testCategoryList = null;
    }

    @Test
    public void loadEventsByCategory_All_Success() {
        when(testSession.getAttribute("user")).thenReturn(testUser);
        when(testRequest.getParameter("categoryId")).thenReturn("");

        //method under test
        String pageView = eventController.loadEventsByCategory(testRequest);

        assertEquals("Incorrect redirect page", EVENTS_LIST_VIEW, pageView);
        verify(testRequest).setAttribute("listHeader", ALL_EVENTS_HEADER);
        verify(eventService).getAllEvents();

    }

    @Test
    public void loadEventsByCategory_AllPublic_Success() {
        when(testSession.getAttribute("user")).thenReturn(null);
        when(testRequest.getParameter("categoryId")).thenReturn("");

        //method under test
        String pageView = eventController.loadEventsByCategory(testRequest);

        assertEquals("Incorrect redirect page", EVENTS_LIST_VIEW, pageView);
        verify(testRequest).setAttribute("listHeader", ALL_EVENTS_HEADER);
        verify(eventService).getPublicEvents();
    }

    @Test
    public void loadEventsByCategory_Success() {
        when(testSession.getAttribute("user")).thenReturn(testUser);
        when(testRequest.getParameter("categoryId")).thenReturn(Integer.toString(VALID_ID));

        //method under test
        String pageView = eventController.loadEventsByCategory(testRequest);

        assertEquals("Incorrect redirect page", EVENTS_LIST_VIEW, pageView);
        verify(testRequest).setAttribute("categoryId", Integer.toString(VALID_ID));
        verify(eventService).getAllEventsByCategory(VALID_ID);
    }

    @Test
    public void loadEventsByCategory_Public_Success() {
        when(testSession.getAttribute("user")).thenReturn(null);
        when(testRequest.getParameter("categoryId")).thenReturn(Integer.toString(VALID_ID));

        //method under test
        String pageView = eventController.loadEventsByCategory(testRequest);

        assertEquals("Incorrect redirect page", EVENTS_LIST_VIEW, pageView);
        verify(testRequest).setAttribute("categoryId", Integer.toString(VALID_ID));
        verify(eventService).getPublicEventsByCategory(VALID_ID);
    }

    @Test
    public void loadPastEvents_PublicOnly() {
        when(testSession.getAttribute("user")).thenReturn(null);

        //method under test
        String pageView = eventController.loadPastEvents(testRequest);

        assertEquals("Incorrect redirect page", EVENTS_LIST_VIEW, pageView);
        verify(testRequest).setAttribute(eq("events"), anyListOf(Event.class));
        verify(testRequest).setAttribute("listHeader", PAST_EVENTS_HEADER);
        verify(eventService).getPublicPastEvents();
    }

    @Test
    public void loadPastEvents_All() {
        when(testSession.getAttribute("user")).thenReturn(testUser);

        //method under test
        String pageView = eventController.loadPastEvents(testRequest);

        assertEquals("Incorrect redirect page", EVENTS_LIST_VIEW, pageView);
        verify(testRequest).setAttribute(eq("events"), anyListOf(Event.class));
        verify(testRequest).setAttribute("listHeader", PAST_EVENTS_HEADER);
        verify(eventService).getAllPastEvents();
    }

    @Test
    public void loadUpcomingEvents_PublicOnly() {
        when(testSession.getAttribute("user")).thenReturn(null);

        //method under test
        String pageView = eventController.loadUpcomingEvents(testRequest);

        assertEquals("Incorrect redirect page", EVENTS_LIST_VIEW, pageView);
        verify(testRequest).setAttribute(eq("events"), anyListOf(Event.class));
        verify(testRequest).setAttribute("listHeader", UPCOMING_EVENTS_HEADER);
        verify(eventService).getPublicUpcomingEvents();
    }

    @Test
    public void loadUpcomingEvents_All() {
        when(testSession.getAttribute("user")).thenReturn(testUser);

        //method under test
        String pageView = eventController.loadUpcomingEvents(testRequest);

        assertEquals("Incorrect redirect page", EVENTS_LIST_VIEW, pageView);
        verify(testRequest).setAttribute(eq("events"), anyListOf(Event.class));
        verify(testRequest).setAttribute("listHeader", UPCOMING_EVENTS_HEADER);
        verify(eventService).getAllUpcomingEvents();
    }

    @Test
    public void loadEventDetails_Success() {
        when(testSession.getAttribute("user")).thenReturn(testUser);
        when(eventService.getFullEventById(anyInt())).thenReturn(testEvent);

        //method under test
        String pageName = eventController.loadEventDetails(VALID_ID, testRequest);

        verify(testRequest).setAttribute("event", testEvent);
        assertEquals("Incorrect redirect page", pageName, EVENT_DETAILS_VIEW);
    }

    @Test
    public void loadEventDetails_Failed() {
        testEvent.setPublicAccessed(false);
        when(testSession.getAttribute("user")).thenReturn(null);
        when(eventService.getFullEventById(VALID_ID)).thenReturn(testEvent);

        //method under test
        String pageName = eventController.loadEventDetails(VALID_ID, testRequest);

        verify(testRequest, never()).setAttribute("event", testEvent);
        assertEquals("Incorrect redirect page", pageName, HOME_VIEW_REDIRECT);
    }

    @Test
    public void goToCreateEventPage_Success() {
        when(testSession.getAttribute("user")).thenReturn(testUser);
        when(categoryService.getAllCategories()).thenReturn(testCategoryList);

        //method under test
        String pageName = eventController.goToCreateEventPage(testRequest);

        verify(categoryService).getAllCategories();
        verify(eventService).createEmptyEvent();
        verify(testRequest).setAttribute("action", "create-event");
        verify(testRequest).setAttribute(eq("categories"), anyListOf(Category.class));
        verify(testRequest).setAttribute(eq("event"), any(Event.class));
        assertEquals("Incorrect redirect page", pageName, EVENT_EDIT_VIEW);
    }

    @Test
    public void goToEditEventPage_Success() {
        testEvent.setOrganizer(testUser);

        when(eventService.getFullEventById(VALID_ID)).thenReturn(testEvent);
        when(testSession.getAttribute("user")).thenReturn(testUser);

        //method under test
        String pageName = eventController.goToEditEventPage(testRequest, VALID_ID);

        verify(categoryService).getAllCategories();
        verify(testRequest).setAttribute("action", "edit-event");
        verify(testRequest).setAttribute(eq("categories"), anyListOf(Category.class));
        verify(testRequest).setAttribute(eq("event"), any(Event.class));
        assertEquals("Incorrect redirect page", pageName, EVENT_EDIT_VIEW);
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void goToCreateEventPage_UnauthorizedUser() {
        when(testSession.getAttribute("user")).thenReturn(null);

        //method under test
        eventController.goToCreateEventPage(testRequest);
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void goToEditEventPage_Failed_UnauthorizedUser() {
        when(testSession.getAttribute("user")).thenReturn(null);

        //method under test
        eventController.goToEditEventPage(testRequest, VALID_ID);
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void deleteEvent_Failed_UnauthorizedUser() {
        when(testSession.getAttribute("user")).thenReturn(null);

        //method under test
        eventController.deleteEvent(testRequest, VALID_ID);
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void deleteEvent_Failed_UnauthorizedUser_NotOrganizer() {
        User sessionUser = createTestUser().setId(VALID_ID + 1);

        when(testSession.getAttribute("user")).thenReturn(sessionUser);
        when(eventService.getEventById(anyInt())).thenReturn(testEvent);

        //method under test
        eventController.deleteEvent(testRequest, testUser.getId());
    }

    @Test
    public void deleteEvent_Success() {
        when(testSession.getAttribute("user")).thenReturn(testUser);
        when(eventService.getEventById(anyInt())).thenReturn(testEvent);
        when(eventService.deleteEvent(anyInt())).thenReturn(true);

        //method under test
        CustomResponse response = eventController.deleteEvent(testRequest, testUser.getId());
        assertEquals("Failed to delete event. Incorrect response status", ACTION_SUCCESS, response.getStatus());
    }

    @Test
    public void deleteEvent_Fail() {
        when(testSession.getAttribute("user")).thenReturn(testUser);
        when(eventService.getEventById(anyInt())).thenReturn(testEvent);
        when(eventService.deleteEvent(anyInt())).thenReturn(false);

        //method under test
        CustomResponse response = eventController.deleteEvent(testRequest, testUser.getId());
        assertEquals("Failed to delete event. Incorrect response status", ACTION_FAIL, response.getStatus());
    }
}
