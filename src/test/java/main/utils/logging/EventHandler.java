package main.utils.logging;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;


public class EventHandler implements WebDriverEventListener {

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        CustomReporter.log("Navigate to " + url);
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {

    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        CustomReporter.log("Navigate back");
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        CustomReporter.log("Current URL: " + driver.getCurrentUrl());
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        CustomReporter.log("Navigate forward");
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        CustomReporter.log("Current URL: " + driver.getCurrentUrl());
    }

    @Override
    public void beforeNavigateRefresh(WebDriver driver) {
        CustomReporter.log("Refresh page");
    }

    @Override
    public void afterNavigateRefresh(WebDriver driver) {
        CustomReporter.log("Current URL: " + driver.getCurrentUrl());
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        CustomReporter.log("Search for element " + by.toString());
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        if (element != null) {
            CustomReporter.log("Found element " + element.getTagName());
        }
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        CustomReporter.logAction("Click on element " + element.getTagName());
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        CustomReporter.log("Change value of element " + element.getTagName());
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        CustomReporter.log("Changed value of element " + element.getTagName());
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
        CustomReporter.log("Execute script: " + script);
    }

    @Override
    public void afterScript(String script, WebDriver driver) {
        CustomReporter.log("Script executed");
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
    }
}
