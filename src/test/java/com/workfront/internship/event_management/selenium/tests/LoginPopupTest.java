package com.workfront.internship.event_management.selenium.tests;

import com.workfront.internship.event_management.selenium.pages.HomePage;
import com.workfront.internship.event_management.selenium.pages.LoginPopup;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import static com.workfront.internship.event_management.selenium.TestHelper.*;
import static junit.framework.TestCase.assertTrue;
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
    }

    @AfterClass
    public static void tearDownClass() {
        homePage.getWebDriver().close();
        loginPopup = null;
        homePage = null;
    }

    @After
    public void tearDown() {
        // redirectToHome();
    }


    @Test
    public void login_success() throws InterruptedException {

        homePage.clickLogin();
        loginPopup.typeEmail(EXISTING_EMAIL);
        loginPopup.typePassword(VALID_PASSWORD);
        loginPopup.clickSignin();

        assertFalse("Login popup is not closed", homePage.getLoginPopup().isDisplayed());
        assertNotNull("Loguot button is not displayed", homePage.getLogoutButton());
    }

    @Test
    public void login_failed() throws InterruptedException {

        homePage.clickLogin();
        loginPopup.typeEmail(EXISTING_EMAIL);
        loginPopup.typePassword(INVALID_PASSWORD);

        loginPopup.clickSignin();

        WebElement loginFailedLabel = loginPopup.getLoginFailedLabel();
        assertTrue("Error message is not displayed", loginFailedLabel.isDisplayed());
    }

}
