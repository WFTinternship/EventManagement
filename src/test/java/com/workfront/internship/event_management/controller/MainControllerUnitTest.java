package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.service.EventService;
import com.workfront.internship.event_management.service.EventServiceImpl;
import org.junit.*;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.TestObjectCreator.createTestEvent;
import static com.workfront.internship.event_management.controller.util.PageParameters.HOME_VIEW;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hermine Turshujyan 8/31/16.
 */
public class MainControllerUnitTest {

    private static MainController mainController;
    private EventService eventService;

    private Event testEvent;
    private HttpServletRequest testRequest;
    private HttpSession testSession;
    private Model testModel;

    @BeforeClass
    public static void setUpClass() {
        mainController = new MainController();
    }

    @AfterClass
    public static void tearDownClass() {
        mainController = null;
    }


    @Before
    public void setUp() {
        //create test user object
        testEvent = createTestEvent();

        //create mocks
        eventService = mock(EventServiceImpl.class);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);
        testModel = mock(Model.class);

        //inject mocks
        Whitebox.setInternalState(mainController, "eventService", eventService);
    }

    @After
    public void tearDown() {
        //delete test user object
        testEvent = null;

        eventService = null;
    }

    @Test
    public void loadUpcomingEvents_Success() {
        List<Event> testEventList = new ArrayList<>();
        testEventList.add(testEvent);

        when(eventService.getAllEvents()).thenReturn(testEventList);

        //method under test
        String pageView = mainController.loadUpcomingEvents(testModel);

        verify(testModel).addAttribute("events", testEventList);
        assertEquals(pageView, HOME_VIEW);
    }
}
