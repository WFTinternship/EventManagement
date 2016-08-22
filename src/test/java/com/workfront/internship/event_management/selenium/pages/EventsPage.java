package com.workfront.internship.event_management.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.workfront.internship.event_management.selenium.TestHelper.EVENTS_PAGE_URL;

/**
 * Created by Hermine Turshujyan 8/15/16.
 */
public class EventsPage extends AbstractPage {


    public void redirectToEventsPage() {
        redirectToPage(EVENTS_PAGE_URL);
    }

    public void clickCategory() throws InterruptedException {
        WebElement category = getWebDriver().findElement(By.cssSelector(".cat_item"));
        category.click();
    }

    public WebElement getAllEventsHeader() throws InterruptedException {
        return getWebDriver().findElement(By.cssSelector(".list_header"));
    }

}
