package com.blazedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import com.blazedemo.utils.ConfigReader;
import com.blazedemo.utils.WaitUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Home page (/index.php).
 * Confirmed against the live site (fetched during development): two <select> dropdowns
 * named fromPort/toPort, and a "Find Flights" submit button with class btn-primary.
 */
public class HomePage {

    private final WebDriver d;

    private final By fromPortSelect = By.name("fromPort");
    private final By toPortSelect = By.name("toPort");
    private final By findFlightsButton = By.cssSelector("input.btn-primary");

    public HomePage(WebDriver d) {
        this.d = d;
    }

    public HomePage open() {
        d.get(ConfigReader.get("baseUrl") + "/index.php");
        WaitUtils.ready(d);
        WaitUtils.visible(d, fromPortSelect);
        return this;
    }

    public boolean isLoaded() {
        return WaitUtils.isDisplayedNow(d, fromPortSelect) && WaitUtils.isDisplayedNow(d, toPortSelect);
    }

    public HomePage selectDeparture(String city) {
        WaitUtils.selectByText(d, fromPortSelect, city);
        return this;
    }

    public HomePage selectDestination(String city) {
        WaitUtils.selectByText(d, toPortSelect, city);
        return this;
    }

    public ReservePage findFlights() {
        WaitUtils.click(d, findFlightsButton);
        return new ReservePage(d);
    }

    /** TC01/TC07 helper: choose the same city for both, then submit. */
    public ReservePage searchFlights(String fromCity, String toCity) {
        selectDeparture(fromCity);
        selectDestination(toCity);
        return findFlights();
    }

    public List<String> getDepartureCityOptions() {
        return new Select(WaitUtils.visible(d, fromPortSelect)).getOptions()
                .stream().map(e -> e.getText().trim()).collect(Collectors.toList());
    }

    public List<String> getDestinationCityOptions() {
        return new Select(WaitUtils.visible(d, toPortSelect)).getOptions()
                .stream().map(e -> e.getText().trim()).collect(Collectors.toList());
    }
}
