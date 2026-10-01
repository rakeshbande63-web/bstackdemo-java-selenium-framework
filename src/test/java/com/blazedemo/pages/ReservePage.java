package com.blazedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.blazedemo.utils.WaitUtils;

/**
 * Reserve page (/reserve.php) - lists available flights, one "Choose This Flight"
 * button per row. Confirmed: the button's visible text/value, referenced identically
 * across every public BlazeDemo Selenium example found during development.
 */
public class ReservePage {

    private final WebDriver d;

    private final By chooseFlightButtons = By.xpath("//input[@value='Choose This Flight']");

    public ReservePage(WebDriver d) {
        this.d = d;
    }

    public ReservePage waitForLoaded() {
        // Waiting on the buttons themselves, rather than an assumed heading tag, since the
        // buttons' text is the one thing confirmed for this exact page.
        WaitUtils.visible(d, chooseFlightButtons);
        return this;
    }

    public int flightCount() {
        return d.findElements(chooseFlightButtons).size();
    }

    /** Chooses the first listed flight (used by the smoke test and the data-driven test). */
    public PurchasePage chooseFirstFlight() {
        waitForLoaded();
        WaitUtils.click(d, chooseFlightButtons);
        return new PurchasePage(d);
    }
}
