package com.workfront.internship.event_management.selenium.tests;

import com.workfront.internship.event_management.selenium.pages.HomePage;
import com.workfront.internship.event_management.selenium.pages.LoginPopup;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.workfront.internship.event_management.selenium.TestHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Hermine Turshujyan 8/11/16.
 */
public class HomePageTest {

    static private HomePage homePage;

    @Before
    public void setUp() {
        homePage = new HomePage();
        homePage.init(HOME_PAGE_URL);
    }

    @After
    public void tearDown() {
        homePage.getWebDriver().close();
        homePage = null;
    }


    @Test
    public void logoutButtonClick() throws InterruptedException {
        homePage.clickLoginButton();

        LoginPopup loginPopup = new LoginPopup();
        loginPopup.typeEmail(EXISTING_EMAIL);
        loginPopup.typePassword(VALID_PASSWORD);
        loginPopup.clickSignin();

        WebDriverWait wait = new WebDriverWait(homePage.getWebDriver(), 15, 100);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_button")));

        homePage.clickLogout();

        String currentUrl = homePage.getWebDriver().getCurrentUrl();
        assertEquals("Page isn't redirecting properly", currentUrl, HOME_PAGE_URL);
        assertNotNull("Login button is not displayed", homePage.getLoginPopup());

    }

    @Test
    public void loginButtonClick() throws InterruptedException {
        WebElement loginPopup = homePage.clickLoginButton();
        Assert.assertNotNull("Login Popup is not displayed", loginPopup);
    }

    @Test
    public void allEventsMenuItemClick() throws InterruptedException {

        homePage.clickAllEventsMenuItem();

        WebDriverWait wait = new WebDriverWait(homePage.getWebDriver(), 15, 100);
        WebElement categoryMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cat_menu")));

        String currentUrl = homePage.getWebDriver().getCurrentUrl();
        assertEquals("Page isn't redirecting properly", EVENTS_PAGE_URL, currentUrl);
    }

}
