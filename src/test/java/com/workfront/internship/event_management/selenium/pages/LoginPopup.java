package com.workfront.internship.event_management.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by Hermine Turshujyan 8/11/16.
 */
public class LoginPopup extends AbstractPage {

    public void typeUsername(String username) throws InterruptedException {
        WebElement usernameField = getWebDriver().findElement(By.id("username"));
        Thread.sleep(1000);
        usernameField.sendKeys(username);
    }

    public void typePassword(String password) {
        WebElement passwordField = getWebDriver().findElement(By.name("password"));
        passwordField.sendKeys(password);
    }

    public void clickSignin() {
        WebElement signinButton = getWebDriver().findElement(By.id("login"));
        signinButton.click();
    }
}
