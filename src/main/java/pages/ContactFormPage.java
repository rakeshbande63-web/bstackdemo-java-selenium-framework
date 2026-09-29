package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

/**
 * /addContact and /editContact/{id} forms.
 * NOT independently verified against the live DOM - field ids follow this app's REST API
 * field names (firstName, lastName, birthdate, email, phone, street1, street2, city,
 * stateProvince, postalCode, country), which is how this app's own form is commonly built,
 * but that convention is not confirmed the way LoginPage/AddUserPage/logout are.
 * If TC_UI_003 fails here, inspect the real form (F12) and correct the field ids below.
 */
public class ContactFormPage {

    private final WebDriver d;

    private final By firstName = By.id("firstName");
    private final By lastName = By.id("lastName");
    private final By birthdate = By.id("birthdate");
    private final By email = By.id("email");
    private final By phone = By.id("phone");
    private final By street1 = By.id("street1");
    private final By street2 = By.id("street2");
    private final By city = By.id("city");
    private final By stateProvince = By.id("stateProvince");
    private final By postalCode = By.id("postalCode");
    private final By country = By.id("country");
    private final By submit = By.id("submit");
    private final By cancel = By.id("cancel");

    public ContactFormPage(WebDriver d) {
        this.d = d;
    }

    public ContactFormPage waitForForm() {
        WaitUtils.visible(d, firstName);
        return this;
    }

    public ContactListPage submitContact(String fName, String lName, String bDate, String mail, String ph,
                                          String addr1, String addr2, String cty, String state,
                                          String zip, String ctry) {
        waitForForm();
        WaitUtils.fill(d, firstName, fName);
        WaitUtils.fill(d, lastName, lName);
        if (bDate != null) WaitUtils.fill(d, birthdate, bDate);
        WaitUtils.fill(d, email, mail);
        if (ph != null) WaitUtils.fill(d, phone, ph);
        if (addr1 != null) WaitUtils.fill(d, street1, addr1);
        if (addr2 != null) WaitUtils.fill(d, street2, addr2);
        if (cty != null) WaitUtils.fill(d, city, cty);
        if (state != null) WaitUtils.fill(d, stateProvince, state);
        if (zip != null) WaitUtils.fill(d, postalCode, zip);
        if (ctry != null) WaitUtils.fill(d, country, ctry);
        WaitUtils.click(d, submit);
        return new ContactListPage(d);
    }

    public ContactListPage cancelForm() {
        WaitUtils.click(d, cancel);
        return new ContactListPage(d);
    }
}
