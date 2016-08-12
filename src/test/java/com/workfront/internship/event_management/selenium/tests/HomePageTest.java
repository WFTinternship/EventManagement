package com.workfront.internship.event_management.selenium.tests;

import com.workfront.internship.event_management.selenium.pages.HomePage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebElement;

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
    }

    @Test
    public void loginButtonClick() throws InterruptedException {
        WebElement loginPopup = homePage.clickLogin();
        Assert.assertNotNull("Login Popup is not displayed", loginPopup);
    }
}
