package com.workfront.internship.event_management.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.workfront.internship.event_management.selenium.TestHelper.HOME_PAGE_URL;

/**
 * Created by Hermine Turshujyan 8/11/16.
 */
public class HomePage extends AbstractPage {

    public void redirectToHomePage() {
        redirectToPage(HOME_PAGE_URL);
    }

    public WebElement clickLogin() throws InterruptedException {
        WebElement loginButton = getWebDriver().findElement(By.id("login_button"));
        loginButton.click();
        return getLoginPopup();
    }

    //actions
    public void clickLogout() throws InterruptedException {
        WebElement logoutButton = getWebDriver().findElement(By.cssSelector("#logout_button"));
        logoutButton.click();
    }

    public void clickAllEventsMenuItem() {
        WebElement allEventsMenuItem = getWebDriver().findElement(By.cssSelector("#all_events"));
        allEventsMenuItem.click();
    }

    //get elements
    public WebElement getLoginPopup() throws InterruptedException {
        Thread.sleep(2000);
        WebElement loginPopup = getWebDriver().findElement(By.cssSelector(".login_dialog"));
        return loginPopup;
    }

    public WebElement getSlider() throws InterruptedException {
        return getWebDriver().findElement(By.className("flexslider"));
    }

    public WebElement getLogoutButton() {
        return getWebDriver().findElement(By.id("logout_button"));
    }


}
