package com.workfront.internship.event_management.selenium.tests;

import com.workfront.internship.event_management.selenium.pages.HomePage;
import com.workfront.internship.event_management.selenium.pages.LoginPopup;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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
        loginPopup.getWebDriver().close();
    }

    @Test
    public void login_success() throws InterruptedException {
        homePage.clickLogin();
        loginPopup.typeUsername("turshujyan@gmail.com");
        loginPopup.typePassword("turshujyan");
        loginPopup.clickSignin();

        assertFalse("login popup is not closed", homePage.getLoginPopup().isDisplayed());
        assertNotNull("loguot button is not displayed", homePage.getLogoutButton());
    }

}
