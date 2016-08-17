package com.workfront.internship.event_management.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by Hermine Turshujyan 8/11/16.
 */
public class AbstractPage {

    static private WebDriver webDriver;

    static public void init(String url) {
        // webDriver = new FirefoxDriver();

        webDriver = new ChromeDriver();
        webDriver.get(url);
    }

    static public void redirectToPage(String url) {
        webDriver.get(url);
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

}
