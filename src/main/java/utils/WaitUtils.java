package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class WaitUtils {

    private WaitUtils() {
    }

    private static WebDriverWait w(WebDriver d) {
        return (WebDriverWait) new WebDriverWait(
                d,
                Duration.ofSeconds(ConfigReader.getInt("timeout"))
        ).ignoring(StaleElementReferenceException.class);
    }

    public static WebElement visible(WebDriver d, By locator) {
        return w(d).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement clickable(WebDriver d, By locator) {
        return w(d).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void ready(WebDriver d) {
        w(d).until(x ->
                "complete".equals(
                        ((JavascriptExecutor) x).executeScript("return document.readyState")
                )
        );
    }

    public static boolean visibleIfPresent(WebDriver d, By locator) {
        try {
            return w(d).until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}
