package com.workfront.internship.event_management.selenium.tests;

import com.workfront.internship.event_management.selenium.pages.HomePage;
import com.workfront.internship.event_management.selenium.pages.LoginPopup;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

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
        homePage.clickLogin();

        LoginPopup loginPopup = new LoginPopup();
        loginPopup.typeEmail(EXISTING_EMAIL);
        loginPopup.typePassword(VALID_PASSWORD);
        loginPopup.clickSignin();

        homePage.clickLogout();

        String currentUrl = homePage.getWebDriver().getCurrentUrl();
        assertEquals("Page isn't redirecting properly", currentUrl, HOME_PAGE_URL);
        assertNotNull("login button is not displayed", homePage.getLoginPopup());

    }

    @Test
    public void loginButtonClick() throws InterruptedException {
        WebElement loginPopup = homePage.clickLogin();
        Assert.assertNotNull("Login Popup is not displayed", loginPopup);
    }

    @Test
    public void allEventsMenuItemClick() throws InterruptedException {

        homePage.clickAllEventsMenuItem();

        String currentUrl = homePage.getWebDriver().getCurrentUrl();
        assertEquals("Page isn't redirecting properly", currentUrl, EVENTS_PAGE_URL);
    }

}
