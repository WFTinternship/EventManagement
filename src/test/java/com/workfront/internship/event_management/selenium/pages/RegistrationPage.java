package com.workfront.internship.event_management.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.workfront.internship.event_management.selenium.TestHelper.REGISTRATION_PAGE_URL;

/**
 * Created by Hermine Turshujyan 8/16/16.
 */
public class RegistrationPage extends AbstractPage {


    public RegistrationPage() {
        init(REGISTRATION_PAGE_URL);
    }

    public void redirectToRegistrationPage() {
        redirectToPage(REGISTRATION_PAGE_URL);
    }

    //get web elements
    public WebElement getFirstNameField() {
        return getWebDriver().findElement(By.id("firstName"));
    }

    public WebElement getLastNameField() {
        return getWebDriver().findElement(By.id("lastName"));
    }

    public WebElement getPhoneNumberField() {
        return getWebDriver().findElement(By.id("phone"));
    }

    public WebElement getEmailField() {
        return getWebDriver().findElement(By.id("email"));
    }

    public WebElement getEmailConfirmField() {
        return getWebDriver().findElement(By.id("confirmEmail"));
    }

    public WebElement getPasswordField() {
        return getWebDriver().findElement(By.id("password"));
    }

    public WebElement getPasswordConfirmField() {
        return getWebDriver().findElement(By.id("confirmPassword"));
    }

    public WebElement getEmailFieldError() {
        return getWebDriver().findElement(By.id("email-error"));
    }

    public WebElement getEmailConfirmError() {
        return getWebDriver().findElement(By.id("confirmEmail-error"));
    }

    public WebElement getPasswordConfirmError() {
        return getWebDriver().findElement(By.id("confirmPassword-error"));
    }

    //typing
    public void typeFirstName(String firstName) {
        getFirstNameField().sendKeys(firstName);
    }

    public void typeLastName(String lastName) {
        getLastNameField().sendKeys(lastName);
    }

    public void typeEmail(String email) {
        getEmailField().sendKeys(email);
    }

    public void typeEmailConfirm(String email) {
        getEmailConfirmField().sendKeys(email);
    }

    public void typePassword(String email) {
        getPasswordField().sendKeys(email);
    }

    public void typePasswordConfirm(String email) {
        getPasswordConfirmField().sendKeys(email);
    }


    //actions
    public void clickRegister() {
        WebElement registerButton = getWebDriver().findElement(By.id("reg_submit"));
        registerButton.click();
    }

}
