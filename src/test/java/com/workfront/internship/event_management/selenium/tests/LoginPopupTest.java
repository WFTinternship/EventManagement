package com.workfront.internship.event_management.selenium.tests;

import com.workfront.internship.event_management.selenium.pages.HomePage;
import com.workfront.internship.event_management.selenium.pages.LoginPopup;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.workfront.internship.event_management.selenium.tests.TestHelper.INVALID_PASSWORD;
import static com.workfront.internship.event_management.selenium.tests.TestHelper.VALID_EMAIL;
import static com.workfront.internship.event_management.selenium.tests.TestHelper.VALID_PASSWORD;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Hermine Turshujyan 8/11/16.
 */
public class LoginPopupTest {
    private static LoginPopup loginPopup;
    private static HomePage homePage;

    @BeforeClass
    public static void setUp() {
        loginPopup = new LoginPopup();
        homePage = new HomePage();
        loginPopup.init();
    }

    @AfterClass
    public static void tearDown() {
        //  loginPopup.getWebDriver().close();
        loginPopup = null;
        homePage = null;
    }

    @Test
    public void login_success() throws InterruptedException {

        homePage.clickLogin();
        loginPopup.typeEmail(VALID_EMAIL);
        loginPopup.typePassword(VALID_PASSWORD);
        loginPopup.clickSignin();

        assertFalse("Login popup is not closed", homePage.getLoginPopup().isDisplayed());
        assertNotNull("Loguot button is not displayed", homePage.getLogoutButton());
    }

    @Test
    public void login_failed() throws InterruptedException {

        homePage.clickLogin();
        loginPopup.typeEmail(VALID_EMAIL);
        loginPopup.typePassword(INVALID_PASSWORD);
        loginPopup.clickSignin();

        assertFalse("Error message is not displayed", loginPopup.getLoginFailedLabel().isDisplayed());
    }

}
