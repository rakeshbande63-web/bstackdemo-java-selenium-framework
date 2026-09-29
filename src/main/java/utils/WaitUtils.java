package utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

    /** Waits until the page (document.readyState) has finished loading. */
    public static void ready(WebDriver d) {
        waitFor(d).until(x -> "complete".equals(((JavascriptExecutor) x).executeScript("return document.readyState")));
    }

    public static WebElement visible(WebDriver d, By by) {
        return waitFor(d).until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static WebElement present(WebDriver d, By by) {
        return waitFor(d).until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static WebElement clickable(WebDriver d, By by) {
        return waitFor(d).until(ExpectedConditions.elementToBeClickable(by));
    }

    /** Waits until the element is gone or hidden. */
    public static void invisible(WebDriver d, By by) {
        waitFor(d).until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    /**
     * Waits for the element to be clickable, scrolls it into view and clicks it. If another
     * element overlays it (e.g. the sliding cart), falls back to a JavaScript click.
     */
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

    /** Sends the given text/keys to the element once it is present. */
    public static void type(WebDriver d, By by, CharSequence... keys) {
        present(d, by).sendKeys(keys);
    }

    /** Clears the field and types the value. */
    public static void fill(WebDriver d, By by, String value) {
        WebElement field = visible(d, by);
        field.clear();
        field.sendKeys(value);
    }

    /** Returns true if any of the locators becomes displayed within the given time. */
    public static boolean waitForAny(WebDriver d, int seconds, By... locators) {
        try {
            return waitFor(d, seconds).until(x -> {
                for (By by : locators) {
                    for (WebElement e : x.findElements(by)) {
                        if (e.isDisplayed()) {
                            return true;
                        }
                    }
                }
                return false;
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** True if the element exists and is displayed right now (no waiting). */
    public static boolean isDisplayedNow(WebDriver d, By by) {
        for (WebElement e : d.findElements(by)) {
            try {
                if (e.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // element was re-rendered; treat as not displayed
            }
        }
        return false;
    }

    /** Accepts a JavaScript alert if one appears within the time limit; returns its text or null. */
    public static String acceptAlertIfPresent(WebDriver d, int seconds) {
        try {
            Alert alert = waitFor(d, seconds).until(ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            alert.accept();
            return text;
        } catch (TimeoutException e) {
            return null;
        }
    }
}
