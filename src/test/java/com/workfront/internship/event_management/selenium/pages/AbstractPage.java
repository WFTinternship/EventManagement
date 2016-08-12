package com.workfront.internship.event_management.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by Hermine Turshujyan 8/11/16.
 */
public class AbstractPage {
    static private WebDriver webDriver;

//    AbstractPage() {
////        webDriver = new FirefoxDriver();
////        webDriver.get("http://localhost:8080");
//        webDriver = new ChromeDriver();
//        webDriver.get("http://localhost:8080");
//    }

    static public void init() {
        webDriver = new ChromeDriver();
        webDriver.get("http://localhost:8080");
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }


}
