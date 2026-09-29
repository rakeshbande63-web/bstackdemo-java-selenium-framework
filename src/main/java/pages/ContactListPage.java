package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.ConfigReader;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * /contactList page.
 * Confirmed: logout (referenced by a public UI-test tutorial built against this exact app).
 * Assumed, not independently verified against the live DOM: add-contact button id and the
 * table row structure. If TC_UI_003 fails on "add-contact", inspect the real button (F12)
 * and update addContactButton below.
 */
public class ContactListPage {

    private final WebDriver d;

    private final By logoutButton = By.id("logout");
    private final By addContactButton = By.id("add-contact");
    // Confirmed against the live DOM: rows are direct children of #myTable with
    // class="contactTableBodyRow" - NOT inside <tbody>, which is empty on this page.
    private final By contactRows = By.xpath("//table[@id='myTable']/tr[contains(@class,'contactTableBodyRow')]");
    private final By welcomeHeading = By.xpath("//h1[contains(normalize-space(),'Contact List')]");

    public ContactListPage(WebDriver d) {
        this.d = d;
    }

    public ContactListPage waitUntilLoaded() {
        WaitUtils.ready(d);
        WaitUtils.visible(d, logoutButton);
        return this;
    }

    public boolean isDisplayed() {
        return WaitUtils.isDisplayedNow(d, logoutButton);
    }

    public LoginPage logout() {
        WaitUtils.click(d, logoutButton);
        return new LoginPage(d);
    }

    public ContactFormPage goToAddContact() {
        WaitUtils.click(d, addContactButton);
        return new ContactFormPage(d);
    }

    public int contactCount() {
        return d.findElements(contactRows).size();
    }

    /** Waits until the table shows exactly the expected number of contact rows. */
    public ContactListPage waitForContactCount(int expected) {
        WaitUtils.waitFor(d).until(
                org.openqa.selenium.support.ui.ExpectedConditions.numberOfElementsToBe(contactRows, expected));
        return this;
    }

    public List<String> contactRowsText() {
        List<String> rows = new ArrayList<>();
        for (WebElement e : d.findElements(contactRows)) {
            rows.add(e.getText().trim());
        }
        return rows;
    }

    /** Clicks the row whose text contains the given name (e.g. a contact's first name). */
    public void openContact(String nameContains) {
        WaitUtils.click(d, By.xpath("//table[@id='myTable']/tr[contains(@class,'contactTableBodyRow')]"
                + "[contains(.,'" + nameContains + "')]"));
    }
}
