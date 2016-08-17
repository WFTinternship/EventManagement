package com.workfront.internship.event_management.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by Hermine Turshujyan 8/11/16.
 */
public class LoginPopup extends AbstractPage {

    public void typeEmail(String username) throws InterruptedException {
        WebElement emailField = getWebDriver().findElement(By.id("lf_email"));
        Thread.sleep(2000);
        emailField.sendKeys(username);
    }

    public void typePassword(String password) {
        WebElement passwordField = getWebDriver().findElement(By.id("lf_password"));
        passwordField.sendKeys(password);
    }

    public void clickSignin() {
        WebElement signInButton = getWebDriver().findElement(By.id("login"));
        signInButton.click();
    }

    public WebElement getLoginFailedLabel() {
        return getWebDriver().findElement(By.id("login_failed_label"));
    }
}
