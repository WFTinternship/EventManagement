package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.service.CategoryService;
import com.workfront.internship.event_management.service.EventService;
import org.junit.*;
import org.mockito.internal.util.reflection.Whitebox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.workfront.internship.event_management.TestObjectCreator.createTestCategory;
import static com.workfront.internship.event_management.TestObjectCreator.createTestEvent;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Hermine Turshujyan 8/30/16.
 */
public class EventControllerUnitTest {

    private static UserController userController;

    private EventService eventService;
    private CategoryService categoryService;

    private List<Category> testCategoryList;

    private List<Event> testEventList;

    private HttpServletRequest testRequest;
    private HttpSession testSession;

    @BeforeClass
    public static void setUpClass() {
        userController = new UserController();
    }

    @AfterClass
    public static void tearDownClass() {
        userController = null;
    }


    @Before
    public void setUp() {
        //create test objects
        Category testCategory = createTestCategory();
        testCategoryList.add(testCategory);

        Event testEvent = createTestEvent();
        testEventList.add(testEvent);

        //creating mocks
        eventService = mock(EventService.class);
        categoryService = mock(CategoryService.class);

        //injecting mocks
        Whitebox.setInternalState(userController, "eventService", eventService);
        Whitebox.setInternalState(userController, "categoryService", categoryService);
    }

    @After
    public void tearDown() {
        //delete test objects
        testEventList = null;
        testCategoryList = null;
    }

    @Test
    public void loadAllEvents() {
        when(categoryService.getAllCategories()).thenReturn(testCategoryList);
        when(eventService.getAllEvents()).thenReturn(testEventList);
    }


}
