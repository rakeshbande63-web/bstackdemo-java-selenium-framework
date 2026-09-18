package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

/**
 * Checkout/shipping Page Object.
 */
public class CheckoutPage {

    private final WebDriver d;
    private final By fn = By.id("firstNameInput");
    private final By ln = By.id("lastNameInput");
    private final By addr = By.id("addressLine1Input");
    private final By state = By.id("provinceInput");
    private final By zip = By.id("postCodeInput");
    private final By submit = By.id("checkout-shipping-continue");
    private final By confirmation = By.id("confirmation-message");
    // Optional/additional fields commonly present on checkout forms. These will
    // be used if present on the page but are not required by the existing
    // tests. Keep their locators here so the page object can fill "all details".
    private final By email = By.id("emailInput");
    private final By phone = By.id("phoneInput");
    private final By city = By.id("cityInput");

    public CheckoutPage(WebDriver d) {
        this.d = d;
    }

    public void placeOrder(String firstName, String lastName, String address,
                           String stateValue, String zipValue) {
        // Fill the core required fields
        fill(fn, firstName);
        fill(ln, lastName);
        fill(addr, address);
        fill(state, stateValue);
        fill(zip, zipValue);

        // Try to fill optional fields if present on the page. These values can
        // be supplied via system properties or config properties if desired.
        safeFillIfPresent(email, System.getProperty("billing.email"));
        safeFillIfPresent(phone, System.getProperty("billing.phone"));
        safeFillIfPresent(city, System.getProperty("billing.city"));

        WaitUtils.clickable(d, submit).click();
        // Wait for next page or confirmation element to be ready
        WaitUtils.ready(d);
    }

    /**
     * Overload that accepts additional contact fields when available.
     */
    public void placeOrder(String firstName, String lastName, String address,
                           String stateValue, String zipValue,
                           String emailValue, String phoneValue, String cityValue) {
        fill(fn, firstName);
        fill(ln, lastName);
        fill(addr, address);
        fill(state, stateValue);
        fill(zip, zipValue);

        safeFillIfPresent(email, emailValue);
        safeFillIfPresent(phone, phoneValue);
        safeFillIfPresent(city, cityValue);

        WaitUtils.clickable(d, submit).click();
        WaitUtils.ready(d);
    }

    private void fill(By locator, String value) {
        WebElement e = WaitUtils.visible(d, locator);
        e.clear();
        e.sendKeys(value == null ? "" : value);
    }

    private void safeFillIfPresent(By locator, String value) {
        try {
            if (value == null) return; // nothing to fill
            if (!d.findElements(locator).isEmpty()) {
                fill(locator, value);
            }
        } catch (Exception ignored) {
            // Ignore failures to fill optional fields; they shouldn't stop flow
        }
    }

    public boolean isOrderConfirmed() {
        try {
            WebElement e = WaitUtils.visible(d, confirmation);
            return e != null && e.isDisplayed();
        } catch (RuntimeException re) {
            // visible() will throw if element isn't found/visible within timeout
            return !d.findElements(confirmation).isEmpty()
                    && d.findElements(confirmation).stream().anyMatch(WebElement::isDisplayed);
        }
    }
}
