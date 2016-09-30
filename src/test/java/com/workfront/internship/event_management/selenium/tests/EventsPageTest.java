package com.workfront.internship.event_management.selenium.tests;

import com.workfront.internship.event_management.selenium.pages.EventsPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.regex.Pattern;

import static com.workfront.internship.event_management.selenium.TestHelper.EVENTS_PAGE_URL;
import static com.workfront.internship.event_management.selenium.TestHelper.HOME_PAGE_URL;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
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
    public void eventDetailsButtonClicked() throws InterruptedException {
        eventsPage.clickEventDetailsButton();

        String currentUrl = eventsPage.getWebDriver().getCurrentUrl();
        WebElement elem = eventsPage.getWebDriver().findElement(By.cssSelector(".event_details_page"));

        assertNotNull(elem);
    }
}
