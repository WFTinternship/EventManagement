package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.TestHttpServletRequest;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.EventService;
import com.workfront.internship.event_management.service.EventServiceImpl;
import org.junit.*;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.TestObjectCreator.EXISTING_KEYWORD;
import static com.workfront.internship.event_management.TestObjectCreator.createTestEvent;
import static com.workfront.internship.event_management.TestObjectCreator.createTestUser;
import static com.workfront.internship.event_management.controller.util.CongrollerConstants.HOME_VIEW;
import static com.workfront.internship.event_management.controller.util.CongrollerConstants.SEARCH_RESULTS_VIEW;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 8/31/16.
 */
public class HomeControllerUnitTest {

    private static HomeController homeController;

    private EventService eventService;
    private Event testEvent;
    private User testUser;

    private HttpServletRequest testRequest;
    private HttpSession testSession;

    @BeforeClass
    public static void setUpClass() {
        homeController = new HomeController();
    }

    @AfterClass
    public static void tearDownClass() {
        homeController = null;
    }


    @Before
    public void setUp() {
        //create test user object
        testEvent = createTestEvent();
        testUser = createTestUser();

        //create mocks
        eventService = mock(EventServiceImpl.class);
        Whitebox.setInternalState(homeController, "eventService", eventService);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);
        when(testRequest.getSession()).thenReturn(testSession);
    }

    @After
    public void tearDown() {
        //delete test objects
        testEvent = null;
        eventService = null;
    }

    @Test
    public void loadUpcomingEvents_PublicOnly_Success() {
        List<Event> testEventList = new ArrayList<>();
        testEventList.add(testEvent);

        when(testSession.getAttribute("user")).thenReturn(null);
        when(eventService.getPublicUpcomingEvents()).thenReturn(testEventList);

        //method under test
        String pageView = homeController.loadUpcomingEvents(testRequest);

        assertEquals("Incorrect redirect page", pageView, HOME_VIEW);
        verify(eventService).getPublicUpcomingEvents();
        verify(testRequest).setAttribute("events", testEventList);
    }

    @Test
    public void loadUpcomingEvents_All_Success() {
        List<Event> testEventList = new ArrayList<>();
        testEventList.add(testEvent);

        when(testSession.getAttribute("user")).thenReturn(testUser);
        when(eventService.getAllUpcomingEvents()).thenReturn(testEventList);

        //method under test
        String pageView = homeController.loadUpcomingEvents(testRequest);

        assertEquals("Incorrect redirect page", pageView, HOME_VIEW);
        verify(eventService).getAllUpcomingEvents();
        verify(testRequest).setAttribute("events", testEventList);
    }

    @Test
    public void search_AllResults() {
        List<Event> testEventList = new ArrayList<>();
        testEventList.add(testEvent);

        when(testRequest.getParameter("keyword")).thenReturn(EXISTING_KEYWORD);
        when(testSession.getAttribute("user")).thenReturn(testUser);
        when(eventService.getAllEventsByKeyword(EXISTING_KEYWORD)).thenReturn(testEventList);

        //method under test
        String pageView = homeController.search(testRequest);

        assertEquals("Incorrect redirect page", pageView, SEARCH_RESULTS_VIEW);
        verify(eventService).getAllEventsByKeyword(EXISTING_KEYWORD);
        verify(testRequest).setAttribute("events", testEventList);
    }

    @Test
    public void search_PublicResults() {
        List<Event> testEventList = new ArrayList<>();
        testEventList.add(testEvent);

        when(testRequest.getParameter("keyword")).thenReturn(EXISTING_KEYWORD);
        when(testSession.getAttribute("user")).thenReturn(null);
        when(eventService.getPublicEventsByKeyword(EXISTING_KEYWORD)).thenReturn(testEventList);

        //method under test
        String pageView = homeController.search(testRequest);

        assertEquals("Incorrect redirect page", pageView, SEARCH_RESULTS_VIEW);
        verify(eventService).getPublicEventsByKeyword(EXISTING_KEYWORD);
        verify(testRequest).setAttribute("events", testEventList);
    }
}
