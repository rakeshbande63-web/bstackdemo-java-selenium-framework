package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

/** Shipping-details form and order confirmation. */
public class CheckoutPage {

    private final WebDriver d;

    private final By firstName = By.xpath("//input[@id='firstNameInput']");
    private final By lastName = By.xpath("//input[@id='lastNameInput']");
    private final By address = By.xpath("//input[@id='addressLine1Input']");
    private final By province = By.xpath("//input[@id='provinceInput']");
    private final By postCode = By.xpath("//input[@id='postCodeInput']");
    private final By submit = By.xpath("//button[@id='checkout-shipping-continue']");
    private final By confirmation = By.xpath("//*[contains(normalize-space(text()),'successfully placed')]");

    public CheckoutPage(WebDriver d) {
        this.d = d;
    }

    public CheckoutPage waitForForm() {
        WaitUtils.visible(d, firstName);
        return this;
    }

    public void placeOrder(String first, String last, String addressLine, String state, String zip) {
        waitForForm();
        fill(firstName, first);
        fill(lastName, last);
        fill(address, addressLine);
        fill(province, state);
        fill(postCode, zip);
        WaitUtils.click(d, submit);
    }

    private void fill(By locator, String value) {
        WebElement field = WaitUtils.visible(d, locator);
        field.clear();
        field.sendKeys(value);
    }

    public boolean isOrderConfirmed() {
        return WaitUtils.waitForAny(d, 15, confirmation);
    }

    public String confirmationText() {
        return WaitUtils.visible(d, confirmation).getText();
    }
}
