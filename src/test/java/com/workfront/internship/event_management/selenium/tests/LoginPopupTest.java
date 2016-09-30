package com.workfront.internship.event_management.selenium.tests;

import com.workfront.internship.event_management.selenium.pages.HomePage;
import com.workfront.internship.event_management.selenium.pages.LoginPopup;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    @Before
    public void setUp() {
        loginPopup = new LoginPopup();
        homePage = new HomePage();
        homePage.init(HOME_PAGE_URL);
    }

    @After
    public void tearDown() {
        homePage.getWebDriver().close();
        loginPopup = null;
        homePage = null;
    }


    @Test
    public void login_success() throws InterruptedException {

        homePage.clickLoginButton();

        loginPopup.typeEmail(EXISTING_EMAIL);
        loginPopup.typePassword(VALID_PASSWORD);

        loginPopup.clickSignin();

        WebDriverWait wait = new WebDriverWait(homePage.getWebDriver(), 15, 100);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_button")));

        assertFalse("Login popup is not closed", homePage.getLoginPopup().isDisplayed());
    }

    @Test
    public void login_failed() throws InterruptedException {

        homePage.clickLoginButton();
        loginPopup.typeEmail(EXISTING_EMAIL);
        loginPopup.typePassword(INVALID_PASSWORD);

        loginPopup.clickSignin();

        WebElement loginFailedLabel = loginPopup.getLoginFailedLabel();
        assertTrue("Error message is not displayed", loginFailedLabel.isDisplayed());
    }

}
