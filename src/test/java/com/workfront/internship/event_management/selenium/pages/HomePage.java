package com.workfront.internship.event_management.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by Hermine Turshujyan 8/11/16.
 */
public class HomePage extends AbstractPage {

    /*public HomePage() {
        //getWebDriver().get("http://localhost:8080");
    }*/

    public WebElement clickLogin() throws InterruptedException {
        WebElement loginButton = getWebDriver().findElement(By.cssSelector("#login_button"));
        loginButton.click();
        return getLoginPopup();
    }

    public void clickLogout() throws InterruptedException {
        WebElement logoutButton = getWebDriver().findElement(By.cssSelector("#logout_button"));
        logoutButton.click();
    }

    public WebElement getLoginPopup() throws InterruptedException {
        Thread.sleep(2000);
        WebElement loginPopup = getWebDriver().findElement(By.cssSelector(".login_dialog"));
        return loginPopup;
    }

    public WebElement getLogoutButton() {
        return getWebDriver().findElement(By.id("logout_button"));
    }

    public void clickAllEventsMenuItem() {
        WebElement allEventsMenuItem = getWebDriver().findElement(By.cssSelector("#all_events"));
        allEventsMenuItem.click();
    }
}
