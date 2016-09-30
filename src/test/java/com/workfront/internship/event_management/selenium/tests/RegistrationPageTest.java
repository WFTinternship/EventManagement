package com.workfront.internship.event_management.selenium.tests;

import com.workfront.internship.event_management.selenium.pages.RegistrationPage;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.workfront.internship.event_management.selenium.TestHelper.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by Hermine Turshujyan 8/16/16.
 */
public class RegistrationPageTest {

    static private RegistrationPage registrationPage;

    @Before
    public void setUp() {
        registrationPage = new RegistrationPage();
        registrationPage.init(REGISTRATION_PAGE_URL);
    }

    @After
    public void tearDown() {
        registrationPage.getWebDriver().close();
        registrationPage = null;
    }

    @Test
    public void registration_fail_existingEmail() throws InterruptedException {

        registrationPage.typeEmail(EXISTING_EMAIL);
        registrationPage.getPasswordField().sendKeys("\t");

        assertTrue("Error message is not displayed", registrationPage.getEmailFieldError().isDisplayed());
    }

    @Test
    public void registration_fail_wrongEmailFormat() throws InterruptedException {

        registrationPage.typeEmail(INVALID_EMAIL);
        registrationPage.getPasswordField().sendKeys("\t");

        assertTrue("Error message is not displayed", registrationPage.getEmailFieldError().isDisplayed());
    }

    @Test
    public void registration_fail_emailsDoNotMatch() throws InterruptedException {

        registrationPage.typeEmail(NON_EXISTING_EMAIL);
        registrationPage.typeEmailConfirm("test@test.com");
        registrationPage.getPasswordField().sendKeys("\t");

        assertTrue("Error message is not displayed", registrationPage.getEmailConfirmError().isDisplayed());
    }

    @Test
    public void registration_fail_passwordsDoNotMatch() throws InterruptedException {

        registrationPage.typePassword(VALID_PASSWORD);
        registrationPage.typePasswordConfirm(INVALID_PASSWORD);

        registrationPage.getEmailField().sendKeys("\t");

        assertTrue("Error message is not displayed", registrationPage.getPasswordConfirmError().isDisplayed());
    }

    @Test
    public void registration_success() throws InterruptedException {

        registrationPage.typeFirstName(FIRST_NAME);
        registrationPage.typeLastName(LAST_NAME);
        registrationPage.typeEmail(NON_EXISTING_EMAIL);
        registrationPage.typeEmailConfirm(NON_EXISTING_EMAIL);
        registrationPage.typePassword(VALID_PASSWORD);
        registrationPage.typePasswordConfirm(VALID_PASSWORD);

        registrationPage.clickRegister();

        WebDriverWait wait = new WebDriverWait(registrationPage.getWebDriver(), 15, 100);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("flexslider"))); //slider

        String currentUrl = registrationPage.getWebDriver().getCurrentUrl();
        assertEquals("Page isn't redirecting properly", currentUrl, HOME_PAGE_URL);
    }
}
