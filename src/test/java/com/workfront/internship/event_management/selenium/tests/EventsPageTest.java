package com.workfront.internship.event_management.selenium.tests;

import com.workfront.internship.event_management.selenium.pages.EventsPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.workfront.internship.event_management.selenium.TestHelper.EVENTS_PAGE_URL;
import static org.junit.Assert.assertFalse;

/**
 * Created by Hermine Turshujyan 8/16/16.
 */
public class EventsPageTest {

    static private EventsPage eventsPage;

    @BeforeClass
    static public void setUp() {

        eventsPage = new EventsPage();
        eventsPage.init(EVENTS_PAGE_URL);
    }

    @AfterClass
    static public void tearDown() {
        eventsPage.getWebDriver().close();
        eventsPage = null;
    }

    @Test
    public void categoryClicked() throws InterruptedException {
        eventsPage.clickCategory();

        assertFalse("List is not updates", eventsPage.getAllEventsHeader().isDisplayed());
    }
}
