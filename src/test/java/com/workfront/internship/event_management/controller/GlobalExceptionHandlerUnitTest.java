package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import org.junit.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static com.workfront.internship.event_management.TestObjectCreator.EXCEPTION_MESSAGE;
import static com.workfront.internship.event_management.controller.util.PageParameters.DEFAULT_ERROR_VIEW;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Created by Hermine Turshujyan 8/31/16.
 */
public class GlobalExceptionHandlerUnitTest {

    private static GlobalExceptionHandler exceptionHandler;

    private ModelAndView modelAndViewMock;

    @BeforeClass
    public static void setUpClass() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @AfterClass
    public static void tearDownClass() {
        exceptionHandler = null;
    }


    @Before
    public void setUp() {
        //create mocks
        modelAndViewMock = mock(ModelAndView.class);
    }

    @After
    public void tearDown() {
    }

    @Ignore
    @Test
    public void handleInvalidObjectException() {
        ModelAndView modelAndView = exceptionHandler.handleInvalidObjectException(any(HttpServletRequest.class),
                new InvalidObjectException(EXCEPTION_MESSAGE));
        assertEquals(modelAndView.getViewName(), DEFAULT_ERROR_VIEW);
        assertEquals(modelAndView.getModel().get("message"), EXCEPTION_MESSAGE);
    }

    @Ignore
    @Test
    public void handleObjectNotFoundException() {

    }

    @Ignore
    @Test
    public void handleDAOException() {

    }

    @Ignore
    @Test
    public void handleOtherException() {

    }

}