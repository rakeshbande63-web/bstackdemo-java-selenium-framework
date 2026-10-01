package com.blazedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.blazedemo.utils.WaitUtils;

/**
 * Confirmation page shown after a successful purchase.
 * Confirmed: the exact confirmation text "Thank you for your purchase today!", used
 * identically in BlazeMeter's own Taurus documentation for this app.
 */
public class ConfirmationPage {

    private final WebDriver d;

    private final By confirmationHeading = By.xpath("//*[contains(normalize-space(text()),'Thank you for your purchase today')]");

    public ConfirmationPage(WebDriver d) {
        this.d = d;
    }

    public boolean isDisplayed(int timeoutSeconds) {
        return WaitUtils.waitUntilDisplayed(d, confirmationHeading, timeoutSeconds);
    }

    public String getConfirmationText() {
        return WaitUtils.visible(d, confirmationHeading).getText();
    }
}
