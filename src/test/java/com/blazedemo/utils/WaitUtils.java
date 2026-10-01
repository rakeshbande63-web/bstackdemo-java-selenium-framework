package com.blazedemo.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Explicit-wait helpers. No Thread.sleep anywhere. */
public final class WaitUtils {

    private WaitUtils() {
    }

    public static WebDriverWait waitFor(WebDriver d) {
        return waitFor(d, ConfigReader.getInt("timeout"));
    }

    public static WebDriverWait waitFor(WebDriver d, int seconds) {
        WebDriverWait w = new WebDriverWait(d, Duration.ofSeconds(seconds));
        w.ignoring(StaleElementReferenceException.class);
        return w;
    }

    public static void ready(WebDriver d) {
        waitFor(d).until(x -> "complete".equals(((JavascriptExecutor) x).executeScript("return document.readyState")));
    }

    public static WebElement visible(WebDriver d, By by) {
        return waitFor(d).until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static WebElement clickable(WebDriver d, By by) {
        return waitFor(d).until(ExpectedConditions.elementToBeClickable(by));
    }

    public static void click(WebDriver d, By by) {
        WebElement element = clickable(d, by);
        ((JavascriptExecutor) d).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        try {
            element.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            WebElement fresh = visible(d, by);
            ((JavascriptExecutor) d).executeScript("arguments[0].click();", fresh);
        }
    }

    public static void fill(WebDriver d, By by, String value) {
        WebElement field = visible(d, by);
        field.clear();
        field.sendKeys(value);
    }

    /** Selects a <select> option by its visible text. */
    public static void selectByText(WebDriver d, By by, String visibleText) {
        new Select(visible(d, by)).selectByVisibleText(visibleText);
    }

    public static boolean isDisplayedNow(WebDriver d, By by) {
        for (WebElement e : d.findElements(by)) {
            try {
                if (e.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // re-rendered; treat as not displayed
            }
        }
        return false;
    }

    /** Returns true if the locator becomes displayed within the given time, false on timeout. */
    public static boolean waitUntilDisplayed(WebDriver d, By by, int seconds) {
        try {
            waitFor(d, seconds).until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}
