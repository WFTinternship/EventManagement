package com.workfront.internship.event_management.selenium.tests;

import com.workfront.internship.event_management.selenium.pages.HomePage;
import com.workfront.internship.event_management.selenium.pages.LoginPopup;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import static com.workfront.internship.event_management.selenium.tests.TestHelper.HOME_PAGE_URL;
import static com.workfront.internship.event_management.selenium.tests.TestHelper.VALID_EMAIL;
import static com.workfront.internship.event_management.selenium.tests.TestHelper.VALID_PASSWORD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Hermine Turshujyan 8/11/16.
 */
public class HomePageTest {

    static private HomePage homePage;

    @BeforeClass
    static public void setUp() {
        homePage = new HomePage();
        homePage.init();
    }

    @AfterClass
    static public void tearDown() {
        homePage.getWebDriver().close();
        homePage = null;
    }

    @Test
    public void loginButtonClick() throws InterruptedException {
        WebElement loginPopup = homePage.clickLogin();
        Assert.assertNotNull("Login Popup is not displayed", loginPopup);
    }

    @Test
    public void logoutButtonClick() throws InterruptedException {

        homePage.clickLogin();
        LoginPopup loginPopup = new LoginPopup();
        loginPopup.typeEmail(VALID_EMAIL);
        loginPopup.typePassword(VALID_PASSWORD);
        loginPopup.clickSignin();

        Thread.sleep(2000);

        homePage.clickLogout();

        String currentUrl = homePage.getWebDriver().getCurrentUrl();
        assertEquals("", currentUrl, HOME_PAGE_URL);
        assertNotNull("login button is not displayed", homePage.getLoginPopup());

    }


}
