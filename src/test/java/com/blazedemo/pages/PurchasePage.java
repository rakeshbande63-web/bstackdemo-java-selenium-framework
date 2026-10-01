package com.blazedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.blazedemo.utils.WaitUtils;

/**
 * Purchase page (/purchase.php) - passenger and payment details.
 *
 * Confirmed against the live site or independently-verified public Selenium scripts:
 * inputName, zipCode, creditCardNumber, nameOnCard.
 * NOT independently confirmed, but following the same field-naming convention seen on
 * this exact page in multiple working automation projects: address, city, state,
 * cardType, creditCardMonth, creditCardYear, rememberMe. If a field lookup times out,
 * inspect the real form (F12 -> copy outerHTML of the &lt;form&gt;) and correct the id here.
 */
public class PurchasePage {

    private final WebDriver d;

    private final By nameInput = By.id("inputName");
    private final By addressInput = By.id("address");
    private final By cityInput = By.id("city");
    private final By stateInput = By.id("state");
    private final By zipCodeInput = By.id("zipCode");
    private final By cardTypeSelect = By.id("cardType");
    private final By cardNumberInput = By.id("creditCardNumber");
    private final By cardMonthSelect = By.id("creditCardMonth");
    private final By cardYearSelect = By.id("creditCardYear");
    private final By nameOnCardInput = By.id("nameOnCard");
    private final By rememberMeCheckbox = By.id("rememberMe");
    private final By purchaseButton = By.cssSelector("input.btn-primary");

    public PurchasePage(WebDriver d) {
        this.d = d;
    }

    public PurchasePage waitForForm() {
        WaitUtils.visible(d, nameInput);
        return this;
    }

    public PurchasePage fillPassengerDetails(String name, String address, String city, String state, String zip) {
        waitForForm();
        WaitUtils.fill(d, nameInput, name);
        WaitUtils.fill(d, addressInput, address);
        WaitUtils.fill(d, cityInput, city);
        WaitUtils.fill(d, stateInput, state);
        WaitUtils.fill(d, zipCodeInput, zip);
        return this;
    }

    /** Leaves the credit card number blank (TC05). */
    public PurchasePage fillPaymentDetails(String cardNumber, String nameOnCard) {
        if (cardNumber != null && !cardNumber.isEmpty()) {
            WaitUtils.fill(d, cardNumberInput, cardNumber);
        }
        WaitUtils.fill(d, nameOnCardInput, nameOnCard);
        return this;
    }

    public ConfirmationPage submitPurchase() {
        WaitUtils.click(d, purchaseButton);
        return new ConfirmationPage(d);
    }

    /** Convenience for the smoke/functional/data-driven flow: fills everything, then submits. */
    public ConfirmationPage purchaseFlight(String name, String address, String city, String state, String zip,
                                            String cardNumber, String nameOnCard) {
        fillPassengerDetails(name, address, city, state, zip);
        fillPaymentDetails(cardNumber, nameOnCard);
        return submitPurchase();
    }

    public boolean isDisplayed() {
        return WaitUtils.isDisplayedNow(d, nameInput);
    }
}
